package com.github.rwsbillyang.wechat.miniprogram;

public class AccessTokenResult {
	public String access_token;
	public Integer expires_in;
	public Integer errcode;
	public String errmsg;
	
	public final static int ERR_OK = 0;//请求成功
	public final static int ERR_BUSY = -1;//系统繁忙，此时请开发者稍候再试
	public final static int ERR_INVALID_SECRET = 40001;//ppSecret 错误或者 AppSecret 不属于这个小程序，请开发者确认 AppSecret 的正确性
	public final static int ERR_INVALID_GRANTTYPE = 40002;//	40002	请确保 grant_type 字段值为 client_credential
	public final static int ERR_INVALID_APPID = 40013;//不合法的 AppID，请开发者检查 AppID 的正确性，避免异常字符，注意大小写
}
