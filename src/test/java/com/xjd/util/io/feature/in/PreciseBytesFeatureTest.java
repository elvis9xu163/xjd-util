package com.xjd.util.io.feature.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.xjd.util.io.feature.IOFeatures;

public class PreciseBytesFeatureTest {

    @Test
    public void test() throws IOException {
	InputStream in = new ByteArrayInputStream(new byte[] { 1, 1 });
	PreciseBytesFeature win = (PreciseBytesFeature) IOFeatures.bind(in, DecorateFeature.class, PreciseBytesFeature.class);
	
	DecorateFeature df = (DecorateFeature) win;
	df.setDecoratedHead(new byte[]{0,0});
	df.setDecoratedTail(new byte[]{2,2});
	
	byte[] buf = new byte[3];
	
	int c = win.read(buf);
	assertThat(c).isEqualTo(3);
	assertThat(buf).isEqualTo(new byte[]{0,0,1});
	
	c = (int) win.skip(6);
	assertThat(c).isEqualTo(3);
    }

}
