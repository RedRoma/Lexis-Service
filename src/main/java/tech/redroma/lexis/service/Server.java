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

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;
import tech.aroma.client.Aroma;
import tech.aroma.client.Urgency;
import tech.redroma.lexis.service.words.LexisWord;

import static java.util.stream.Collectors.toList;

/**
 *
 * @author SirWellington
 */
public final class Server
{

    private final static Logger LOG = LoggerFactory.getLogger(Server.class);
    final static Aroma AROMA = Aroma.create("de354716-b063-4b83-bdb4-ff9d05150563");

    final static String APPLICATION_JSON = "application/json";

    public static void main(String[] args)
    {
        final int port = 7777;

        Server server = new Server();
        server.serveAtPort(port);
        server.setupRoutes();
    }

    void serveAtPort(int port)
    {
        LOG.info("Starting server at {}");
        Spark.port(port);

        AROMA.begin()
            .titled("Service Launched")
            .withUrgency(Urgency.LOW)
            .send();
    }

    void setupRoutes()
    {
        Spark.get("/", this::getAllWords);
        Spark.get("/search/starting-with/:searchTerm", this::getAllWordsStartingWith);
        Spark.get("/search/containing/:searchTerm", this::getAllWordsContaining);
        Spark.get("/search/containing-in-definition/:searchTerm", this::getAllWordsContainingInDefinition);
    }

    Object getAllWords(Request request, Response response)
    {
        LOG.info("Received request to get all words: {}", request);
        long begin = System.currentTimeMillis();
        
        response.status(200);
        response.type(APPLICATION_JSON);

        List<LexisWord> words = Words.WORDS;

        AROMA.begin().titled("Request Received")
            .text("Received request to get all {} words.", words.size())
            .withUrgency(Urgency.MEDIUM)
            .send();

        List<JsonObject> allWords = words.stream()
            .map(word -> word.asJSON())
            .collect(toList());

        long latency = System.currentTimeMillis() - begin;
        AROMA.begin().titled("Request Completed")
            .text("Completed request to get all words. Operation took {}ms", latency)
            .withUrgency(Urgency.MEDIUM)
            .send();

        return allWords;
    }

    Object getAllWordsStartingWith(Request request, Response response)
    {
        String term = request.params("searchTerm");

        AROMA.begin().titled("Received Request")
            .withUrgency(Urgency.MEDIUM)
            .text("Getting all words starting with {}", term)
            .send();

        if (Strings.isNullOrEmpty(term))
        {
            return missingSearchTerm(response);
        }
        
        response.type(APPLICATION_JSON);

        LOG.info("Received request to get all words starting with: {}", term);

        Predicate<LexisWord> filter = (LexisWord word) ->
        {
            return word.getForms().stream().anyMatch((form) -> form.startsWith(term));
        };
        
        long start = System.currentTimeMillis();

        List<JsonObject> matches = Words.WORDS.parallelStream()
            .filter(filter)
            .map(word -> word.asJSON())
            .collect(toList());

        long latency = System.currentTimeMillis() - start;

        LOG.info("Found {} words matching search term {}. Operation took {}ms", matches.size(), term, latency);

        AROMA.begin().titled("Searched Words")
            .withUrgency(Urgency.LOW)
            .text("Found {} words starting with '{}' in {}ms", matches.size(), term, latency)
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

        response.status(200);
        response.type(APPLICATION_JSON);

        Predicate<LexisWord> filter = (word) ->
        {
            return word.getForms().stream().anyMatch((form) -> form.contains(term));
        };

        long begin = System.currentTimeMillis();
        List<JsonObject> results = Words.WORDS.parallelStream()
            .filter(filter)
            .map(word -> word.asJSON())
            .collect(toList());
        long latency = System.currentTimeMillis() - begin;

        LOG.info("Found {} words containing '{}' in {}ms", results.size(), term, latency);
        AROMA.begin().titled("Searched Words")
            .withUrgency(Urgency.LOW)
            .text("Found {} words containing '{}' in {}ms", results.size(), term, latency)
            .send();

        return results;
    }
    
    Object getAllWordsContainingInDefinition(Request request, Response response)
    {
        String term = request.params("searchTerm");
        
        if(Strings.isNullOrEmpty(term))
        {
            return missingSearchTerm(response);
        }
        
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
            .map((word) -> word.asJSON())
            .collect(toList());
        long latency = System.currentTimeMillis() - start;

        LOG.info("Found {} words with term '{}' in definition in {}ms", results.size(), term, latency);
        AROMA.begin().titled("Searched Words")
            .withUrgency(Urgency.LOW)
            .text("Found {} words with '{}' in definitions in {}ms", results.size(), term, latency)
            .send();

        return results;
    }

    private Response missingSearchTerm(Response response)
    {
        LOG.warn("Missing search term");
        
        AROMA.begin().titled("Invalid Request")
            .withUrgency(Urgency.HIGH)
            .titled("Received request to search with missing search term")
            .send();
        
        response.status(400);
        response.body("Search Term cannot be empty");
        
        return response;
    }
}
