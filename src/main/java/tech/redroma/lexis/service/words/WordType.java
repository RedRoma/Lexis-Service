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
class WordType
{

    private final static Logger LOG = LoggerFactory.getLogger(WordType.class);

    final static WordType ADJECTIVE = new WordType(Adjective);
    final static WordType ADVERB = new WordType(Adverb);
    final static WordType CONJUNCTION = new WordType(Conjunction);
    final static WordType INTERJECTION = new WordType(Interjection);
    final static WordType NUMERAL = new WordType(Numeral);
    final static WordType PERSONAL_PRONOUN = new WordType(PersonalPronoun);
    final static WordType PRONOUN = new WordType(Pronoun);

    private final Types wordType;

    public WordType(Types wordType)
    {
        checkThat(wordType).is(notNull());

        this.wordType = wordType;
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

    static enum Types
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
        Verb
        ;
        
    }
    
}
