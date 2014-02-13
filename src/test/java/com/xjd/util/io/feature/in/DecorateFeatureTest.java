package com.xjd.util.io.feature.in;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.xjd.util.io.feature.IOFeatures;
import com.xjd.util.io.feature.in.DecorateFeature.DecorateListener;

public class DecorateFeatureTest {

    InputStream in;

    @Before
    public void setup() {
	in = new ByteArrayInputStream(new byte[] { 1 });
    }

    @Test
    public void test() throws IOException {
	DecorateFeature win = (DecorateFeature) IOFeatures.bind(in, DecorateFeature.class);

	final int[] rt = new int[] { 0 };

	win.addDecorateListener(new DecorateListener() {

	    @Override
	    public void onBeforeDecorateTail(DecorateFeature in) {
		rt[0] = 3;
	    }

	    @Override
	    public void onBeforeDecorateHead(DecorateFeature in) {
		rt[0] = 1;
	    }

	    @Override
	    public void onAfterDecorateTail(DecorateFeature in) {
		rt[0] = 4;
	    }

	    @Override
	    public void onAfterDecorateHead(DecorateFeature in) {
		rt[0] = 2;
	    }
	});

	win.setDecoratedHead(new byte[] { 1 });
	win.setDecoratedTail(new byte[] { 1, 2, 3 });

	win.read();
	assertThat(rt[0]).isEqualTo(1);

	win.read();
	assertThat(rt[0]).isEqualTo(2);

	win.read();
	assertThat(rt[0]).isEqualTo(3);

	win.read(new byte[2]);
	assertThat(rt[0]).isEqualTo(3);

	win.read();
	assertThat(rt[0]).isEqualTo(4);

    }

    @Test
    public void test2() throws IOException {
	DecorateFeature win = (DecorateFeature) IOFeatures.bind(in, DecorateFeature.class);

	win.setDecoratedHead(new byte[] { 3, 3, 3 });
	win.setDecoratedTail(new byte[] { 4, 4, 4 });

	byte[] buf = new byte[5];
	assertThat(win.read()).isEqualTo(3);

	assertThat(win.read(buf)).isEqualTo(2);
	assertThat(buf).isEqualTo(new byte[] { 3, 3, 0, 0, 0 });

	assertThat(win.read(buf)).isEqualTo(1);
	assertThat(buf).isEqualTo(new byte[] { 1, 3, 0, 0, 0 });

	assertThat(win.read(buf)).isEqualTo(3);
	assertThat(buf).isEqualTo(new byte[] { 4, 4, 4, 0, 0 });

    }

}
