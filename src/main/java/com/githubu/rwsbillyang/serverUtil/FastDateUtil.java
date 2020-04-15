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


import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 日期时间工具类
 * 
 * @author sunflower
 * 
 */
public class FastDateUtil {

	private static final FastDateFormat datetimeFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	private static final FastDateFormat datetimeFormat2 = FastDateFormat.getInstance("yyyyMMddHHmmss");
	
	private static final FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
	private static final FastDateFormat timeFormat = FastDateFormat.getInstance("HH:mm:ss");

	private static final FastDateFormat datetimeFormatWithWeek = FastDateFormat.getInstance("E MM-dd",Locale.CHINA);
	
	/**
	 * 带星期几的 日期
	 * */
	public static String formatDateWithWeek(Timestamp time) {
		if (time == null)
			return null;
		return datetimeFormatWithWeek.format(time);
	}
	/**
	 * 格式化日期时间
	 * 
	 * @return
	 */
	public static String formatDatetime(Date date, String pattern) {
		return FastDateFormat.getInstance(pattern).format(date);
	}
	public static Date parseDatetime(String dateStr, String pattern) {
		try {
			return FastDateFormat.getInstance(pattern).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 格式化日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatDatetime(Date date) {
		return datetimeFormat.format(date);
	}
	/**
	 * 格式化日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatTimestamp(Timestamp timestamp,String defaultValue) {
		if(timestamp==null)
			return defaultValue;
		return datetimeFormat.format(timestamp);
	}
	public static String formatTimestamp(Timestamp timestamp,String defaultValue, String pattern) {
		if(timestamp==null) return defaultValue;
		return FastDateFormat.getInstance(pattern).format(timestamp);
	}
	public static String formatDateTime(Long time) {
		if(time==null)
			return null;
		return datetimeFormat.format(time.longValue());
	}
	public static String formatDateTime(Long time, String pattern) {
		if(time==null)
			return null;
		return FastDateFormat.getInstance(pattern).format(time.longValue());
	}

	/**
	 * 格式化日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String formatDate(Date date) {
		if(date==null) return null;
		return dateFormat.format(date);
	}


	/**
	 * 格式化时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}
	
	
	/**
	 * 将字符串日期时间转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetime(String datetime)  {
		if(StringUtils.isBlank(datetime))
			return null;
		try {
			return datetimeFormat.parse(datetime);
		}catch(ParseException e) {
			System.err.println("ParseException:date="+datetime);
		}
		return null;
	}
	public static Long parseDatetimeToLong(String datetime)  {
		if(StringUtils.isBlank(datetime))
			return null;
		try {
			return datetimeFormat.parse(datetime).toInstant().toEpochMilli();
		}catch(ParseException e) {
			System.err.println("ParseException:date="+datetime);
		}
		return null;
	}
	
	public static java.sql.Date parseSqlDate(String datetime)  {
		if(StringUtils.isBlank(datetime))
			return null;
		try {
			return new java.sql.Date(datetimeFormat.parse(datetime).getTime()); 
		}catch(ParseException e) {
			System.err.println("ParseException:date="+datetime);
		}
		return null;
	}
	public static Timestamp parseForTimestamp(String datetime) throws ParseException {
		if(StringUtils.isBlank(datetime))
			return null;
		
		return new Timestamp(datetimeFormat.parse(datetime).getTime());
	}
	/**
	 * 将字符串日期转换成java.util.Date类型
	 *<p>
	 * 日期时间格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) {
		if(StringUtils.isBlank(date))
			return null;
		try {
			return dateFormat.parse(date);
		}catch(ParseException e) {
			System.err.println("ParseException:date="+date);
		}
		return null;
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 *<p>
	 * 时间格式 HH:mm:ss
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String time) throws ParseException {
		return timeFormat.parse(time);
	}
		


	/**
	 * 获得当前日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentDatetime() {
		return datetimeFormat.format(now());
	}
	/**
	 * 获得当前日期时间
	 * <p>
	 * 日期时间格式yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public static String currentDatetime2() {
		return datetimeFormat2.format(now());
	}

	/**
	 * 获得当前日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String currentDate() {
		return dateFormat.format(now());
	}
	/**
	 * 获得当前时间的<code>java.util.Date</code>对象
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}
	
	/**
	 * 获得当前时间的毫秒数
	 * <p>
	 * 详见{@link System#currentTimeMillis()}
	 * 
	 * @return
	 */
	public static long millis() {
		return System.currentTimeMillis();
	}
	/**
	 * 获得当前时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentTime() {
		return timeFormat.format(now());
	}
	
	
	
	//====================================日期比较====================================//
	
	/**
	 *判断原日期是否在目标日期之前
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isBefore(Date src, Date dst) {
		return src.before(dst);
	}

	/**
	 *判断原日期是否在目标日期之后
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isAfter(Date src, Date dst) {
		return src.after(dst);
	}

	/**
	 *判断两日期是否相同
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqual(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	/**
	 * 判断某个日期是否在某个日期范围
	 * 
	 * @param beginDate
	 *            日期范围开始
	 * @param endDate
	 *            日期范围结束
	 * @param src
	 *            需要判断的日期
	 * @return
	 */
	public static boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}
	
	//====================================日期比较====================================//
	
	
	/**
	 * 与当前时间对比，若当前时间为空，则返回-1；若已过期则返回-2；若未过期，则返回0
	 * 若旧截止日期为空或已过时，则在当前时间上增加
	 * */
	public static int isEndDateTimeout(Timestamp endDate)
	{
		if(endDate==null)
			return -1;
		else
		{
			if(endDate.compareTo(new Date())>0)//endDate after now
				return 0;
			else
				return -2;
		}
	}
	
	/**
	 * 在旧的时间上增加新的月数量
	 * 若旧截止日期为空或已过时，则在当前时间上增加
	 * */
	public static Timestamp addMonthAndDay(Timestamp oldEndTime,int month,int day)
	{
		Date oldDate;	
		Date now = new Date();

		int ret=isEndDateTimeout(oldEndTime);
		if(ret<0)
			oldDate=now;
		else
		{
			oldDate=new Date(oldEndTime.getTime());
		}
		Date newDate = DateUtils.addMonths(oldDate, month);
		newDate = DateUtils.addDays(newDate, day);
		return new Timestamp(newDate.getTime());
		/*
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oldDate);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		Timestamp newDateTime=new Timestamp(calendar.getTimeInMillis());
		return 	newDateTime;
		*/
	}
	public static Timestamp addMonthAndDayBaseNow(int month,int day)
	{
		Date oldDate = new Date();

		Date newDate = DateUtils.addMonths(oldDate, month);
		newDate = DateUtils.addDays(newDate, day);
		return new Timestamp(newDate.getTime());
		
	}

	/**
	 * 计算当前时间的前days天的毫秒起始值. [start,end)
	 * based on java8 time package
	 * */
	public static long getStartMilliSeconds(int month, int days)
	{
		return LocalDate.now().minusMonths(month).minusDays(days).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	/**
	 * 计算当前日期的起始毫秒数。不要包括当前秒:[start,end)
	 * */
	public static long getEndMilliSeconds()
	{
		return LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
}

