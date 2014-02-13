package com.xjd.util.io.feature;

import java.io.InputStream;

import com.xjd.util.io.feature.impl.in.InputStreamFeatureBinder;
import com.xjd.util.io.feature.in.InputStreamFeature;

/**
 * <pre>
 * IO特征工具类
 * </pre>
 * @author elvis.xu
 * @since 2014-2-11
 */
public abstract class IOFeatures {

	/**
	 * <pre>
	 * 为输入流添加特性
	 * </pre>
	 * @param in
	 * @param featureClasses
	 * @return
	 */
	public static InputStream bind(InputStream in, Class<? extends InputStreamFeature>... featureClasses) {
		return InputStreamFeatureBinder.bind(in, featureClasses);
	}
	
	/**
	 * <pre>
	 * 为输入流添加特性
	 * </pre>
	 * @param in
	 * @param featureClass
	 * @param featureImpl
	 * @return
	 */
	public static InputStream bind(InputStream in, Class<? extends InputStreamFeature> featureClass, InputStreamFeature featureImpl) {
		return InputStreamFeatureBinder.bind(in, featureClass, featureImpl);
	}
	
	/**
	 * <pre>
	 * 为输入流添加特性
	 * </pre>
	 * @param in
	 * @param featureClasses
	 * @return
	 */
	public static InputStream bindWithNewStage(InputStream in, Class<? extends InputStreamFeature>... featureClasses) {
		return InputStreamFeatureBinder.bind(InputStreamFeatureBinder.newStage(in), featureClasses);
	}
	
	/**
	 * <pre>
	 * 为输入流添加特性
	 * </pre>
	 * @param in
	 * @param featureClass
	 * @param featureImpl
	 * @return
	 */
	public static InputStream bindWithNewStage(InputStream in, Class<? extends InputStreamFeature> featureClass, InputStreamFeature featureImpl) {
		return InputStreamFeatureBinder.bind(InputStreamFeatureBinder.newStage(in), featureClass, featureImpl);
	}
}
