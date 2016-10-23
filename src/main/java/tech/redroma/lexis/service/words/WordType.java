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

import com.google.gson.JsonObject;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.redroma.lexis.service.words.WordType.Types.Adjective;
import static tech.redroma.lexis.service.words.WordType.Types.Adverb;
import static tech.redroma.lexis.service.words.WordType.Types.Conjunction;
import static tech.redroma.lexis.service.words.WordType.Types.Interjection;
import static tech.redroma.lexis.service.words.WordType.Types.Numeral;
import static tech.redroma.lexis.service.words.WordType.Types.PersonalPronoun;
import static tech.redroma.lexis.service.words.WordType.Types.Pronoun;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Pojo
public class WordType implements JSONConvertible
{

    private final static Logger LOG = LoggerFactory.getLogger(WordType.class);

    final static WordType ADJECTIVE = new WordType(Adjective);
    final static WordType ADVERB = new WordType(Adverb);
    final static WordType CONJUNCTION = new WordType(Conjunction);
    final static WordType INTERJECTION = new WordType(Interjection);
    final static WordType NUMERAL = new WordType(Numeral);
    final static WordType PERSONAL_PRONOUN = new WordType(PersonalPronoun);
    final static WordType PRONOUN = new WordType(Pronoun);

    private Types wordType;

    public WordType()
    {
        this.wordType = null;
    }
    
    public WordType(Types wordType)
    {
        checkThat(wordType).is(notNull());

        this.wordType = wordType;
    }

    public static WordType fromJSON(JsonObject object)
    {
        checkThat(object).is(notNull());

        try
        {
            String wordTypeEnum = object.get("wordType").getAsString();

            WordType.Types wordType = WordType.Types.valueOf(wordTypeEnum);

            switch (wordType)
            {
                case Adjective: return ADJECTIVE;
                case Adverb: return ADVERB;
                case Conjunction: return CONJUNCTION;
                case Interjection: return INTERJECTION;
                case Numeral: return NUMERAL;
                case PersonalPronoun: return PERSONAL_PRONOUN;
                case Pronoun: return PRONOUN;
            }

            if (wordType == Types.Noun)
            {
                return Noun.fromJSON(object);
            }

            if (wordType == Types.Verb)
            {
                return Verb.fromJSON(object);
            }

            if (wordType == Types.Preposition)
            {
                return Preposition.fromJSON(object);
            }

            LOG.warn("Failed to determine the WordType from JSON: {}", object);

            return null;

        }
        catch (Exception ex)
        {
            LOG.error("Failed to load as WordType: {}", object, ex);
            return null;
        }
    }

    @Override
    public JsonObject asJSON()
    {
        JsonObject json = new JsonObject();

        String wordTypeString = this.wordType.toString();
        json.addProperty("wordType", wordTypeString);

        return json;
    }

    public Types getWordType()
    {
        return wordType;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.wordType);
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
        final WordType other = (WordType) obj;
        if (!Objects.equals(this.wordType, other.wordType))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "WordType{" + "wordType=" + wordType + '}';
    }

    public static enum Types
    {
        Adjective,
        Adverb,
        Conjunction,
        Interjection,
        Noun,
        Numeral,
        PersonalPronoun,
        Preposition,
        Pronoun,
        Verb;

    }

}
