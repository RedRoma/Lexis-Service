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
final class Preposition extends WordType
{
    
    private final static Logger LOG = LoggerFactory.getLogger(Preposition.class);
    
    private final CaseType caseType;
    
    public Preposition(CaseType caseType)
    {
        super(Types.Preposition);
        checkThat(caseType).is(notNull());
        
        this.caseType = caseType;
    }
    
    public static Preposition fromJSON(JsonObject object)
    {
        checkThat(object).is(notNull());
        
        try
        {
            String caseTypeString = object.get("caseType").getAsString();
            CaseType caseType = CaseType.fromString(caseTypeString);
            
            return new Preposition(caseType);
        }
        catch (Exception ex)
        {
            LOG.error("Failed to decode Preposition from {}", object, ex);
            return null;
        }
    }
    
    @Override
    public JsonObject asJSON()
    {
        JsonObject object = super.asJSON();
        
        String caseTypeString = caseType.toString();
        object.addProperty("caseType", caseTypeString);
        
        return object;
    }
    
    public CaseType getCaseType()
    {
        return caseType;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.caseType);
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
        final Preposition other = (Preposition) obj;
        if (this.caseType != other.caseType)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        return "Preposition{" + "caseType=" + caseType + '}';
    }
    
    static enum CaseType
    {
        Nominative,
        Genitive,
        Accusative,
        Dative,
        Ablative,
        Vocative,
        Locative,
        Unknown;
        
        static CaseType fromString(String string)
        {
            checkThat(string).is(nonEmptyString());
            
            CaseType caseType = null;
            
            try
            {
                caseType = CaseType.valueOf(string);
            }
            catch (IllegalArgumentException ex)
            {
                LOG.warn("Failed to load CaseType from {}", string);
            }
            
            if (caseType == null)
            {
                caseType = CaseType.Unknown;
            }
            
            return caseType;
        }
    }
    
}
