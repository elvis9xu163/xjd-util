package com.xjd.util.crypt;

import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.GOST3411Digest;
import org.bouncycastle.crypto.digests.MD2Digest;
import org.bouncycastle.crypto.digests.MD4Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD128Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.RIPEMD256Digest;
import org.bouncycastle.crypto.digests.RIPEMD320Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA224Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA384Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.TigerDigest;
import org.bouncycastle.crypto.digests.WhirlpoolDigest;

/**
 * <pre>
 * 摘要工具类
 * </pre>
 * 
 * @author elvis.xu
 * @date 2013-11-18
 */
public abstract class DigestUtil {
	public static final String MD2 = "MD2";
	public static final String MD4 = "MD4";
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
	public static final String SHA224 = "SHA-224";
	public static final String SHA256 = "SHA-256";
	public static final String SHA384 = "SHA-384";
	public static final String SHA512 = "SHA-512";
	public static final String RIPEMD128 = "RIPEMD128";
	public static final String RIPEMD160 = "RIPEMD160";
	public static final String RIPEMD256 = "RIPEMD256";
	public static final String RIPEMD320 = "RIPEMD320";
	public static final String GOST3411 = "GOST3411";
	public static final String TIGER = "Tiger";
	public static final String WHIRLPOOL = "Whirlpool";

	protected static Map<String, Class<? extends Digest>> digestMap = new HashMap<String, Class<? extends Digest>>();
	static {
		digestMap.put(MD2, MD2Digest.class);
		digestMap.put(MD4, MD4Digest.class);
		digestMap.put(MD5, MD5Digest.class);
		digestMap.put(SHA1, SHA1Digest.class);
		digestMap.put(SHA224, SHA224Digest.class);
		digestMap.put(SHA256, SHA256Digest.class);
		digestMap.put(SHA384, SHA384Digest.class);
		digestMap.put(SHA512, SHA512Digest.class);
		digestMap.put(RIPEMD128, RIPEMD128Digest.class);
		digestMap.put(RIPEMD160, RIPEMD160Digest.class);
		digestMap.put(RIPEMD256, RIPEMD256Digest.class);
		digestMap.put(RIPEMD320, RIPEMD320Digest.class);
		digestMap.put(GOST3411, GOST3411Digest.class);
		digestMap.put(TIGER, TigerDigest.class);
		digestMap.put(WHIRLPOOL, WhirlpoolDigest.class);
	}
	
	protected static Digest getDigest(String algorithm) {
		Class<? extends Digest> clazz = digestMap.get(algorithm);
		if (clazz == null) {
			throw new CryptException(String.format("The algorithm '%s' is not support.", algorithm));
		}
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new CryptException(e);
		}
	}

	public static byte[] digest(String algorithm, byte[] data) {
		Digest digest = getDigest(algorithm);
		digest.update(data, 0, data.length);
		byte[] buf = new byte[digest.getDigestSize()];
		digest.doFinal(buf, 0);
		return buf;
	}
	
}
