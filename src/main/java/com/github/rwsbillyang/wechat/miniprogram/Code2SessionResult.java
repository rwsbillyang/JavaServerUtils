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

import lombok.Getter;
import lombok.Setter;

/**
 * 微信小程序登录后，后端凭着临时code换取用户openId等信息
 * https://developers.weixin.qq.com/miniprogram/dev/api/code2Session.html
 * */
@Getter
@Setter
public class Code2SessionResult {
	public String openid;
	public String session_key;
	public String unionid;
	public Integer errcode;
	public String errmsg;
	
	public final static int ERR_OK = 0;//请求成功
	public final static int ERR_BUSY = -1;//系统繁忙，此时请开发者稍候再试
	public final static int ERR_INVALID_CODE = 40029;//code 无效
	public final static int ERR_FREQUENCY_LIMIT = 45011;//	频率限制，每个用户每分钟100次;
}
