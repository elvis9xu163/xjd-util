package com.xjd.util.crypt;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.bouncycastle.util.Arrays;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigestUtilTest {
	private static Logger log = LoggerFactory.getLogger(DigestUtilTest.class);

	@Test
	public void test() throws UnsupportedEncodingException {
		String data = "垃圾中的垃圾, 1234567890, !@#$%^&*()-=, /?@&";
		
		test(DigestUtil.MD2, data);
		test(DigestUtil.MD4, data);
		test(DigestUtil.MD5, data);
		test(DigestUtil.SHA1, data);
		test(DigestUtil.SHA224, data);
		test(DigestUtil.SHA256, data);
		test(DigestUtil.SHA384, data);
		test(DigestUtil.SHA512, data);
		test(DigestUtil.RIPEMD128, data);
		test(DigestUtil.RIPEMD160, data);
		test(DigestUtil.RIPEMD256, data);
		test(DigestUtil.RIPEMD320, data);
		test(DigestUtil.GOST3411, data);
		test(DigestUtil.TIGER, data);
		test(DigestUtil.WHIRLPOOL, data);
	}
	
	public void test(String algorithm, String data) throws UnsupportedEncodingException {
		log.debug("开始测试算法...: algorithm='{}'", algorithm);
		log.debug("编码前的数据: [{}]", data);
		log.debug("编码前的数据(Base64编码): [{}]", new String(CoderUtil.encode(CoderUtil.BASE64, data.getBytes("UTF-8")), "UTF-8"));
		
		byte[] enData = DigestUtil.digest(algorithm, data.getBytes("UTF-8"));
		log.debug("编码后的数据1: [{}]", new String(enData, "UTF-8"));
		log.debug("编码后的数据1(Base64编码): [{}]", new String(CoderUtil.encode(CoderUtil.BASE64, enData), "UTF-8"));
		
		byte[] enData2 = DigestUtil.digest(algorithm, data.getBytes("UTF-8"));
		log.debug("编码后的数据2: [{}]", new String(enData2, "UTF-8"));
		log.debug("编码后的数据2(Base64编码): [{}]", new String(CoderUtil.encode(CoderUtil.BASE64, enData2), "UTF-8"));
		
		Assert.assertTrue(Arrays.areEqual(enData, enData2));
		
		log.debug("");
	}

}
