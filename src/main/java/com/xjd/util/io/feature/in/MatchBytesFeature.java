package com.xjd.util.io.feature.in;

import java.util.EventListener;

/**
 * <pre>
 * 在流读取过程中，匹配查找给定的bytes数据，用于流式过滤或解析
 * 对流的匹配查找会影响流的读取速度, 影响程度视具体实现而定
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-13
 */
public interface MatchBytesFeature extends InputStreamFeature {

	/**
	 * <pre>
	 * 设置要在流中查找到的字节数据
	 * </pre>
	 * 
	 * @param bytes
	 */
	void setBytesToMatch(byte[] bytes);

	/**
	 * <pre>
	 * 获取要在流中查找到的字节数据
	 * </pre>
	 * 
	 * @return
	 */
	byte[] getBytesToMatch();

	/**
	 * <pre>
	 * 设置是否过滤掉匹配的数据
	 * </pre>
	 * 
	 * @param filter
	 */
	void setFilterMatched(boolean filter);

	/**
	 * <pre>
	 * 获取是否过滤掉匹配的数据
	 * </pre>
	 * 
	 * @return
	 */
	boolean isFilterMatched();

	/**
	 * <pre>
	 * 添加MatchListener
	 * </pre>
	 * 
	 * @param lsnr
	 */
	void addMatchListener(MatchListener lsnr);

	/**
	 * <pre>
	 * 移除MatchListener
	 * </pre>
	 * 
	 * @param lsnr
	 */
	void removeMatchListener(MatchListener lsnr);

	/**
	 * <pre>
	 * 匹配监听器
	 * </pre>
	 * 
	 * @author elvis.xu
	 * @since 2014-2-13
	 */
	public static interface MatchListener extends EventListener {
		/**
		 * <pre>
		 * 在流的读取过程中，若匹配到了给定数据，就会回调该方法
		 * </pre>
		 * 
		 * @param in
		 * @param index
		 */
		public void onMatched(MatchBytesFeature in);
	}
}
