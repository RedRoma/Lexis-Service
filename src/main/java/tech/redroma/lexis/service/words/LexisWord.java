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

package tech.redroma.lexis.service.words;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Pojo
public final class LexisWord implements JSONConvertible
{

    private final static Logger LOG = LoggerFactory.getLogger(LexisWord.class);

    private final List<String> forms;
    private final List<Definition> definitions;
    private final WordType wordType;
    private final SupplementalInformation supplementalInformation;

    public LexisWord(List<String> forms, 
                     List<Definition> definitions,
                     WordType wordType,
                     SupplementalInformation supplementalInformation)
    {
        checkThat(forms, definitions, wordType, supplementalInformation)
            .are(notNull());

        this.forms = forms;
        this.definitions = definitions;
        this.wordType = wordType;
        this.supplementalInformation = supplementalInformation;
    }

    @Override
    public JsonObject asJSON()
    {
        JsonObject object = new JsonObject();
        
        Supplier<JsonArray> supplier = () -> new JsonArray();
        BiConsumer<JsonArray, String> accumulator = (array, string) -> array.add(string);
        BiConsumer<JsonArray, JsonArray> combiner = (first, second) -> first.addAll(second);
        JsonArray formsArray = forms.stream().collect(supplier, accumulator, combiner);
        
        
        JsonArray definitionsArray = new JsonArray();
        
        for (Definition definition : definitions)
        {
            JsonObject definitionObject = definition.asJSON();
            definitionsArray.add(definitionObject);
        }
        
        JsonObject wordTypeJson = wordType.asJSON();
        JsonObject supplementalInformationJson = supplementalInformation.asJSON();
        
        object.add(Keys.WORD_TYPE, wordTypeJson);
        object.add(Keys.FORMS, formsArray);
        object.add(Keys.DEFINITIONS, definitionsArray);
        object.add(Keys.SUPPLEMENTAL_INFORMATION, supplementalInformationJson);
        
        return object;
    }

    public List<String> getForms()
    {
        return forms;
    }

    public List<Definition> getDefinitions()
    {
        return definitions;
    }

    public WordType getWordType()
    {
        return wordType;
    }

    public SupplementalInformation getSupplementalInformation()
    {
        return supplementalInformation;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.forms);
        hash = 97 * hash + Objects.hashCode(this.definitions);
        hash = 97 * hash + Objects.hashCode(this.wordType);
        hash = 97 * hash + Objects.hashCode(this.supplementalInformation);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final LexisWord other = (LexisWord) obj;
        if (!Objects.equals(this.forms, other.forms))
        {
            return false;
        }
        if (!Objects.equals(this.definitions, other.definitions))
        {
            return false;
        }
        if (!Objects.equals(this.wordType, other.wordType))
        {
            return false;
        }
        if (!Objects.equals(this.supplementalInformation, other.supplementalInformation))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "LexisWord{" + "forms=" + forms + ", definitions=" + definitions + ", wordType=" + wordType + ", supplementalInformation=" + supplementalInformation + '}';
    }

    
    private static class Keys
    {
        static final String DEFINITIONS = "definitions";
        static final String SUPPLEMENTAL_INFORMATION = "supplemental_information";
        static final String FORMS = "forms";
        static final String WORD_TYPE = "word_ype";
    }

}
