package com.xjd.util.crypt;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <pre>
 * 对称加解密工具类
 * </pre>
 * 
 * @author elvis.xu
 * @date 2013-11-18
 */
public abstract class CipherUtil {
	public static final String DES = "DES";
	public static final String DESede = "DESede";
	public static final String AES = "AES";
	public static final String Blowfish = "Blowfish";
	public static final String RC2 = "RC2";
	public static final String RC4 = "RC4";

	protected static Key toKey(String algorithm, byte[] key) {
		SecretKey secretKey = new SecretKeySpec(key, algorithm);
		return secretKey;
	}

	public static byte[] encrypt(String algorithm, byte[] key, byte[] data) {
		try {
			Key k = toKey(algorithm, key);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, k);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new CryptException(e);
		}
	}

	public static byte[] decrypt(String algorithm, byte[] key, byte[] data) {
		try {
			Key k = toKey(algorithm, key);
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, k);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new CryptException(e);
		}
	}

	public static byte[] genKey(String algorithm, byte[] seed) {
		SecureRandom secureRandom = null;

		if (seed != null) {
			secureRandom = new SecureRandom(seed);
		} else {
			secureRandom = new SecureRandom();
		}

		try {
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			kg.init(secureRandom);

			return kg.generateKey().getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw new CryptException(e);
		}
	}
}
