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

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.*;

/**
 *
 * @author SirWellington
 */
@Pojo
final class Noun extends WordType
{

    private final static Logger LOG = LoggerFactory.getLogger(Noun.class);

    private Declension declension;
    private Gender gender;

    public Noun()
    {
        super(Types.Noun);
    }

    public Noun(Declension declension, Gender gender)
    {
        super(Types.Noun);
        checkThat(declension, gender).are(notNull());

        this.declension = declension;
        this.gender = gender;
    }

    @Override
    public JsonObject asJSON()
    {
        JsonObject object = super.asJSON();

        if (gender != null)
        {
            String genderString = gender.toString();
            object.addProperty(Keys.GENDER, genderString);
        }

        if (declension != null)
        {
            String declensionString = declension.toString();
            object.addProperty(Keys.DECLENSION, declensionString);
        }

        return object;
    }

    public static Noun fromJSON(JsonObject object)
    {
        checkThat(object).is(notNull());

        try
        {
            String genderString = object.get(Keys.GENDER).getAsString();
            String declensionString = object.get(Keys.DECLENSION).getAsString();

            Gender gender = Gender.fromString(genderString);
            Declension declension = Declension.fromString(declensionString);

            return new Noun(declension, gender);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to load Noun from JSON: {}", object, ex);
            return new Noun();
        }
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

    private static class Keys
    {

        static final String GENDER = "gender";
        static final String DECLENSION = "declension";
    }

}
