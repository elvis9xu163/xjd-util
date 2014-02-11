package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * 记数InputStream被消费的字节数
 * </pre>
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
	
	void addProgressListener(ProgressListener listener);
	
	void removeProgressListener(ProgressListener listener);
	
	void addNotifySizeListener(NotifySizeListener listener);
	
	void removeNotifySizeListener(NotifySizeListener listener);

	/**
	 * InputStream字节被消费进度监听器
	 * @author  elvis.xu
	 * @version 2013-1-25 下午7:06:13
	 */
	public static interface ProgressListener extends EventListener {

		/**
		 * <pre>
		 * 对InputStream字节的每次消费都会回调该函数
		 * </pre>
		 * @param obj
		 * @param totallUsedBytes
		 * @param thisUsedBytes
		 */
		void onProgress(BytesCounter obj, long totallUsedBytes, long thisUsedBytes);

	}

	/**
	 * InputStream字节消费总数达到指定大小时的监听器
	 * @author  elvis.xu
	 * @version 2013-1-25 下午7:06:55
	 */
	public static interface NotifySizeListener extends EventListener {

		/**
		 * 需要监控的消费字节数
		 * @return > 0
		 */
		long getNotifySize();

		/**
		 * 当字节数达到需要监控的字节数时(精确达到,不多不少)回调该方法
		 * @param obj
		 */
		void onReachSize(BytesCounter obj);
	}
}
