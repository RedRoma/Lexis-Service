/*
 * Copyright 2016 RedRoma, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.redroma.lexis.service;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.*;
import tech.aroma.client.Aroma;
import tech.aroma.client.Priority;
import tech.redroma.lexis.service.words.LexisWord;

import static java.util.stream.Collectors.toList;

/**
 * @author SirWellington
 */
public final class Server
{

    private final static Logger LOG = LoggerFactory.getLogger(Server.class);
    final static Aroma AROMA = Aroma.Factory.create("0355e009-f92a-494f-bc1c-25bfbe901c7f");

    private final static String APPLICATION_JSON = "application/json";

    public static void main(String[] args)
    {
        final int port = 7777;

        Server server = new Server();
        server.serveAtPort(port);
        server.setupRoutes();
    }

    private void serveAtPort(int port)
    {
        LOG.info("Starting server at {}", port);
        Spark.port(port);

        int count = Words.WORDS.size();

        LOG.info("Server Started; Serving {} words.", count);

        AROMA.begin()
             .titled("Service Launched")
             .withBody("Serving {} words", count)
             .withPriority(Priority.LOW)
             .send();
    }

    void setupRoutes()
    {
        Spark.get("/", this::getAllWords);
        Spark.get("/search/starting-with/:searchTerm", this::getAllWordsStartingWith);
        Spark.get("/search/containing/:searchTerm", this::getAllWordsContaining);
        Spark.get("/search/containing-in-definition/:searchTerm", this::getAllWordsContainingInDefinition);
        Spark.get("/search/any-word", this::getAnyWord);
    }

    Object getAllWords(Request request, Response response)
    {
        LOG.info("Received request to get all words: {}", request);
        long begin = System.currentTimeMillis();

        response.status(200);
        response.type(APPLICATION_JSON);

        List<LexisWord> words = Words.WORDS;

        AROMA.begin().titled("Received Request")
             .withBody("Received request to get all {} words.", words.size())
             .withPriority(Priority.MEDIUM)
             .send();

        List<JsonObject> allWords = words.stream()
                                         .map(word -> word.asJSON())
                                         .collect(toList());

        long latency = System.currentTimeMillis() - begin;
        AROMA.begin().titled("Request Completed")
             .withBody("Completed request to get all words for IP [{}]. Operation took {}ms", request.ip(), latency)
             .withPriority(Priority.MEDIUM)
             .send();

        return allWords;
    }

    Object getAllWordsStartingWith(Request request, Response response)
    {
        String term = request.params("searchTerm");

        AROMA.begin().titled("Received Request")
             .withPriority(Priority.MEDIUM)
             .withBody("From [{}] To GET all words starting with '{}'", request.ip(), term)
             .send();

        if (Strings.isNullOrEmpty(term))
        {
            return missingSearchTerm(response);
        }

        response.type(APPLICATION_JSON);

        LOG.info("Received request from [{}] to get all words starting with: {}", request.ip(), term);

        Predicate<LexisWord> filter = (LexisWord word) ->
        {
            return word.getForms().stream().anyMatch((form) -> form.startsWith(term));
        };

        long start = System.currentTimeMillis();

        List<JsonObject> matches = Words.WORDS.parallelStream()
                                              .filter(filter)
                                              .map(LexisWord::asJSON)
                                              .collect(toList());

        long latency = System.currentTimeMillis() - start;

        LOG.info("Found {} words matching search term {}. Operation took {}ms", matches.size(), term, latency);

        AROMA.begin().titled("Request Complete")
             .withPriority(Priority.LOW)
             .withBody("Found {} words starting with '{}' in {}ms for IP [{}]", matches.size(), term, latency, request.ip())
             .send();

        return matches;
    }

    Object getAllWordsContaining(Request request, Response response)
    {
        String term = request.params("searchTerm");

        if (Strings.isNullOrEmpty(term))
        {
            return missingSearchTerm(response);
        }

        AROMA.begin().titled("Received Request")
             .withPriority(Priority.MEDIUM)
             .withBody("From [{}] to GET all words containing '{}'", request.ip(), term)
             .send();
        LOG.debug("Received request to get all words containing '{}' from IP [{}]", term, request.ip());

        response.status(200);
        response.type(APPLICATION_JSON);

        Predicate<LexisWord> filter = (word) ->
        {
            return word.getForms().stream().anyMatch((form) -> form.contains(term));
        };

        long begin = System.currentTimeMillis();
        List<JsonObject> results = Words.WORDS.parallelStream()
                                              .filter(filter)
                                              .map(LexisWord::asJSON)
                                              .collect(toList());
        long latency = System.currentTimeMillis() - begin;

        LOG.info("Found {} words containing '{}' in {}ms", results.size(), term, latency);

        AROMA.begin().titled("Request Complete")
             .withPriority(Priority.LOW)
             .withBody("Found {} words containing '{}' in {}ms for IP [{}]", results.size(), term, latency, request.ip())
             .send();

        return results;
    }

    Object getAllWordsContainingInDefinition(Request request, Response response)
    {
        String term = request.params("searchTerm");

        if (Strings.isNullOrEmpty(term))
        {
            return missingSearchTerm(response);
        }

        AROMA.begin().titled("Received Request")
             .withPriority(Priority.MEDIUM)
             .withBody("From [{}] to GET all words with '{}' in the definition", request.ip(), term)
             .send();

        response.status(200);
        response.type(APPLICATION_JSON);

        Predicate<LexisWord> filter = (word) ->
        {
            return word.getDefinitions()
                       .stream()
                       .flatMap(def -> def.getTerms().stream())
                       .anyMatch(def -> def.contains(term));
        };

        long start = System.currentTimeMillis();
        List<JsonObject> results = Words.WORDS.parallelStream()
                                              .filter(filter)
                                              .map(LexisWord::asJSON)
                                              .collect(toList());
        long latency = System.currentTimeMillis() - start;

        LOG.info("Found {} words with term '{}' in definition in {}ms", results.size(), term, latency);
        AROMA.begin().titled("Request Complete")
             .withPriority(Priority.LOW)
             .withBody("Found {} words with '{}' in definitions in {}ms for IP [{}]", results.size(), term, latency, request
                     .ip())
             .send();

        return results;
    }

    Object getAnyWord(Request request, Response response)
    {
        long start = System.currentTimeMillis();
        String ip = request.ip();
        LOG.info("Received request to get any word from {}", ip);

        AROMA.begin()
             .titled("Received Request")
             .withBody("From [{}] to GET any word.", ip)
             .withPriority(Priority.LOW)
             .send();

        response.status(200);
        response.type(APPLICATION_JSON);

        LexisWord randomWord = getRandomWord();

        JsonObject json = randomWord.asJSON();
        long latency = System.currentTimeMillis() - start;

        LOG.debug("Operation to load any word turned up {} and took {}ms", json, latency);

        AROMA.begin().titled("Request Complete")
             .withBody("Operation to load any word turned up {} and took {}ms for IP [{}]", json, latency, ip)
             .withPriority(Priority.LOW)
             .send();

        return json;
    }

    private Response missingSearchTerm(Response response)
    {
        LOG.warn("Missing search term");

        AROMA.begin().titled("Invalid Request")
             .withPriority(Priority.HIGH)
             .titled("Received request to search with missing search term")
             .send();

        response.status(400);
        response.body("Search Term cannot be empty");

        return response;
    }

    private LexisWord getRandomWord()
    {
        int size = Words.WORDS.size();
        int index = new Random().nextInt(size);
        if (index >= size)
        {
            index = size - 1;
        }

        return Words.WORDS.get(index);
    }
}
