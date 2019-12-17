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

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 实体对象Json序列化时，将ObjectId转换成String
 * Usage: 
 * @JsonSerialize(using=ObjectIdJacksonSerializer.class)
 * 
 * https://www.jianshu.com/p/a0fb6559f56d
 * */
public class ObjectId2StringSerializer  extends JsonSerializer<ObjectId>{

	@Override
	public void serialize(ObjectId value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value == null) {
			gen.writeNull();
		} else {
			gen.writeString(value.toString());
		}
	}
}
