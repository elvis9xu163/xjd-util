package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * 流装饰特性
 * 在流头部(开始)和结尾处加入指定的字节修饰数据
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-13
 */
public interface DecorateFeature extends InputStreamFeature {

	/**
	 * <pre>
	 * 设置流头部修饰字节
	 * </pre>
	 * 
	 * @param bytes
	 */
	void setDecoratedHead(byte[] bytes);

	/**
	 * <pre>
	 * 获取流头部修饰字节
	 * </pre>
	 * 
	 * @return
	 */
	byte[] getDecoratedHead();

	/**
	 * <pre>
	 * 设置流结尾修饰字节
	 * </pre>
	 * 
	 * @param bytes
	 */
	void setDecoratedTail(byte[] bytes);

	/**
	 * <pre>
	 * 获取流结尾修饰字节
	 * </pre>
	 * 
	 * @return
	 */
	byte[] getDecoratedTail();

	/**
	 * <pre>
	 * 添加DecorateListener
	 * </pre>
	 * 
	 * @param lsnr
	 */
	void addDecorateListener(DecorateListener lsnr);

	/**
	 * <pre>
	 * 移除DecorateListener
	 * </pre>
	 * 
	 * @param lsnr
	 */
	void removeDecorateListener(DecorateListener lsnr);

	public static interface DecorateListener extends EventListener {

		/**
		 * <pre>
		 * 在修饰头部之前调用, 在以下情况下该方法不会被回调:
		 * 1. 没有设置修饰头(head=null)
		 * 2. 已经开始修饰头
		 * 
		 * </pre>
		 * 
		 * @param in
		 */
		void onBeforeDecorateHead(DecorateFeature in);

		/**
		 * <pre>
		 * 在修饰头部之后调用, 在以下情况下该方法不会被回调:
		 * 1. 没有设置修饰头(head=null)
		 * 2. 已经结束修饰头
		 * 
		 * </pre>
		 * 
		 * @param in
		 */
		void onAfterDecorateHead(DecorateFeature in);

		/**
		 * <pre>
		 * 在修饰尾部之前调用, 在以下情况下该方法不会被回调:
		 * 1. 没有设置修饰尾(tail=null)
		 * 2. 已经开始修饰尾
		 * 
		 * </pre>
		 * 
		 * @param in
		 */
		void onBeforeDecorateTail(DecorateFeature in);

		/**
		 * <pre>
		 * 在修饰尾部之后调用, 在以下情况下该方法不会被回调:
		 * 1. 没有设置修饰尾(tail=null)
		 * 2. 已经结束修饰尾
		 * 
		 * </pre>
		 * 
		 * @param in
		 */
		void onAfterDecorateTail(DecorateFeature in);
	}
}
