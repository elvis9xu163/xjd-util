package com.xjd.util.io.feature.impl.in;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.cglib.proxy.Enhancer;

import com.xjd.util.io.feature.IOFeatureRuntimeException;
import com.xjd.util.io.feature.impl.in.featureimpl.CloseFeatureImpl;
import com.xjd.util.io.feature.impl.in.featureimpl.CountBytesFeatureImpl;
import com.xjd.util.io.feature.impl.in.featureimpl.DecorateFeatureImpl;
import com.xjd.util.io.feature.impl.in.featureimpl.EOFFeatureImpl;
import com.xjd.util.io.feature.impl.in.featureimpl.MatchBytesFeatureImpl;
import com.xjd.util.io.feature.impl.in.featureimpl.PreciseBytesFeatureImpl;
import com.xjd.util.io.feature.in.CloseFeature;
import com.xjd.util.io.feature.in.CountBytesFeature;
import com.xjd.util.io.feature.in.DecorateFeature;
import com.xjd.util.io.feature.in.EOFFeature;
import com.xjd.util.io.feature.in.InputStreamFeature;
import com.xjd.util.io.feature.in.MatchBytesFeature;
import com.xjd.util.io.feature.in.PreciseBytesFeature;

/**
 * <pre>
 * InputStream的代理组装器
 * 通过代理方式为InputStream添加特征
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-11
 */
public abstract class InputStreamFeatureBinder {

	protected static Map<Class<? extends InputStreamFeature>, Class<? extends InputStreamFeature>> featureClassMap = null;

	/**
	 * <pre>
	 * 注册默认的特性
	 * </pre>
	 */
	protected static void registerDefaultFeatures() {
		if (featureClassMap == null) {
			featureClassMap = new HashMap<Class<? extends InputStreamFeature>, Class<? extends InputStreamFeature>>();
			// add default
			featureClassMap.put(CloseFeature.class, CloseFeatureImpl.class);
			featureClassMap.put(CountBytesFeature.class, CountBytesFeatureImpl.class);
			featureClassMap.put(DecorateFeature.class, DecorateFeatureImpl.class);
			featureClassMap.put(EOFFeature.class, EOFFeatureImpl.class);
			featureClassMap.put(MatchBytesFeature.class, MatchBytesFeatureImpl.class);
			featureClassMap.put(PreciseBytesFeature.class, PreciseBytesFeatureImpl.class);
		}
	}

	/**
	 * <pre>
	 * 注册自定义特性
	 * </pre>
	 * 
	 * @param featureClass
	 * @param featureImplClass
	 */
	public static void registerFeature(Class<? extends InputStreamFeature> featureClass, Class<? extends InputStreamFeature> featureImplClass) {
		registerDefaultFeatures();
		checkFeatureClass(featureClass);
		checkFeatureImplClass(featureImplClass, featureClass);
		featureClassMap.put(featureClass, featureImplClass);
	}

	/**
	 * <pre>
	 * 正常情况下为InputStream添加特性时，一种特性只能添加一次，
	 * 由于需要，若想对同一个特性添加多次时，可以先使用该方法，隐藏
	 * 前面添加的所有特性(但仍然有效)，就像一个全新的InputStream一
	 * 样，再添加特性。
	 * </pre>
	 * 
	 * @param in
	 * @return
	 */
	public static InputStream newStage(InputStream in) {
		return new InputStreamWrapper(in);
	}

	/**
	 * <pre>
	 * 添加特性
	 * </pre>
	 * 
	 * @param in
	 * @param featureClass
	 * @return
	 */
	public static InputStream bind(InputStream in, Class<? extends InputStreamFeature>... featureClasses) {
		if (featureClasses == null || featureClasses.length == 0) {
			return in;
		}
		Map<Class<? extends InputStreamFeature>, Object> featureMap = new LinkedHashMap<Class<? extends InputStreamFeature>, Object>(
				featureClasses.length);
		for (Class<? extends InputStreamFeature> featureClass : featureClasses) {
			checkFeatureClass(featureClass);
			Class<? extends InputStreamFeature> featureImplClass = getFeatureImplClass(featureClass);
			if (featureImplClass == null) {
				throw new IOFeatureRuntimeException("Cannot find impl class for feature[" + featureClass + "]");
			}
			featureMap.put(featureClass, featureImplClass);
		}
		return bind(in, featureMap);
	}

	/**
	 * <pre>
	 * 添加特性
	 * </pre>
	 * 
	 * @param in
	 * @param featureClass
	 * @param featureImpl
	 * @return
	 */
	public static InputStream bind(InputStream in, Class<? extends InputStreamFeature> featureClass, InputStreamFeature featureImpl) {
		checkFeatureClass(featureClass);
		checkFeatureImplInstance(featureImpl, featureClass);
		Map<Class<? extends InputStreamFeature>, Object> featureMap = new LinkedHashMap<Class<? extends InputStreamFeature>, Object>(1);
		featureMap.put(featureClass, featureImpl);
		return bind(in, featureMap);
	}

