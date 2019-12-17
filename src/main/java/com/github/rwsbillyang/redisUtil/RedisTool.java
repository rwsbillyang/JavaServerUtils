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

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;




public class RedisTool {

	private static Logger log = LoggerFactory.getLogger(RedisTool.class);

	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";
	private static final Long RELEASE_SUCCESS = 1L;


	/**
	 * http://blog.jobbole.com/99751/ 尝试获取分布式锁
	 * 
	 * @param lockName
	 * @param expireTime 超期时间,milliseconds 若expireTime<=0，则不过期
	 * @return 成功获取锁返回锁标识符，该标识符用于释放锁；失败返回null
	 */
	public static String tryGetDistributedLock(RedisConnectionFactory redisConnectionFactory, String lockName,
			int expirationMilliseconds) {
		if (StringUtils.isBlank(lockName))
			return null;
		String requestId = UUID.randomUUID().toString();
		
		RedisConnection redisConnection = (RedisConnection) redisConnectionFactory.getConnection();  
		//Jedis jedis = jedisConnection.getNativeConnection();  

		// Starting with Redis 2.6.12 SET supports a set of options that modify its
		// behavior:
		// EX seconds -- Set the specified expire time, in seconds.
		// PX milliseconds -- Set the specified expire time, in milliseconds.
		// NX -- Only set the key if it does not already exist.
		// XX -- Only set the key if it already exist
		// http://static.javadoc.io/redis.clients/jedis/2.9.0/index.html?index-all.html
		// https://redis.io/commands/set

//		String ret = null;
//		// 不能先set再设置ttl，否则执行到二者中间时挂了，锁永远不能超时
//		if (LOCK_SUCCESS.equals(lettuce.set(lockName, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expirationMilliseconds))) {
//			ret = requestId;
//		}
		Expiration expiration = null;
		if(expirationMilliseconds<=0)
		{
			expiration = Expiration.persistent();
		}else {
			expiration = Expiration.milliseconds(expirationMilliseconds);
		}
		Boolean ret = redisConnection.set(lockName.getBytes(), requestId.getBytes(),
				expiration, SetOption.ifAbsent());
		
		redisConnection.close();
		if(ret!=null && ret==true)
			return requestId;
		else
			return null;
	}

	/**
	 * 释放分布式锁
	 * 
	 * @param lockName
	 * @param requestId 请求标识
	 * @return 是否释放成功
	 */
	public static boolean releaseDistributedLock(RedisConnectionFactory redisConnectionFactory, String lockName, String requestId) {
		if (StringUtils.isBlank(lockName))
			return false;
		RedisConnection redisConnection = (RedisConnection) redisConnectionFactory.getConnection();  
//		Jedis jedis = jedisConnection.getNativeConnection();  
//		boolean ret = false;
//		// EVAL script numkeys key [key ...] arg [arg ...]
		//EVAL的第一个参数是一段 Lua 5.1 脚本程序。 这段Lua脚本不需要（也不应该）定义函数。它运行在 Redis 服务器中。
		//EVAL的第二个参数是参数的个数，后面的参数（从第三个参数），表示在脚本中所用到的那些 Redis 键(key)，这些键名参数可以在 Lua 中通过全局变量 KEYS 数组，用 1 为基址的形式访问( KEYS[1] ， KEYS[2] ，以此类推)。
		//在命令的最后，那些不是键名参数的附加参数 arg [arg …] ，可以在 Lua 中通过全局变量 ARGV 数组访问，访问的形式和 KEYS 变量类似( ARGV[1] 、 ARGV[2] ，诸如此类)
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//		if (RELEASE_SUCCESS.equals(jedis.eval(script, Collections.singletonList(lockName), Collections.singletonList(requestId)))) {
//			ret = true;
//		}
//		redisConnection.close();
//		return ret;
		
		Integer ret = redisConnection.eval(script.getBytes(), ReturnType.INTEGER, 1, lockName.getBytes(),requestId.getBytes());
		if(ret!=null && ret ==1)
			return true;
		else 
			return false;
	}

	/**
	 * 清除某个前缀作为开头的所有键值对
	 */
	public static void deleteKeysByPrefix(RedisTemplate<String, String> redisTemplate, String prefix) {
		if (StringUtils.isBlank(prefix)) {
			log.warn("prefix is null,ignore");
			return;
		}
		deleteKeysByPattern(redisTemplate, prefix + "*");
	}

	public static void deleteKeysIfContains(RedisTemplate<String, String> redisTemplate, String str) {
		if (StringUtils.isBlank(str)) {
			log.warn("deleteKeysIfContains: str is null,ignore");
			return;
		}
		deleteKeysByPattern(redisTemplate, "*" + str + "*");
	}

	// EVAL "local keys = redis.call('keys', ARGV[1]) \n for i=1,#keys,5000 do \n
	// redis.call('del', unpack(keys, i, math.min(i+4999, #keys))) \n end \n return
	// keys" 0 prefix:* –
	public static void deleteKeysByPattern(RedisTemplate<String, String> redisTemplate, String pattern) {

		Set<String> set = redisTemplate.keys(pattern);
		if (set == null || set.size() == 0) {
			log.info("no keys for " + pattern + ", set=" + set);
		} else {
			log.info(" keys for " + pattern + " size=" + set.size());
			redisTemplate.delete(set);
		}
	}

	/**
	 * @deprecated 删除无效
	 */
	public static void deleteKeyValuesByPrefixInPipeline(RedisTemplate<String, String> redisTemplate, final String prefix) {
		log.info("deleteKeyValuesByPrefix,prefix=" + prefix);

		final byte[] rawkey = (prefix + "*").getBytes();

		// pipeline中删除所有缓存站点
		redisTemplate.execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				// StringRedisConnection stringRedisConn = (StringRedisConnection) connection;
				connection.openPipeline();
				// Collection<String> collection = stringRedisConn.keys(prefix + "*");
				// Long count = connection.del(collection.toArray(new
				// String[collection.size()]));

				Collection<byte[]> collection = connection.keys(rawkey);
				if (collection == null || collection.size() == 0) {
					log.info("no keys for " + prefix + "*" + ", collection=" + collection);
				} else {
					byte[][] bytesArray = (byte[][]) collection.toArray();
					Long count = connection.del(bytesArray);
					log.info("delKeysNumber=" + count + ", keys for " + prefix + "*");
				}

				connection.closePipeline();
				return null;
			}
		}, false, true);

		// redisTemplate.delete(redisTemplate.keys(prefix+"*"));
	}
	

}
