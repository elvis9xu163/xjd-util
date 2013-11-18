package com.xjd.util.crypt;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoderUtilTest {
	private static Logger log = LoggerFactory.getLogger(CoderUtilTest.class);

	@Test
	public void test() throws UnsupportedEncodingException {
		String data = "垃圾中的垃圾, 1234567890, !@#$%^&*()-=, /?@&";
		test(CoderUtil.HEX, data);
		test(CoderUtil.BASE64, data);
		test(CoderUtil.URLBASE64, data);
	}
	
	public void test(String algorithm, String data) throws UnsupportedEncodingException {
		log.info("开始测试算法...: algorithm='{}'", algorithm);
		log.info("编码前的数据: [{}]", data);
		
		byte[] enData = CoderUtil.encode(algorithm, data.getBytes("UTF-8"));
		log.info("编码后的数据: [{}]", new String(enData, "UTF-8"));
		
		byte[] deData = CoderUtil.decode(algorithm, enData);
		log.info("解码后的数据: [{}]", new String(deData, "UTF-8"));
		log.info("");
		
		Assert.assertEquals(data, new String(deData, "UTF-8"));
	}

}
