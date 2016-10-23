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
import tech.redroma.lexis.service.words.DictionaryCodes.Age;
import tech.redroma.lexis.service.words.DictionaryCodes.Frequency;
import tech.redroma.lexis.service.words.DictionaryCodes.GeographicalArea;
import tech.redroma.lexis.service.words.DictionaryCodes.Source;
import tech.redroma.lexis.service.words.DictionaryCodes.SubjectArea;
import tech.sirwellington.alchemy.annotations.objects.Pojo;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Pojo
class SupplementalInformation
{

    private final static Logger LOG = LoggerFactory.getLogger(SupplementalInformation.class);

    private final Age age;
    private final SubjectArea subjectArea;
    private final GeographicalArea geographicalArea;
    private final Frequency frequency;
    private final Source source;

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

}
