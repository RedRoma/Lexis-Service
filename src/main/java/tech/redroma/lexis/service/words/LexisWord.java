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
public final class LexisWord
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

    @Pojo
    public static class Definition
    {

        private final List<String> terms;

        Definition(List<String> terms)
        {
            checkThat(terms).is(notNull());

            this.terms = terms;
        }

        public List<String> getTerms()
        {
            return terms;
        }

        @Override
        public int hashCode()
        {
            int hash = 3;
            hash = 97 * hash + Objects.hashCode(this.terms);
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
            final Definition other = (Definition) obj;
            if (!Objects.equals(this.terms, other.terms))
            {
                return false;
            }
            return true;
        }

        @Override
        public String toString()
        {
            return "Definition{" + "terms=" + terms + '}';
        }

    }

}
