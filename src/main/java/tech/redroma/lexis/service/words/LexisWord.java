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

 import java.util.List;
 import java.util.Objects;
 import java.util.function.BiConsumer;
 import java.util.function.Supplier;

 import com.google.gson.*;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import sir.wellington.alchemy.collections.lists.Lists;
 import tech.sirwellington.alchemy.annotations.objects.Pojo;

 import static tech.sirwellington.alchemy.arguments.Arguments.*;
 import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Pojo
public final class LexisWord implements JSONConvertible
{

    private final static Logger LOG = LoggerFactory.getLogger(LexisWord.class);

    private List<String> forms;
    private List<Definition> definitions;
    private WordType wordType;
    private SupplementalInformation supplementalInformation;
    private JsonObject jsonForm;

    public LexisWord()
    {
    }

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
        if (jsonForm != null)
        {
            return jsonForm;
        }

        JsonObject object = new JsonObject();

        if (!Lists.isEmpty(forms))
        {
            Supplier<JsonArray> supplier = () -> new JsonArray();
            BiConsumer<JsonArray, String> accumulator = (array, string) -> array.add(string);
            BiConsumer<JsonArray, JsonArray> combiner = (first, second) -> first.addAll(second);
            JsonArray formsArray = forms.stream().collect(supplier, accumulator, combiner);

            object.add(Keys.FORMS, formsArray);

        }

        if (!Lists.isEmpty(definitions))
        {
            JsonArray definitionsArray = new JsonArray();

            for (Definition definition : definitions)
            {
                JsonObject definitionObject = definition.asJSON();
                definitionsArray.add(definitionObject);
            }
            object.add(Keys.DEFINITIONS, definitionsArray);

        }

        if (wordType != null)
        {
            JsonObject wordTypeJson = wordType.asJSON();
            object.add(Keys.WORD_TYPE, wordTypeJson);
        }

        if (supplementalInformation != null)
        {
            JsonObject supplementalInformationJson = supplementalInformation.asJSON();
            object.add(Keys.SUPPLEMENTAL_INFORMATION, supplementalInformationJson);
        }

        this.jsonForm = object;

        return object;
    }

    public static LexisWord fromJSON(JsonObject object)
    {
        checkThat(object).is(notNull());

        LexisWord word = new LexisWord();

        try
        {
            JsonArray formsArray = object.getAsJsonArray(Keys.FORMS);
            List<String> forms = Lists.create();

            for (JsonElement element : formsArray)
            {
                if (element.isJsonPrimitive())
                {
                    forms.add(element.getAsString());
                }
            }

            word.forms = forms;

            JsonArray definitionsArray = object.getAsJsonArray(Keys.DEFINITIONS);
            List<Definition> definitions = Lists.create();

            for(JsonElement element : definitionsArray)
            {
                if (element.isJsonObject())
                {
                    Definition definition = Definition.fromJSON(element.getAsJsonObject());
                    definitions.add(definition);
                }
            }
            word.definitions = definitions;

            JsonObject wordTypeJson = object.getAsJsonObject(Keys.WORD_TYPE);
            word.wordType = WordType.fromJSON(wordTypeJson);

            JsonObject supplementalInformationJson = object.getAsJsonObject(Keys.SUPPLEMENTAL_INFORMATION);
            word.supplementalInformation = SupplementalInformation.fromJSON(supplementalInformationJson);

        }
        catch (Exception ex)
        {
            LOG.error("Failed to parse Lexis Word from {}", object, ex);
        }

        return word;
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
        static final String WORD_TYPE = "word_type";
    }

}
