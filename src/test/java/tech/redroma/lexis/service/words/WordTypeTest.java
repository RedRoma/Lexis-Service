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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tech.sirwellington.alchemy.test.junit.runners.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 *
 * @author SirWellington
 */
@Repeat(100)
@RunWith(AlchemyTestRunner.class)
public class WordTypeTest
{

    @GeneratePojo
    private WordType instance;

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testFromJSON()
    {
        JsonObject json = instance.asJSON();
        WordType result = WordType.fromJSON(json);
        assertThat(result.getWordType(), is(instance.getWordType()));
    }

    @Test
    public void testAsJSON()
    {
        JsonObject json = instance.asJSON();
        assertThat(json, notNullValue());
    }

}