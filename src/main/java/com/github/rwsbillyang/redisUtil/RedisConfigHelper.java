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

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class RedisConfigHelper {
	
	/***
	 * https://github.com/FasterXML/jackson-databind/wiki/Deserialization-Features
	 */
	public static ObjectMapper getObjectMapper(boolean useMsgPackSerializer)
	{
		ObjectMapper mapper = null;
		if(useMsgPackSerializer)
		{
			mapper = new ObjectMapper(new MessagePackFactory());
		}else
		{
			mapper = new ObjectMapper();
		}
		mapper.registerModule((new SimpleModule()).addSerializer(new NullValueSerializer(null)));
		
//		SimpleModule mod = new SimpleModule("ObjectId", new Version(1, 0, 0, null, null, null));
//		mod.addDeserializer(ObjectId.class, new ObjectIdStringDeserializer());
//		mod.addSerializer(ObjectId.class, new ObjectId2StringSerializer());
//		mapper.registerModule(mod);
		
		//http://www.leadroyal.cn/?p=594
		//http://www.cnblogs.com/lknny/p/5757784.html
		
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL,As.PROPERTY);
		//mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		//mapper.enableDefaultTyping();
		
		
		//mapper.configure(MapperFeature.USE_ANNOTATIONS, true);
		
		//spring.jackson.mapper.propagate_transient_marker=true
		mapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);
		
		
		//在反序列化时忽略在 json 中存在但 Java 对象不存在的属性 
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false); 
		
		
		//为空的参数不序列化，如""、null、没有内容的new HashMap()等都算。Integer的0不算空。
		//spring.jackson.default-property-inclusion=non_empty
		mapper.setSerializationInclusion(Include.NON_EMPTY); 
		
		//忽略值为默认值的属性 
		//mapper.setDefaultPropertyInclusion(Include.NON_DEFAULT);
		//mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY); 
		
		// 所有日期格式都统一为以下样式
       // mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		//mapper.configure(com.fasterxml.jackson.databind.MapperFeature.AUTO_DETECT_FIELDS, true);//default is true
		
        return mapper;
	}
	
	/**
	 * 若参数useFastJsonSerializer为true的话，将使用FastJson序列化；
	 * 若为false的话，则检查参数useMsgPackSerializer是否为true，
	 * 若为true则使用MsgPack，若为false则使用Jackson进行Json序列化
	 * */
	public static RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory,boolean useFastJsonSerializer,boolean useMsgPackSerializer) {
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		RedisSerializer redisSerializer = redisSerializer(useFastJsonSerializer, useMsgPackSerializer);

		StringRedisSerializer keySerializer = new StringRedisSerializer();

		redisTemplate.setKeySerializer(keySerializer);// 单独设置keySerializer
		redisTemplate.setValueSerializer(redisSerializer);// 单独设置valueSerializer

		redisTemplate.setHashKeySerializer(keySerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		
		redisTemplate.afterPropertiesSet();
		
		return redisTemplate;
	}
	
	public static RedisSerializer redisSerializer(boolean useFastJsonSerializer,boolean useMsgPackSerializer)
	{
		RedisSerializer redisSerializer = null;
		if (useFastJsonSerializer) {
			ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
			redisSerializer = new GenericFastJsonRedisSerializer();
		} else {
			ObjectMapper mapper =  getObjectMapper(useMsgPackSerializer);
			
			GenericJackson2JsonRedisSerializer jackson2Serializer = new GenericJackson2JsonRedisSerializer(mapper);
			
			//Jackson2JsonRedisSerializer jackson2Serializer = new Jackson2JsonRedisSerializer(Object.class);
			//jackson2Serializer.setObjectMapper(mapper);

			if (useMsgPackSerializer) {
				redisSerializer = new GenericMsgpackRedisSerializer(mapper);
			} else {
				redisSerializer = jackson2Serializer;
			}
		}
		return redisSerializer;
	}
}
