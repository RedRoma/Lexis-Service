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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.annotations.arguments.NonEmpty;

import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;

/**
 * Dictionary Codes are used to convey additional information about a Latin Word.
 * 
 * @author SirWellington
 */
@NonInstantiable
final class DictionaryCodes
{

    private final static Logger LOG = LoggerFactory.getLogger(DictionaryCodes.class);
    
    private DictionaryCodes()
    {
        
    }

    /**
     * The Age represents a Word's age and in which time period it was used.
     */
    static enum Age
    {
        X,
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H;

        static DictionaryCodes.Age fromString(@NonEmpty String string)
        {
            checkThat(string).is(nonEmptyString());

            Age age = null;

            try
            {
                age = Age.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.error("Failed to load Age from String {}", string);
            }

            if (age != null)
            {
                return age;
            }

            LOG.error("Failed to load Age from String {}", string);
            return Age.X;
        }
    }

    /**
     * The Subject Area represents the discipline in which the word is used, for example, in science, or arts.
     */
    static enum SubjectArea
    {
        X,
        A,
        B,
        D,
        E,
        G,
        L,
        P,
        S,
        T,
        W,
        Y;

        static SubjectArea fromString(@NonEmpty String string)
        {
            checkThat(string).is(nonEmptyString());

            SubjectArea area = null;

            try
            {
                area = SubjectArea.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load SubjectArea from String {}", string);
            }

            if (area == null)
            {
                area = SubjectArea.X;
            }

            return area;

        }
    }

    /**
     * Geographical Area represents a location where the word was commonly used.
     */
    static enum GeographicalArea
    {
        X,
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
        K,
        N,
        P,
        Q,
        R,
        S,
        U;

        static GeographicalArea fromString(@NonEmpty String string)
        {
            checkThat(string).is(nonEmptyString());

            GeographicalArea area = null;

            try
            {
                area = GeographicalArea.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load GeographicalArea from String {}", string);
            }

            if (area == null)
            {
                area = GeographicalArea.X;
            }

            return area;
        }
    }

    /**
     * Frequency is an indication of the relative frequency of a word.
     *
     * This code also applies differently to inflections. If there were several matches to an input word, this key may be used to
     * sort the output, or exclude rate interpretations.
     */
    static enum Frequency
    {
        X,
        A,
        B,
        C,
        D,
        E,
        F,
        I,
        M,
        N;

        static Frequency fromString(@NonEmpty String string)
        {
            checkThat(string).is(nonEmptyString());

            Frequency frequency = null;

            try
            {
                frequency = Frequency.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load Frequency from code {}", string, ex);
            }

            if (frequency == null)
            {
                frequency = Frequency.X;
            }

            return frequency;
        }
    }

    /**
     * The Source of the word is the original Dictionary text where the word's definition and information was procured from.
     */
    static enum Source
    {
        X,
        A,
        B,
        C,
        D,
        E,
        F,
        G,
        H,
        I,
        J,
        K,
        L,
        M,
        N,
        O,
        P,
        Q,
        R,
        S,
        T,
        U,
        V,
        W,
        Y,
        Z;

        static Source fromString(String string)
        {
            checkThat(string).is(nonEmptyString());

            Source source = null;

            try
            {
                source = Source.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load Source from Code {}", string);
            }

            if (source == null)
            {
                source = Source.X;
            }

            return source;
        }

    }

}
