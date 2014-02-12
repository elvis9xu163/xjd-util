package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * 记数InputStream被消费的字节数
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-11
 */
public interface BytesCounter extends InputStreamFeature {

	/**
	 * 通过<code>InputStream.read()</code>读取的字节数
	 * 
	 * @return
	 */
	long getReadBytes();

	/**
	 * 通过<code>InputStream.skip()</code>跳过的字节数
	 * 
	 * @return
	 */
	long getSkippedBytes();

	/**
	 * 通过<code>InputStream.read()</code>和<code>InputStream.skip()</code>使用的字节数和
	 * 
	 * @return
	 */
	long getUsedBytes();

	/**
	 * 修正读取的字节数
	 * 
	 * @param l
	 */
	void reviseReadBytes(long l);

	/**
	 * 修正跳过的字节数
	 * 
	 * @param l
	 */
	void reviseSkippedBytes(long l);

}
