package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * 与输入流关闭相关的特性
 * 1. 可以设置关闭时是否关源InputStream
 * 2. 可以设置关闭后,再做读取等操作时,返回-1或抛出异常
 * 3. 可以添加监听器,监听流关闭
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-13
 */
public interface CloseFeature extends InputStreamFeature {
	/** 传递给源InputStream, 注意：如果设置不关闭源输入流,该模式下仍可读取源流中的数据 */
	static final int DELEGATE_TO_SOURCE = 0;
	/** 返回-1 */
	static final int RETURN_MINUS_1 = 1;
	/** 抛出异常 */
	static final int THROW_EXCEPTION = 2;

	/**
	 * <pre>
	 * 设置关闭后对流操作的响应模式
	 * 默认值为 {@link DELEGATE_TO_SOURCE}
	 * </pre>
	 * 
	 * @param mode
	 * @see DELEGATE_TO_SOURCE
	 * @see RETURN_MINUS_1
	 * @see THROW_EXCEPTION
	 */
	void setMode(int mode);

	/**
	 * <pre>
	 * 返回当前的mode
	 * </pre>
	 * 
	 * @return
	 */
	int getMode();

	/**
	 * <pre>
	 * 关闭流时，是否关闭源InputStream
	 * 默认为true
	 * </pre>
	 * 
	 * @param colse
	 */
	void setCloseSource(boolean colse);

	/**
	 * <pre>
	 * 返回当前设置
	 * </pre>
	 * 
	 * @return
	 */
	boolean getCloseSource();

	/**
	 * <pre>
	 * 是否已关闭
	 * </pre>
	 * 
	 * @return
	 */
	boolean isClosed();

	/**
	 * <pre>
	 * 添加流关闭监听器
	 * </pre>
	 * 
	 * @param listener
	 */
	void addCloseListener(CloseListener listener);

	/**
	 * <pre>
	 * 移除流关闭监听器
	 * </pre>
	 * 
	 * @param listener
	 */
	void removeCloseListener(CloseListener listener);

	public static interface CloseListener extends EventListener {
		void onClose(CloseFeature in);
	}
}
