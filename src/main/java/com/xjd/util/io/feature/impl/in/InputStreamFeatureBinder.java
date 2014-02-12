package com.xjd.util.io.feature.impl.in;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.cglib.proxy.Enhancer;

import com.xjd.util.io.feature.IOFeatureRuntimeException;
import com.xjd.util.io.feature.impl.in.featureimpl.CountBytesFeatureImpl;
import com.xjd.util.io.feature.in.CountBytesFeature;
import com.xjd.util.io.feature.in.InputStreamFeature;

/**
 * <pre>
 * InputStream的代理组装器
 * 通过代理方式为InputStream添加特征
 * </pre>
 * 
 * @author elvis.xu
 * @since 2014-2-11
 */
public class InputStreamFeatureBinder {

	protected static Map<Class<? extends InputStreamFeature>, Class<? extends InputStreamFeature>> featureClassMap = null;

	protected static void registerDefaultFeatures() {
		if (featureClassMap == null) {
			featureClassMap = new HashMap<Class<? extends InputStreamFeature>, Class<? extends InputStreamFeature>>();
			// add default
			featureClassMap.put(CountBytesFeature.class, CountBytesFeatureImpl.class);
		}
	}
	
	protected static Class<? extends InputStreamFeature> getFeatureImplClass(Class<? extends InputStreamFeature> featureClass) {
		registerDefaultFeatures();
		return featureClassMap.get(featureClass);
	}
	
	/**
	 * <pre>
	 * 注册自定义特性
	 * </pre>
	 * @param featureClass
	 * @param featureImplClass
	 */
	public static void registerFeature(Class<? extends InputStreamFeature> featureClass, Class<? extends InputStreamFeature> featureImplClass) {
		registerDefaultFeatures();
		featureClassMap.put(featureClass, featureImplClass);
	}

	/**
	 * <pre>
	 * 添加特性
	 * </pre>
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
		if (in instanceof InputStreamInterceptorAware) {
			interceptor = ((InputStreamInterceptorAware) in).getInputStreamInterceptor();
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
		interfaces.add(InputStreamInterceptorAware.class);

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
				Object value = entry.getValue();
				if (key == null) {
					throw new IOFeatureRuntimeException("feature class cannot be null.");
				}
				if (InputStreamFeature.class.equals(key)) {
					throw new IOFeatureRuntimeException("feature[" + key + "] class must be subclass of InputStreamFeature.");
				}
				if (value == null) {
					throw new IOFeatureRuntimeException("impl instance or class of feature[" + key + "] cannot be null.");
				}
				if (value instanceof Class) {
					Class valClass = (Class) value;
					if (!key.isAssignableFrom(valClass)) {
						throw new IOFeatureRuntimeException("feature impl class[" + valClass + "] must implements feature[" + key + "].");
					}
				} else {
					if (!key.isAssignableFrom(value.getClass())) {
						throw new IOFeatureRuntimeException("feature impl[" + value + "] must be instance of feature[" + key + "].");
					}
				}
			}
		}
	}

}
