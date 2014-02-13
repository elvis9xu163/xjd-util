package com.xjd.util.io.feature.impl.in.featureimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xjd.util.io.feature.IOFeatureRuntimeException;
import com.xjd.util.io.feature.in.DecorateFeature;

public class DecorateFeatureImpl extends EOFFeatureImpl implements DecorateFeature {
    protected boolean hset, hbegin, hend, tset, tbegin, tend, sbegin;
    protected byte[] head, tail;
    protected int hp = 0, tp = 0;
    protected List<DecorateListener> lsnrs = null;

    @Override
    public void setDecoratedHead(byte[] bytes) {
	if (sbegin) {
	    throw new IOFeatureRuntimeException("流已开始使用.");
	}
	head = bytes;
	hp = 0;
	if (bytes == null) {
	    hset = false;
	} else {
	    hset = true;
	}
    }

    @Override
    public byte[] getDecoratedHead() {
	return head;
    }

    @Override
    public void setDecoratedTail(byte[] bytes) {
	if (tend) {
	    throw new IOFeatureRuntimeException("Tail修饰已经结束.");
	}
	if (tbegin) {
	    throw new IOFeatureRuntimeException("Tail修饰已经开始.");
	}
	tail = bytes;
	tp = 0;
	if (bytes == null) {
	    tset = false;
	} else {
	    tset = true;
	}
    }

    @Override
    public byte[] getDecoratedTail() {
	return tail;
    }

    @Override
    public void addDecorateListener(DecorateListener lsnr) {
	if (lsnrs == null) {
	    lsnrs = new ArrayList<DecorateFeature.DecorateListener>();
	}
	lsnrs.add(lsnr);
    }

    @Override
    public void removeDecorateListener(DecorateListener lsnr) {
	if (lsnrs != null) {
	    lsnrs.remove(lsnr);
	}
    }

    protected DecorateFeature getDecorateFeature() {
	return (DecorateFeature) getEnhancedInputStream();
    }

    protected void fire(int event) {
	if (lsnrs != null && lsnrs.size() > 0) {
	    DecorateFeature target = getDecorateFeature();
	    for (DecorateListener lsnr : lsnrs) {
		switch (event) {
		case 1: // head begin
		    lsnr.onBeforeDecorateHead(target);
		    break;
		case 2: // head end
		    lsnr.onAfterDecorateHead(target);
		    break;
		case 3: // tail begin
		    lsnr.onBeforeDecorateTail(target);
		    break;
		case 4: // tail end
		    lsnr.onAfterDecorateTail(target);
		    break;
		}
	    }
	}
    }

    protected int headHead() {
	if (!sbegin) sbegin = true;
	if (!hset) return -1;
	if (hend) return -1;
	if (!hbegin) {
	    hbegin = true;
	    fire(1);
	}
	if (hp == head.length) {
	    hend = true;
	    fire(2);
	    return -1;
	}
	return 0;
    }

    protected int readHead() {
	int i = headHead();
	if (i == -1) return i;
	return head[hp++];
    }

    protected int availableHead() {
	int i = headHead();
	if (i == -1) return i;
	return head.length - hp;
    }

    protected int readHead(byte[] buf, int off, int len) {
	int i = headHead();
	if (i == -1) return i;
	int toRead = head.length - hp;
	toRead = toRead <= len ? toRead : len;
	System.arraycopy(head, hp, buf, off, toRead);
	hp += toRead;
	return toRead;
    }

    protected int skipHead(long n) {
	int i = headHead();
	if (i == -1) return i;
	int toSkip = head.length - hp;
	toSkip = toSkip <= n ? toSkip : (int) n;
	hp += toSkip;
	return toSkip;
    }

    protected int tailTail() {
	if (!tset) return -1;
	if (tend) return -1;
	if (!tbegin) {
	    tbegin = true;
	    fire(3);
	}
	if (tp == tail.length) {
	    tend = true;
	    fire(4);
	    return -1;
	}
	return 0;
    }

    protected int readTail() {
	int i = tailTail();
	if (i == -1) return -1;
	return tail[tp++];
    }

    protected int availableTail() {
	int i = tailTail();
	if (i == -1) return -1;
	return tail.length - tp;
    }

    protected int readTail(byte[] buf, int off, int len) {
	int i = tailTail();
	if (i == -1) return -1;
	int toRead = tail.length - tp;
	toRead = toRead <= len ? toRead : len;
	System.arraycopy(tail, tp, buf, off, toRead);
	tp += toRead;
	return toRead;
    }

    protected int skipTail(long n) {
	int i = tailTail();
	if (i == -1) return -1;
	int toSkip = tail.length - tp;
	toSkip = toSkip <= n ? toSkip : (int) n;
	tp += toSkip;
	return toSkip;
    }

    @Override
    public int read() throws IOException {
	int i = readHead();
	if (i != -1) return i;

	i = super.read();
	if (i != -1) return i;

	return readTail();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
	int i = readHead(b, off, len);
	if (i != -1) return i;

	i = super.read(b, off, len);
	if (i != -1) return i;

	return readTail(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
	long i = skipHead(n);
	if (i != -1) return i;

	i = super.skip(n);
	if (i > 0) return i;

	if (!isEOF()) { // 如果流还没结束
	    return i;
	}

	i = skipTail(n);
	if (i != -1)
	    return i;
	else
	    return 0;
    }

    @Override
    public int available() throws IOException {
	int i = availableHead();
	if (i != -1) return i;
	
	i = super.available();
	if (i > 0) return i;
	
	if (!isEOF()) { // 如果流还没结束
	    return i;
	}
	
	i = availableTail();
	if (i != -1)
	    return i;
	else
	    return 0;
    }
}
