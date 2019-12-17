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

package com.github.rwsbillyang.wechat.miniprogram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rwsbillyang.clearUtils.HttpUtil;
import com.githubu.rwsbillyang.serverUtil.JsonUtil;

public class MiniProgramUtil {
	private static Logger log = LoggerFactory.getLogger(MiniProgramUtil.class);
	/**
	 * 
	 * //https://developers.weixin.qq.com/miniprogram/dev/api/code2Session.html
		//GET https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
	 * */
	private final static String URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
	public static Code2SessionResult getSessionReuslt(String appid,String secret,String code)
	{
		String url = String.format(URL, appid, secret, code);
		String json = HttpUtil.sendGet(url);
		log.info("url="+url+",result="+json);
		if(json!=null)
		{
			Code2SessionResult ret = JsonUtil.fromJson(json, Code2SessionResult.class);
			return ret;
		}
		return null;
	}
	
	 /**
	  * 用户支付完成后，获取该用户的 UnionId，无需用户授权。
	  * https://developers.weixin.qq.com/miniprogram/dev/api/getPaidUnionId.html
	  * */
	private final static String GET_PAIDUNION_URL= "https://api.weixin.qq.com/wxa/getpaidunionid?access_token=%s&openid=%s&transaction_id=%s";
	public static GetPaidUnionIdResult getPaidUnionId(String accessToken,String openId,String transactionId)
	{
		if(accessToken==null) return null;
		
		String url = String.format(GET_PAIDUNION_URL, accessToken, openId,transactionId);
		String json = HttpUtil.sendGet(url);
		log.info("url="+url+",result="+json);
		if(json!=null)
		{
			GetPaidUnionIdResult ret = JsonUtil.fromJson(json, GetPaidUnionIdResult.class);
			return ret;
		}
		return null;
	}
	
	/**
	 * https://developers.weixin.qq.com/miniprogram/dev/api/getAccessToken.html
	 * 
	 * access_token 的存储与更新
access_token 的存储至少要保留 512 个字符空间；
access_token 的有效期目前为 2 个小时，需定时刷新，重复获取将导致上次获取的 access_token 失效；
建议开发者使用中控服务器统一获取和刷新 access_token，其他业务逻辑服务器所使用的 access_token 均来自于该中控服务器，不应该各自去刷新，否则容易造成冲突，导致 access_token 覆盖而影响业务；
access_token 的有效期通过返回的 expire_in 来传达，目前是7200秒之内的值，中控服务器需要根据这个有效时间提前去刷新。在刷新过程中，中控服务器可对外继续输出的老 access_token，此时公众平台后台会保证在5分钟内，新老 access_token 都可用，这保证了第三方业务的平滑过渡；
access_token 的有效时间可能会在未来有调整，所以中控服务器不仅需要内部定时主动刷新，还需要提供被动刷新 access_token 的接口，这样便于业务服务器在API调用获知 access_token 已超时的情况下，可以触发 access_token 的刷新流程。
	 * */
	private final static String GET_ACCESSTOKEN_URL= "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	public static AccessTokenResult getAccessToken(String appId,String appSecret)
	{
		String url = String.format(GET_ACCESSTOKEN_URL, appId, appSecret);
		String json = HttpUtil.sendGet(url);
		log.info("url="+url+",result="+json);
		if(json!=null)
		{
			AccessTokenResult ret = JsonUtil.fromJson(json, AccessTokenResult.class);
			return ret;
		}
		return null;
	}
}
