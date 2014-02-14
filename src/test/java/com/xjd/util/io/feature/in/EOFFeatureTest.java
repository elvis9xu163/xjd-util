package com.xjd.util.io.feature.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.xjd.util.io.feature.IOFeatures;
import com.xjd.util.io.feature.in.EOFFeature.EOFListener;

public class EOFFeatureTest {

    @Test
    public void test() throws IOException {
	InputStream in = new ByteArrayInputStream(new byte[] { 1, 1, 1 });
	EOFFeature win = (EOFFeature) IOFeatures.bind(in, EOFFeature.class);
	final int[] rt = new int[]{0};
	win.addEOFListener(new EOFListener() {
	    @Override
	    public void onEOF(EOFFeature in) {
		rt[0] = 1;
	    }
	});

	assertThat(win.isEOF()).isFalse();
	win.read();
	win.read();
	assertThat(rt[0]).isEqualTo(0);
	assertThat(win.isEOF()).isFalse();
	win.read();
	assertThat(win.isEOF()).isTrue();
	assertThat(rt[0]).isEqualTo(1);
    }

}
