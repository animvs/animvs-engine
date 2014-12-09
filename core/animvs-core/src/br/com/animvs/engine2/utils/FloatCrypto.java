package br.com.animvs.engine2.utils;

public class FloatCrypto {
    public abstract class ChangeListener {
        protected abstract void eventChanged();
    }

    private ChangeListener listener;

    private int value;
    private AnimvsIntCrypto crypto;

    protected final AnimvsIntCrypto getCrypto() {
        return crypto;
    }

    public float getValue() {
        return Float.intBitsToFloat(crypto.decipher(value));
    }

    public void setValue(float value) {
        this.value = crypto.encipher(Float.floatToIntBits(value));

        if (listener != null)
            listener.eventChanged();
    }

    public FloatCrypto(float value, AnimvsIntCrypto crypto) {
        if (crypto == null)
            throw new RuntimeException("The parameter 'crypto' must be != null");

        this.crypto = crypto;
        this.value = crypto.encipher(Float.floatToIntBits(value));
    }

    public void increment() {
        setValue(getValue() + 1f);
    }

    public void decrement() {
        setValue(getValue() - 1f);
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }
}
