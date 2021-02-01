package com.na_ho.pricer.thread;

import com.na_ho.pricer.jni.QuantJNI;

public abstract class QuantLibRunnable implements Runnable {

    protected QuantJNI quantJNI = null;

    public QuantJNI getQuantJNI() {
        return quantJNI;
    }
    public void setQuantJNI(QuantJNI quantJNI) {
        this.quantJNI = quantJNI;
    }

    public abstract void run();
}
