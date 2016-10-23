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
final class Noun extends WordType
{

    private final static Logger LOG = LoggerFactory.getLogger(Noun.class);

    private final Declension declension;
    private final Gender gender;

    public Noun(Declension declension, Gender gender, String wordType)
    {
        super(Types.Noun);
        checkThat(declension, gender).are(notNull());

        this.declension = declension;
        this.gender = gender;
    }

    public Declension getDeclension()
    {
        return declension;
    }

    public Gender getGender()
    {
        return gender;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.declension);
        hash = 37 * hash + Objects.hashCode(this.gender);
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
        final Noun other = (Noun) obj;
        if (this.declension != other.declension)
        {
            return false;
        }
        if (this.gender != other.gender)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Noun{" + "declension=" + declension + ", gender=" + gender + '}';
    }

    static enum Gender
    {
        Male,
        Female,
        Neuter,
        Unknown;

        static Gender fromString(String string)
        {
            checkThat(string).is(nonEmptyString());

            Gender gender = null;

            try
            {
                gender = Gender.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load Gender from {}", string);
            }

            if (gender == null)
            {
                gender = Gender.Unknown;
            }

            return gender;
        }
    }

    static enum Declension
    {
        First,
        Second,
        Third,
        Fourth,
        Fifth,
        Undeclined;

        static Declension fromString(String string)
        {
            checkThat(string).is(nonEmptyString());

            Declension declension = null;

            try
            {
                declension = Declension.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load Declension from {}", string);
            }

            if (declension == null)
            {
                declension = Declension.Undeclined;
            }

            return declension;
        }
    }

}
