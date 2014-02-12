package com.xjd.util.io.feature.impl.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.xjd.util.io.feature.in.CountBytesFeature;
import com.xjd.util.io.feature.in.InputStreamFeature;

public class InputStreamFeatureBinderTest {

	InputStream in;

	@Before
	public void setup() throws IOException {
		in = new ConstInputStream();
	}

	@Test
	public void test() throws IOException {
		Map<Class<? extends InputStreamFeature>, Object> featureMap = new LinkedHashMap<Class<? extends InputStreamFeature>, Object>();
		featureMap.put(CountBytesFeature.class, null);

		InputStream win = InputStreamFeatureBinder.bind(in, featureMap);
		CountBytesFeature cf = (CountBytesFeature) win;

		win.read();
		cf.getUsedBytes();
		assertThat(1L).isEqualTo(cf.getUsedBytes());
		assertThat(1L).isEqualTo(cf.getReadBytes());

		win.read(new byte[2]);
		assertThat(3L).isEqualTo(cf.getUsedBytes());
		assertThat(3L).isEqualTo(cf.getReadBytes());

		win.skip(3);
		assertThat(6L).isEqualTo(cf.getUsedBytes());
		assertThat(3L).isEqualTo(cf.getSkippedBytes());

	}

}
