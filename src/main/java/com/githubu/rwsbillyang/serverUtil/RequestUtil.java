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

package com.githubu.rwsbillyang.serverUtil;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.github.rwsbillyang.clearUtils.BrowserCheckUtil;

public class RequestUtil {
	public static boolean isMicroMessenger(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		if(ua == null) return false;
		return BrowserCheckUtil.isMicroMessenger(ua.toLowerCase());
	}
	public static boolean isMobile(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		if(ua == null) return false;
		return BrowserCheckUtil.isMobile(ua.toLowerCase());
	}
	/**
     * 0: PC browser
     * 1: mobile
     * 2: weixin
     * -1: null user-agent
     * */
	public static int getBrowserType(HttpServletRequest request) {
		String ua = request.getHeader("User-Agent");
		if(ua == null) return -1;
		return BrowserCheckUtil.getBrowserType(ua.toLowerCase());
	}
	
	/**
	 * 从HttpServletRequest中提取请求url，包括参数
	 * */
	public static String getUrlFromRequestWithQueryParameter(HttpServletRequest request) {
		StringBuffer buf = request.getRequestURL();
		String queryString = request.getQueryString();
		if (!StringUtils.isBlank(queryString))
			buf.append("?" + request.getQueryString());

		return buf.toString();
	}
	public static String getUriFromRequestWithQueryParameter(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer(request.getRequestURI());
		String queryString = request.getQueryString();
		if (!StringUtils.isBlank(queryString))
			buf.append("?" + request.getQueryString());

		return buf.toString();
	}
	
	/**
	 * 获取到客户端IP地址
	 * 
	 * @param request
	 * @return IP地址字符串
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = null;

		ip = request.getHeader("Cdn-Src-Ip");
		if (ip != null && !"".equals(ip)) {
			return ip;
		}

		ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.indexOf(',') > 0) {
			String[] tmp = ip.split("[,]");
			for (int i = 0; tmp != null && i < tmp.length; i++) {
				if (tmp[i] != null && tmp[i].length() > 0 && !"unknown".equalsIgnoreCase(tmp[i])) {
					ip = tmp[i];
					break;
				}
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
