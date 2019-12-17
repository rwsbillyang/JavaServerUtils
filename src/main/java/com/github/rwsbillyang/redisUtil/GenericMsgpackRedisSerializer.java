/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rwsbillyang.redisUtil;

import org.msgpack.core.annotations.Nullable;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//https://blog.csdn.net/asd8510678/article/details/80278417
public class GenericMsgpackRedisSerializer implements RedisSerializer<Object> {
    static final byte[] EMPTY_ARRAY = new byte[0];
    private final ObjectMapper mapper;


    public GenericMsgpackRedisSerializer(ObjectMapper mapper) {
      this.mapper = mapper;
    }

    //@Override
    public byte[] serialize(@Nullable Object source) throws SerializationException {
        if (source == null) {
            return EMPTY_ARRAY;
        } else {
            try {
                return this.mapper.writeValueAsBytes(source);
            } catch (JsonProcessingException var3) {
                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
            }
        }
    }

    //@Override
    public Object deserialize(@Nullable byte[] source) throws SerializationException {
        return this.deserialize(source, Object.class);
    }

    @Nullable
    public <T> T deserialize(@Nullable byte[] source, Class<T> type) throws SerializationException {
        Assert.notNull(type, "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");
        if (source == null || source.length == 0) {
            return null;
        } else {
            try {
                return this.mapper.readValue(source, type);
            } catch (Exception var4) {
                throw new SerializationException("Could not read JSON: " + var4.getMessage(), var4);
            }
        }
    }

}

