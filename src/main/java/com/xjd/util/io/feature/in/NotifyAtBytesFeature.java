package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * 当InputStream字节被消费到指定字节时,通过该特性进行通知
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-12
 */
public interface NotifyAtBytesFeature extends BytesCounter {
	/**
	 * <pre>
	 * 添加NotifySizeListener
	 * </pre>
	 * @param listener
	 */
	void addNotifySizeListener(NotifySizeListener listener);

	/**
	 * <pre>
	 * 移除NotifySizeListener
	 * </pre>
	 * @param listener
	 */
	void removeNotifySizeListener(NotifySizeListener listener);

	/**
	 * InputStream字节消费总数达到指定大小时的监听器
	 * 
	 * @author elvis.xu
	 * @version 2013-1-25 下午7:06:55
	 */
	public static interface NotifySizeListener extends EventListener {

		/**
		 * 需要监控的消费字节数
		 * 
		 * @return > 0
		 */
		long getNotifySize();

		/**
		 * 当字节数达到需要监控的字节数时(精确达到,不多不少)回调该方法
		 * 
		 * @param obj
		 */
		void onSize(NotifyAtBytesFeature obj);
	}
}