	/**
	 * <pre>
	 * 添加特性
	 * </pre>
	 * 
	 * @param in
	 * @param featureMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static InputStream bind(InputStream in, Map<Class<? extends InputStreamFeature>, Object> featureMap) {
		if (featureMap == null || featureMap.size() == 0) {
			return in;
		}
		// 默认值设置
		defaultFeature(featureMap);

		// 校验
		checkFeatureMap(featureMap);

		// 创建Interceptor
		InputStreamInterceptor interceptor = null;
		if (in instanceof InputStreamInterceptorGetter) {
			interceptor = ((InputStreamInterceptorGetter) in).getInputStreamInterceptor();
		} else {
			interceptor = new InputStreamInterceptor(in);
		}

		// 构建Interceptor中的Delegate
		for (Entry<Class<? extends InputStreamFeature>, Object> entry : featureMap.entrySet()) {
			Class<? extends InputStreamFeature> key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Class) {
				interceptor.addFeature(key, (Class<? extends InputStreamFeature>) value);
			} else {
				interceptor.addFeature(key, (InputStreamFeature) value);
			}
		}

		// 创建代理对象
		List<Class<? extends InputStreamFeature>> features = interceptor.getFeatures();
		List<Class<?>> interfaces = new ArrayList<Class<?>>(features.size() + 1);
		interfaces.addAll(featureMap.keySet());
		interfaces.add(InputStreamInterceptorGetter.class);

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(InputStream.class);
		enhancer.setInterfaces(interfaces.toArray(new Class[0]));
		enhancer.setCallback(interceptor);
		return (InputStream) enhancer.create();
	}

	protected static void defaultFeature(Map<Class<? extends InputStreamFeature>, Object> featureMap) {
		if (featureMap != null && featureMap.size() > 0) {
			for (Entry<Class<? extends InputStreamFeature>, Object> entry : featureMap.entrySet()) {
				Class<? extends InputStreamFeature> key = entry.getKey();
				Object value = entry.getValue();
				if (value == null) {
					entry.setValue(getFeatureImplClass(key));
				}
			}
		}
	}

	protected static void checkFeatureMap(Map<Class<? extends InputStreamFeature>, Object> featureMap) {
		if (featureMap != null && featureMap.size() > 0) {
			for (Entry<Class<? extends InputStreamFeature>, Object> entry : featureMap.entrySet()) {
				Class<? extends InputStreamFeature> key = entry.getKey();
				checkFeatureClass(key);
				checkFeatureImpl(entry.getValue(), key);
			}
		}
	}

	protected static void checkFeatureClass(Class<?> clazz) {
		if (clazz == null) {
			throw new IOFeatureRuntimeException("feature class cannot be null.");
		}
		if (!InputStreamFeature.class.isAssignableFrom(clazz)) {
			throw new IOFeatureRuntimeException("feature class[" + clazz + "] must be subclass of InputStreamFeature.");
		}
		if (InputStreamFeature.class.equals(clazz)) {
			throw new IOFeatureRuntimeException("cannot use directly InputStreamFeature as feature class.");
		}
	}

	protected static void checkFeatureImplClass(Class<?> clazz, Class<?> featureClass) {
		if (clazz == null) {
			throw new IOFeatureRuntimeException("feature impl class cannot be null.");
		}
		if (!featureClass.isAssignableFrom(clazz)) {
			throw new IOFeatureRuntimeException("feature impl class[" + clazz + "] must implements feature class[" + featureClass + "].");
		}
		if (clazz.isInterface()) {
			throw new IOFeatureRuntimeException("feature impl class[" + clazz + "] must be a concrete class.");
		}
	}

	protected static void checkFeatureImplInstance(Object inst, Class<?> featureClass) {
		if (inst == null) {
			throw new IOFeatureRuntimeException("feature impl cannot be null.");
		}
		checkFeatureImplClass(inst.getClass(), featureClass);
	}

	protected static void checkFeatureImpl(Object clazzOrInst, Class<?> featureClass) {
		if (clazzOrInst == null) {
			throw new IOFeatureRuntimeException("feature impl instance or class cannot be null.");
		}
		if (clazzOrInst instanceof Class<?>) {
			checkFeatureImplClass((Class<?>) clazzOrInst, featureClass);
		} else {
			checkFeatureImplInstance(clazzOrInst, featureClass);
		}
	}

	protected static Class<? extends InputStreamFeature> getFeatureImplClass(Class<? extends InputStreamFeature> featureClass) {
		registerDefaultFeatures();
		return featureClassMap.get(featureClass);
	}

	protected static class InputStreamWrapper extends FilterInputStream {

		public InputStreamWrapper(InputStream in) {
			super(in);
		}

	}
}
