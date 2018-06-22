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
import tech.sirwellington.alchemy.arguments.Arguments;
import tech.sirwellington.alchemy.arguments.assertions.Assertions;

import static tech.sirwellington.alchemy.arguments.Arguments.*;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;

/**
 *
 * @author SirWellington
 */
@Pojo
public class Definition implements JSONConvertible
{

    private static final Logger LOG = LoggerFactory.getLogger(Definition.class);

    private List<String> terms;

    public Definition()
    {
        terms = Lists.create();
    }

    Definition(List<String> terms)
    {
        Arguments.checkThat(terms).is(Assertions.notNull());
        this.terms = terms;
    }

    @Override
    public JsonObject asJSON()
    {
        JsonObject object = new JsonObject();

        Supplier<JsonArray> supplier = () -> new JsonArray();
        BiConsumer<JsonArray, String> accumulator = (array, term) -> array.add(term);
        BiConsumer<JsonArray, JsonArray> combiner = (first, second) -> first.addAll(second);

        JsonArray termsArray = terms.stream().collect(supplier, accumulator, combiner);
        object.add("terms", termsArray);

        return object;
    }

    static Definition fromJSON(JsonObject object)
    {
        checkThat(object).is(notNull());

        try
        {
            JsonArray termsArray = object.getAsJsonArray("terms");
            List<String> terms = Lists.create();

            for (JsonElement element : termsArray)
            {
                if (element.isJsonPrimitive())
                {
                    terms.add(element.getAsString());
                }
            }

            return new Definition(terms);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to decode Definition from {}", object, ex);
            return new Definition(Lists.emptyList());
        }

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
