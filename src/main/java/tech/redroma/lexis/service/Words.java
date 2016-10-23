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


import com.google.common.io.Resources;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.collections.lists.Lists;
import tech.aroma.client.Urgency;
import tech.redroma.lexis.service.words.JSONConvertible;
import tech.redroma.lexis.service.words.LexisWord;

import static com.google.common.base.Charsets.UTF_8;

/**
 *
 * @author SirWellington
 */
final class Words 
{
    private final static Logger LOG = LoggerFactory.getLogger(Words.class);

    /**
     * The Lexis Database is read into this JSON file.
     */
    final static String JSON_FILE = loadJSONFile(); 
    
    final static List<LexisWord> WORDS = loadLexisWords();
    
    private static String loadJSONFile()
    {
        String path = "LexisWords.pretty.json";
        
        LOG.debug("Loading JSON at {}", path);
        
        URL url;
        try
        {
            url = Resources.getResource(path);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to load URL at {}", path, ex);
            
            Server.AROMA.begin().titled("Operation Failed")
                .text("Could not load file at path {}", path, ex)
                .withUrgency(Urgency.HIGH)
                .send();
            
            return "";
        }
       
        try 
        {
            return Resources.toString(url, UTF_8);
        }
        catch(IOException ex)
        {
            LOG.error("Failed to load URL at {}", url, ex);
            
            Server.AROMA.begin().titled("Operation Faile")
                .text("Could not load url {}", url, ex)
                .withUrgency(Urgency.HIGH)
                .send();
            
            return "";
        }
        
    }

    private static List<LexisWord> loadLexisWords()
    {
        List<LexisWord> results = Lists.create();

        JsonElement wordsJson = JSONConvertible.GSON.fromJson(JSON_FILE, JsonElement.class);

        if (!wordsJson.isJsonArray())
        {
            return results;
        }

        for (JsonElement element : wordsJson.getAsJsonArray())
        {
            if (element.isJsonObject())
            {
                LexisWord word = LexisWord.fromJSON(element.getAsJsonObject());

                if (word != null)
                {
                    results.add(word);
                }
            }
        }

        return results;
    }
}
