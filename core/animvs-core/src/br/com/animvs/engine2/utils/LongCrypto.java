package br.com.animvs.engine2.utils;

public class LongCrypto {
    private AnimvsIntCrypto crypto;

    private int a;
    private int b;

    protected final AnimvsIntCrypto getCrypto() {
        return crypto;
    }

    public long getValue() {
        return retrieve(crypto.decipher(a), crypto.decipher(b));
    }

    public void setValue(long value) {
        process(value);
    }

    protected void setValue(int a, int b) {
        this.a = a;
        this.b = b;
    }

    protected final int getA() {
        return this.a;
    }

    protected final int getB() {
        return this.b;
    }

    public LongCrypto(long value, AnimvsIntCrypto crypto) {
        if (crypto == null)
            throw new RuntimeException("The parameter 'crypto' must be != null");

        this.crypto = crypto;
        process(value);
    }

    private long retrieve(int a, int b) {
        /*return ((long) a << 32) | ((long) b & 0xFFFFFFFL);*/
        return (((long)a) << 32) | (b & 0xffffffffL);
    }

    private void process(long value) {
        /*a =  crypto.encipher((int) ((value >>> 56) | (value >>> 48) | (value >>> 40) | (value >>> 32)));
        b = crypto.encipher((int) ((value >>> 24) | (value >>> 16) | (value >>> 8) | (value >>> 0)));*/

        this.a = crypto.encipher((int)(value >> 32));   // masking is implicit
        this.b = crypto.encipher((int) value);
    }
}
