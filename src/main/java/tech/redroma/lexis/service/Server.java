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
    }

    Object getAllWords(Request request, Response response)
    {
        LOG.info("Received request to get all words: {}", request);
        response.type("application/json");

        List<LexisWord> words = Words.WORDS;

        AROMA.begin().titled("Request Received")
            .text("Received request to get all {} words.", words.size())
            .withUrgency(Urgency.MEDIUM)
            .send();

        List<JsonObject> allWords = words.stream().map(word -> word.asJSON()).collect(toList());
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
            response.status(400);
            response.body("Search Term cannot be empty");
            
            AROMA.begin().titled("Invalid Argument")
                .withUrgency(Urgency.HIGH)
                .text("Received empty search term")
                .send();
            
            return response;
        }

        LOG.info("Received request to get all words starting with: {}", term);

        Predicate<LexisWord> filter = (LexisWord word) ->
        {
            return word.getForms().stream().anyMatch((form) -> form.startsWith(term));
        };
        
        long start = System.currentTimeMillis();

        List<LexisWord> matches = Words.WORDS.parallelStream().filter(filter).collect(toList());
        long latency = System.currentTimeMillis() - start;

        LOG.info("Found {} words matching search term {}. Operation took {}ms", matches.size(), term, latency);

        AROMA.begin().titled("Searched Words")
            .withUrgency(Urgency.LOW)
            .text("Found {} words startin with {} in {}ms", matches.size(), term, latency)
            .send();

        return matches;
    }
}
