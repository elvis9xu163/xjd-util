package com.xjd.util.crypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.bouncycastle.util.encoders.Base64Encoder;
import org.bouncycastle.util.encoders.Encoder;
import org.bouncycastle.util.encoders.HexEncoder;
import org.bouncycastle.util.encoders.UrlBase64Encoder;


/**
 * <pre>
 * 编码工具类
 * </pre>
 * 
 * @author elvis.xu
 * @date 2013-11-18
 */
public abstract class CoderUtil {

	public static final String HEX = "HEX";
	public static final String BASE64 = "BASE64";
	public static final String URLBASE64 = "UrlBase64";
	
	public static byte[] encode(String algorithm, byte[] data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
		
		try {
			encode(algorithm, data, out);
		} catch (IOException e) {
			throw new RuntimeException("编码异常!", e);
		}
		
		return out.toByteArray();
	}
	
	public static int encode(String algorithm, byte[] data, OutputStream outputstream) throws IOException {
		return encode(getEncoder(algorithm), data, outputstream);
	}

	public static int encode(Encoder encoder, byte[] data, OutputStream outputstream) throws IOException {
		return encoder.encode(data, 0, data.length, outputstream);
	}

	public static byte[] decode(String algorithm, byte[] data) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(data.length);
		
		try {
			decode(algorithm, data, out);
		} catch (IOException e) {
			throw new RuntimeException("解码异常!", e);
		}
		
		return out.toByteArray();
	}
	
	public static int decode(String algorithm, byte[] data, OutputStream outputstream) throws IOException {
		return decode(getEncoder(algorithm), data, outputstream);
	}
	
	public static int decode(Encoder encoder, byte[] data, OutputStream outputstream) throws IOException {
		return encoder.decode(data, 0, data.length, outputstream);
	}
	
	protected static Encoder getEncoder(String algorithm) {
		Encoder encoder = null;
		if (HEX.equalsIgnoreCase(algorithm)) {
			encoder = new HexEncoder();
			
		} else if (BASE64.equalsIgnoreCase(algorithm)) {
			encoder = new Base64Encoder();
			
		} else if (URLBASE64.equalsIgnoreCase(algorithm)) {
			encoder = new UrlBase64Encoder();
			
		} else {
			throw new RuntimeException(String.format("The algorithm '%s' is not support.", algorithm));
		}
		return encoder;
	}

}
