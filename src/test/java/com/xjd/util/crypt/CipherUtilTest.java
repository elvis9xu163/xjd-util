package com.xjd.util.crypt;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CipherUtilTest {
	private static Logger log = LoggerFactory.getLogger(CipherUtilTest.class);

	@Test
	public void test() throws UnsupportedEncodingException {
		String data = "李又又江同志号称“将军”？若他敢上钓鱼岛上高歌一曲！可以考虑给“银枪小霸王”减刑5年，否则请自去“将军”头衔，不要在这里丢人现眼！！！同意的请顶一个！";
		test(CipherUtil.DES, data);
		test(CipherUtil.DESede, data);
		test(CipherUtil.AES, data);
		test(CipherUtil.Blowfish, data);
		test(CipherUtil.RC2, data);
		test(CipherUtil.RC4, data);
	}
	
	public void test(String algorithm, String data) throws UnsupportedEncodingException {
		log.debug("开始测试算法...: algorithm='{}'", algorithm);
		log.debug("加密前的数据: [{}]", data);
		
		byte[] key = CipherUtil.genKey(algorithm, null);
		log.debug("生成的KEY: [{}]", new String(key, "UTF-8"));
		
		byte[] enData = CipherUtil.encrypt(algorithm, key, data.getBytes("UTF-8"));
		log.debug("加密后的数据: [{}]", new String(enData, "UTF-8"));
		
		byte[] deData = CipherUtil.decrypt(algorithm, key, enData);
		log.debug("解密后的数据: [{}]", new String(deData, "UTF-8"));
		
		Assert.assertEquals(data, new String(deData, "UTF-8"));
		log.debug("");
	}
	
	@Test
	public void testExce() {
		try {
			CipherUtil.genKey("NONE", null);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof CryptException);
		}
		
		try {
			CipherUtil.encrypt("NONE", CipherUtil.genKey(CipherUtil.DES, null), null);
		} catch (Exception e) {
			Assert.assertTrue(e instanceof CryptException);
		}
	}

}
