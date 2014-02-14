package com.xjd.util.crypt;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
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
		
		test(data);
		
	}
	
	public void test(String algorithm, String data) throws UnsupportedEncodingException {
		log.debug("开始测试算法...: algorithm='{}'", algorithm);
		log.debug("编码前的数据: [{}]", data);
		
		byte[] enData = CoderUtil.encode(algorithm, data.getBytes("UTF-8"));
		log.debug("编码后的数据: [{}]", new String(enData, "UTF-8"));
		
		byte[] deData = CoderUtil.decode(algorithm, enData);
		log.debug("解码后的数据: [{}]", new String(deData, "UTF-8"));
		log.debug("");
		
		Assert.assertEquals(data, new String(deData, "UTF-8"));
	}
	
	public void test(String data) throws UnsupportedEncodingException {
		byte[] o = data.getBytes("UTF-8");
		
		byte[] en = CoderUtil.encode(CoderUtil.BASE64, o);
		String enStr = new String(en, "UTF-8");
		Assert.assertEquals(Base64.encodeBase64String(o), enStr);
		
		byte[] en2 = CoderUtil.encode(CoderUtil.HEX, o);
		String en2Str = new String(en2, "UTF-8");
		Assert.assertEquals(Hex.encodeHexString(o), en2Str);
	}

}
