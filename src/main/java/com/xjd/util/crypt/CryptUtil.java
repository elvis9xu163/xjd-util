package com.xjd.util.crypt;

/**
 * <pre>
 * 综合工具类, 集成Coder, Cipher, Digest的常用操作
 * </pre>
 * 
 * @author elvis.xu
 * @since 2013-11-19
 */
public abstract class CryptUtil {

	/**
	 * <pre>
	 * 使用编码算法进行编码
	 * </pre>
	 * 
	 * @param codeAlgorithm
	 * @param data
	 * @return
	 * @author elvis.xu
	 * @since 2013-11-19
	 */
	public static byte[] encode(String codeAlgorithm, byte[] data) {
		return CoderUtil.encode(codeAlgorithm, data);
	}

	/**
	 * <pre>
	 * 使用编码算法进行解码
	 * </pre>
	 * 
	 * @param codeAlgorithm
	 * @param data
	 * @return
	 * @author elvis.xu
	 * @since 2013-11-19
	 */
	public static byte[] decode(String codeAlgorithm, byte[] data) {
		return CoderUtil.decode(codeAlgorithm, data);
	}

	/**
	 * <pre>
	 * 使用加密算法进行加密
	 * </pre>
	 * 
	 * @param cipherAlgorithm
	 * @param key
	 * @param data
	 * @return
	 * @author elvis.xu
	 * @since 2013-11-19
	 */
	public static byte[] encrypt(String cipherAlgorithm, byte[] key, byte[] data) {
		return CipherUtil.encrypt(cipherAlgorithm, key, data);
	}

	/**
	 * <pre>
	 * 使用加密算法进行解密
	 * </pre>
	 * 
	 * @param cipherAlgorithm
	 * @param key
	 * @param data
	 * @return
	 * @author elvis.xu
	 * @since 2013-11-19
	 */
	public static byte[] decrypt(String cipherAlgorithm, byte[] key, byte[] data) {
		return CipherUtil.decrypt(cipherAlgorithm, key, data);
	}

	/**
	 * <pre>
	 * 生成加密算法的密码
	 * </pre>
	 * 
	 * @param cipherAlgorithm
	 * @param seed
	 * @return
	 * @author elvis.xu
	 * @since 2013-11-19
	 */
	public static byte[] genKey(String cipherAlgorithm, byte[] seed) {
		return CipherUtil.genKey(cipherAlgorithm, seed);
	}

	/**
	 * <pre>
	 * 使用摘要算法计算摘要
	 * </pre>
	 * 
	 * @param digestAlgorithm
	 * @param data
	 * @return
	 * @author elvis.xu
	 * @since 2013-11-19
	 */
	public static byte[] digest(String digestAlgorithm, byte[] data) {
		return DigestUtil.digest(digestAlgorithm, data);
	}
}
