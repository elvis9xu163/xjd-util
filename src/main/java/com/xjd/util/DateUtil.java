package com.xjd.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class DateUtil {

	protected static ThreadLocal<Map<String, SimpleDateFormat>> formatLocal = new ThreadLocal<Map<String, SimpleDateFormat>>();

	/**
	 * <pre>
	 * 格式化时间为字符串
	 * </pre>
	 * @param date
	 * @param format
	 * @return
	 * @see java.text.SimpleDateFormat
	 */
	public static String format(Date date, String format) {
		return getSimpleDateFormat(format).format(date);
	}

	/**
	 * <pre>
	 * 将字符串转换成时间
	 * </pre>
	 * @param dateStr
	 * @param format
	 * @return
	 * @throws ParseException
	 * @see java.text.SimpleDateFormat
	 */
	public static Date parse(String dateStr, String format) throws ParseException {
		return getSimpleDateFormat(format).parse(dateStr);
	}

	/**
	 * <pre>
	 * 获取线程安全的SimpleDateFormat
	 * </pre>
	 * @param formatStr
	 * @return
	 */
	protected static SimpleDateFormat getSimpleDateFormat(String formatStr) {
		Map<String, SimpleDateFormat> formatMap = formatLocal.get();
		if (formatMap == null) {
			formatMap = new HashMap<String, SimpleDateFormat>();
			formatLocal.set(formatMap);
		}

		SimpleDateFormat format = formatMap.get(formatStr);
		if (format == null) {
			format = new SimpleDateFormat(formatStr);
			formatMap.put(formatStr, format);
		}
		return format;
	}
}
