package com.github.rwsbillyang.wechat.miniprogram;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaidUnionIdResult {

	public String unionid;
	public Integer errcode;
	public String errmsg;
	
	public final static int ERR_OK = 0;//请求成功
	public final static int ERR_BUSY = -1;//系统繁忙，此时请开发者稍候再试
	public final static int ERR_INVALID_OPENID = 40003;//openid 错误
	public final static int ERR_NOT_BIND = 89002;//	没有绑定开放平台帐号
	public final static int ERR_INVALID_TRANSACTION = 89300;//	订单无效
}
