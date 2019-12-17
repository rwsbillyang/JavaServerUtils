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

import java.io.IOException;

import org.msgpack.core.annotations.Nullable;
import org.springframework.cache.support.NullValue;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class NullValueSerializer  extends StdSerializer<NullValue> {
	 private static final long serialVersionUID = 2199052150128658111L;
     private final String classIdentifier;

     public NullValueSerializer(@Nullable String classIdentifier) {
         super(NullValue.class);
         this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
     }

     @Override
     public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
         jgen.writeStartObject();
         jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
         jgen.writeEndObject();
     }
}
