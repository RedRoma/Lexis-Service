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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;
import tech.aroma.client.Aroma;
import tech.aroma.client.Urgency;

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
    }
    
    Object getAllWords(Request request, Response response)
    {
        LOG.info("Received request to get all words: {}", request);
        
        AROMA.begin().titled("Request Received")
            .text("Received request to get all words.")
            .withUrgency(Urgency.MEDIUM)
            .send();
              
        return Words.JSON_FILE;
    }
}
