package com.xjd.util.io.feature.in;

import java.io.IOException;
import java.util.EventListener;

/**
 * <pre>
 * 与InputStream读取到结尾相关的特性
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-13
 */
public interface EOFFeature extends InputStreamFeature {

	/**
	 * <pre>
	 * InputStream是否已到结尾。
	 * 注意该方法会掉用InputStream.read()方法来进行判断, 所以可能会阻塞等待。
	 * </pre>
	 * 
	 * @return
	 * @throws IOException
	 */
	boolean isEOF() throws IOException;

	/**
	 * <pre>
	 * 添加EOFListener
	 * </pre>
	 * 
	 * @param lsnr
	 */
	void addEOFListener(EOFListener lsnr);

	/**
	 * <pre>
	 * 移除EOFListener
	 * </pre>
	 * 
	 * @param lsnr
	 */
	void removeEOFListener(EOFListener lsnr);

	/**
	 * <pre>
	 * 流结束监听器
	 * </pre>
	 * 
	 * @author elvis.xu
	 * @since 2014-2-13
	 */
	public static interface EOFListener extends EventListener {
		/**
		 * <pre>
		 * 当流读取到结尾时会回调该方法
		 * </pre>
		 * 
		 * @param in
		 */
		void onEOF(EOFFeature in);
	}
}
