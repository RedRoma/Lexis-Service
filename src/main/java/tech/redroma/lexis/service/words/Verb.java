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

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;

/**
 *
 * @author SirWellington
 */
@Pojo
final class Verb extends WordType
{

    private final static Logger LOG = LoggerFactory.getLogger(Verb.class);

    private final Conjugation conjugation;
    private final Verb.Type verbType;

    public Verb(Conjugation conjugation, Type verbType, String wordType)
    {
        super(Types.Verb);
        checkThat(conjugation, wordType)
            .are(notNull());

        this.conjugation = conjugation;
        this.verbType = verbType;
    }

    public Conjugation getConjugation()
    {
        return conjugation;
    }

    public Type getVerbType()
    {
        return verbType;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.conjugation);
        hash = 41 * hash + Objects.hashCode(this.verbType);
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
        final Verb other = (Verb) obj;
        if (this.conjugation != other.conjugation)
        {
            return false;
        }
        if (this.verbType != other.verbType)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Verb{" + "conjugation=" + conjugation + ", verbType=" + verbType + '}';
    }

    /**
     * Represents a Verb's conjugation.
     */
    static enum Conjugation
    {
        First,
        Second,
        Third,
        Fourth,
        Irregular,
        Unconjugated;

        static Conjugation fromString(String string)
        {
            checkThat(string).is(nonEmptyString());

            Conjugation conjugation = null;

            try
            {
                conjugation = Conjugation.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load Conjugation from {}", string);
            }

            if (conjugation == null)
            {
                conjugation = Conjugation.Unconjugated;
            }

            return conjugation;
        }

    }

    static enum Type
    {
        Transitive,
        Intransitive,
        Impersonal,
        Deponent,
        SemiDeponent,
        PerfectDefinite,
        Unknown;

        static Type fromString(String string)
        {
            checkThat(string).is(nonEmptyString());

            Type type = null;

            try
            {
                type = Type.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load Verb Type from {}", type);
            }

            if (type == null)
            {
                type = Type.Unknown;
            }

            return type;
        }

    }

}
