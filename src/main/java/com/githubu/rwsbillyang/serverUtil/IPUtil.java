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

import java.util.HashMap;
import java.util.Map;



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.rwsbillyang.clearUtils.HttpUtil;



/**
 * @date 2014-7-14
 */
public class IPUtil {

	private static Logger log = LoggerFactory.getLogger(IPUtil.class);



	/**
	 * 访问淘宝接口解析IP所属城市
	 * 
	 * @param Ip
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> findCityByIp(String Ip) {
		String url = "http://ip.taobao.com/service/getIpInfo.php";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", Ip);
			String result = HttpUtil.sendGet(url, params);
			if(result==null) return null;
			
			Map<String, Object> map = JsonUtil.fromJson(decodeUnicode(result),
					new TypeReference<Map<String, Object>>() {
					});
			int code = (Integer) map.get("code");
			if (0 == code) {
				Map<String, String> data = (Map<String, String>) map.get("data");
				return data;
			}
		} catch (Exception e) {
			log.error("IP解析出错：" + e.getMessage());
			return null;
		}
		return null;
	}

	/**
	 * 解析IP获取城市
	 * 
	 * @param request
	 * @return
	 */
	public static String findProvinceCityByIp(String ip) {
		if(StringUtils.isBlank(ip)) return null;
		
		String url = "http://ip.taobao.com/service/getIpInfo.php";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("ip", ip);
			String result = new String(HttpUtil.sendGet(url, params));
			Map<String, Object> map = JsonUtil.fromJson(decodeUnicode(result),
					new TypeReference<Map<String, Object>>() {
					});
			int code = (Integer) map.get("code");
			if (0 == code) {
				Map<String, String> data = (Map<String, String>) map.get("data");
				String address = data.get("region") + " " + data.get("city");
				return address;
			}
		} catch (Exception e) {
			log.error("IP解析出错：" + e.getMessage());
			return null;
		}
		return null;
	}

	// 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
	public static Long ipToLong(String strIp) {
		if(strIp == null) return null;
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		if(position1 < 0)
		{
			log.warn("IP no first dot, Ip={}",strIp);
			return null;
		}
		int position2 = strIp.indexOf(".", position1 + 1);
		if(position2 < 0)
		{
			log.warn("IP no second dot, Ip={}",strIp);
			return null;
		}
		int position3 = strIp.indexOf(".", position2 + 1);
		if(position3 < 0)
		{
			log.warn("IP no third dot, Ip={}",strIp);
			return null;
		}
		try {
			// 将每个.之间的字符串转换成整型
			ip[0] = Long.parseLong(strIp.substring(0, position1).trim());
			ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2).trim());
			ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3).trim());
			ip[3] = Long.parseLong(strIp.substring(position3 + 1).trim());
			return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
		} catch (java.lang.NumberFormatException e) {
			log.warn("NumberFormatException: strIp=" + strIp);
			return null;
		}

	}

	// 将十进制整数形式转换成127.0.0.1形式的ip地址
	public static String longToIP(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 解析unicode中文
	 * 
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

}
