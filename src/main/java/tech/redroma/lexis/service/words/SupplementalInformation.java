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
import tech.redroma.lexis.service.words.DictionaryCodes.*;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Pojo
public final class SupplementalInformation implements JSONConvertible
{

    private final static Logger LOG = LoggerFactory.getLogger(SupplementalInformation.class);

    private Age age;
    private SubjectArea subjectArea;
    private GeographicalArea geographicalArea;
    private Frequency frequency;
    private Source source;

    public SupplementalInformation()
    {
    }

    SupplementalInformation(Age age,
                            SubjectArea subjectArea,
                            GeographicalArea geographicalArea,
                            Frequency frequency,
                            Source source)
    {

        checkThat(age, subjectArea, geographicalArea, frequency, source)
            .are(notNull());

        this.age = age;
        this.subjectArea = subjectArea;
        this.geographicalArea = geographicalArea;
        this.frequency = frequency;
        this.source = source;
    }

    @Override
    public JsonObject asJSON()
    {
        JsonObject object = new JsonObject();

        if (age != null)
        {
            String ageString = age.toString();
            object.addProperty(Keys.AGE, ageString);
        }

        if (subjectArea != null)
        {
            String subjectAreaString = subjectArea.toString();
            object.addProperty(Keys.SUBJECT_AREA, subjectAreaString);
        }

        if (geographicalArea != null)
        {
            String geographyString = geographicalArea.toString();
            object.addProperty(Keys.GEOGRAPHICAL_AREA, geographyString);
        }

        if (frequency != null)
        {
            String frequencyString = frequency.toString();
            object.addProperty(Keys.FREQUENCY, frequencyString);
        }

        if (source != null)
        {
            String sourceString = source.toString();
            object.addProperty(Keys.SOURCE, sourceString);
        }

        return object;
    }

    static SupplementalInformation fromJSON(JsonObject object)
    {
        try
        {
            String ageString = object.get(Keys.AGE).getAsString();
            String geographicalAreaString = object.get(Keys.GEOGRAPHICAL_AREA).getAsString();
            String subjectAreaString = object.get(Keys.SUBJECT_AREA).getAsString();
            String frequencyString = object.get(Keys.FREQUENCY).getAsString();
            String sourceString = object.get(Keys.SOURCE).getAsString();

            Age age = Age.fromString(ageString);
            GeographicalArea geographicalArea = GeographicalArea.fromString(geographicalAreaString);
            SubjectArea subjectArea = SubjectArea.fromString(subjectAreaString);
            Frequency frequency = Frequency.fromString(frequencyString);
            Source source = Source.fromString(sourceString);

            return new SupplementalInformation(age, subjectArea, geographicalArea, frequency, source);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to load Supplemental Information from {}", object);
            return null;
        }
    }

    public Age getAge()
    {
        return age;
    }

    public SubjectArea getSubjectArea()
    {
        return subjectArea;
    }

    public GeographicalArea getGeographicalArea()
    {
        return geographicalArea;
    }

    public Frequency getFrequency()
    {
        return frequency;
    }

    public Source getSource()
    {
        return source;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.age);
        hash = 67 * hash + Objects.hashCode(this.subjectArea);
        hash = 67 * hash + Objects.hashCode(this.geographicalArea);
        hash = 67 * hash + Objects.hashCode(this.frequency);
        hash = 67 * hash + Objects.hashCode(this.source);
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
        final SupplementalInformation other = (SupplementalInformation) obj;
        if (this.age != other.age)
        {
            return false;
        }
        if (this.subjectArea != other.subjectArea)
        {
            return false;
        }
        if (this.geographicalArea != other.geographicalArea)
        {
            return false;
        }
        if (this.frequency != other.frequency)
        {
            return false;
        }
        if (this.source != other.source)
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "SupplementalInformation{" + "age=" + age + ", subjectArea=" + subjectArea + ", geographicalArea=" + geographicalArea + ", frequency=" + frequency + ", source=" + source + '}';
    }

    private static class Keys
    {

        static final String AGE = "age";
        static final String SUBJECT_AREA = "subject_area";
        static final String GEOGRAPHICAL_AREA = "geographical_area";
        static final String FREQUENCY = "frequency";
        static final String SOURCE = "source";
    }

}
