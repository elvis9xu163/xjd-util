package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * InputStream字节消费进度监听特性
 * </pre>
 * @author elvis.xu
 * @since 2014-2-12
 */
public interface ListenProgressFeature extends BytesCounter {
	/**
	 * <pre>
	 * 添加一个进度监听器
	 * </pre>
	 * @param listener
	 */
	void addProgressListener(ProgressListener listener);

	/**
	 * <pre>
	 * 删除一个进度监听器
	 * </pre>
	 * @param listener
	 * @author elvis.xu
	 * @since 2014-2-12
	 */
	void removeProgressListener(ProgressListener listener);

	/**
	 * InputStream字节被消费进度监听器
	 * 
	 * @author elvis.xu
	 * @version 2013-1-25 下午7:06:13
	 */
	public static interface ProgressListener extends EventListener {

		/**
		 * <pre>
		 * 对InputStream字节的每次消费都会回调该函数
		 * </pre>
		 * 
		 * @param obj
		 * @param totallUsedBytes
		 * @param thisUsedBytes
		 */
		void onProgress(ListenProgressFeature obj, long totallUsedBytes, long thisUsedBytes);

	}
}
