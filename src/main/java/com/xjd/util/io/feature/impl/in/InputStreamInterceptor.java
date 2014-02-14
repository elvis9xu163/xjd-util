package com.xjd.util.io.feature.impl.in;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.xjd.util.io.feature.IOFeatureRuntimeException;
import com.xjd.util.io.feature.in.InputStreamFeature;

public class InputStreamInterceptor implements InputStreamInterceptorGetter, MethodInterceptor {
	protected InputStream enhancedInputStream;
	protected InputStream source;
	protected InputStream lastDelegator;
	protected LinkedHashMap<Class<? extends InputStreamFeature>, InputStreamFeature> featureMap = new LinkedHashMap<Class<? extends InputStreamFeature>, InputStreamFeature>();

	public InputStreamInterceptor(InputStream source) {
		this.source = source;
		this.lastDelegator = source;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		if (enhancedInputStream == null) {
			enhancedInputStream = (InputStream) obj;
		}

		Class<?> methodClass = method.getDeclaringClass();

		// 调用Object中的方法
		if (methodClass.equals(Object.class)) {
			return method.invoke(source, args);
		}

		// 调用Feature中的方法
		if (InputStreamFeature.class.isAssignableFrom(methodClass) && !InputStreamFeature.class.equals(methodClass)) {
			// 存在该feature
			InputStreamFeature feature = featureMap.get(methodClass);
			if (feature != null) {
				return method.invoke(feature, args);
			}

			// 存在该feature的子类
			for (Entry<Class<? extends InputStreamFeature>, InputStreamFeature> entry : featureMap.entrySet()) {
				if (methodClass.isAssignableFrom(entry.getKey())) {
					return method.invoke(entry.getValue(), args);
				}
			}
		}

		// 调用InputStreamInterceptorAware中的方法
		if (InputStreamInterceptorGetter.class.isAssignableFrom(methodClass)) {
			return method.invoke(this, args);
		}

		// 调用InputStream中的方法
		try {
			return method.invoke(lastDelegator, args);
		} catch (Exception e) {
			if (e instanceof InvocationTargetException && e.getCause() != null) {
				throw e.getCause();
			}
			throw e;
		}
	}

	public void addFeature(Class<? extends InputStreamFeature> featureClass, InputStreamFeature featureImpl) {
		if (featureClass == null || featureImpl == null) {
			throw new IOFeatureRuntimeException("featureClass or featureImpl cannot be null.");
		}
		if (!featureClass.isAssignableFrom(featureImpl.getClass()) || !(featureImpl instanceof AbstractInputStreamFeature)) {
			throw new IOFeatureRuntimeException("featureImpl must be instance of featureClass[" + featureClass + "] and AbstractInputStreamFeature.");
		}
		if (featureMap.get(featureClass) != null) {
			throw new IOFeatureRuntimeException("feature[" + featureClass + "] already added.");
		}
		doAddFeature(featureClass, ((AbstractInputStreamFeature) featureImpl));
	}

	public void addFeature(Class<? extends InputStreamFeature> featureClass, Class<? extends InputStreamFeature> featureImplClass) {
		if (featureClass == null || featureImplClass == null) {
			throw new IOFeatureRuntimeException("featureClass or featureImplClass cannot be null.");
		}
		if (!featureClass.isAssignableFrom(featureImplClass) || !AbstractInputStreamFeature.class.isAssignableFrom(featureImplClass)) {
			throw new IOFeatureRuntimeException("featureImplClass must extends featureClass[" + featureClass + "] and AbstractInputStreamFeature.");
		}
		if (featureMap.get(featureClass) != null) {
			throw new IOFeatureRuntimeException("feature[" + featureClass + "] already added.");
		}
		try {
			InputStreamFeature featureImpl = featureImplClass.newInstance();
			doAddFeature(featureClass, ((AbstractInputStreamFeature) featureImpl));
		} catch (Exception e) {
			throw new IOFeatureRuntimeException(e);
		}
	}

	protected void doAddFeature(Class<? extends InputStreamFeature> featureClass, AbstractInputStreamFeature featureImpl) {
		featureImpl.setSource(lastDelegator);
		featureImpl.setInputStreamInterceptor(this);
		lastDelegator = (InputStream) featureImpl;
		featureMap.put(featureClass, featureImpl);
	}

	public void removeFeature(Class<? extends InputStreamFeature> featureClass) {
		if (featureClass != null && featureMap.get(featureClass) != null) {
			Entry<Class<? extends InputStreamFeature>, InputStreamFeature> cur = null, next = null;
			for (Entry<Class<? extends InputStreamFeature>, InputStreamFeature> entry : featureMap.entrySet()) {
				cur = next;
				next = entry;
				if (cur != null && featureClass.equals(cur.getKey())) {
					break;
				}
			}

			AbstractInputStreamFeature featureImpl;
			if (cur == null || !featureClass.equals(cur)) {
				// cur == null : 表示当前只有一个feature, 且就是要移除的feature
				// !featureClass.equals(cur) ： 表示要移除的feature为最后一个feature
				featureImpl = (AbstractInputStreamFeature) featureMap.get(featureClass);
				lastDelegator = featureImpl.getSource();
			} else {
				AbstractInputStreamFeature curFeatureImpl = (AbstractInputStreamFeature) cur.getValue();
				AbstractInputStreamFeature nextFeatureImpl = (AbstractInputStreamFeature) next.getValue();
				nextFeatureImpl.setSource(curFeatureImpl.getSource());
				featureImpl = curFeatureImpl;
			}

			featureImpl.setSource(null);
			featureImpl.setInputStreamInterceptor(null);
			featureMap.remove(featureClass);
		}
	}

	@Override
	public InputStreamInterceptor getInputStreamInterceptor() {
		return this;
	}

	public List<Class<? extends InputStreamFeature>> getFeatures() {
		List<Class<? extends InputStreamFeature>> list = new ArrayList<Class<? extends InputStreamFeature>>(featureMap.size());
		list.addAll(featureMap.keySet());
		return list;
	}

	public InputStream getEnhancedInputStream() {
		return enhancedInputStream;
	}

}
