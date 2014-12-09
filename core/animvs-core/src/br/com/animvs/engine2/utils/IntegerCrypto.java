package br.com.animvs.engine2.utils;

public class IntegerCrypto {
    public abstract class ChangeListener {
        protected abstract void eventChanged();
    }

    private ChangeListener listener;

    private int value;
    private AnimvsIntCrypto crypto;

    protected final AnimvsIntCrypto getCrypto() {
        return crypto;
    }

    public int getValue() {
        return crypto.decipher(this.value);
    }

    public void setValue(int value) {
        this.value = crypto.encipher(value);

        if (listener != null)
            listener.eventChanged();
    }

    public IntegerCrypto(int value, AnimvsIntCrypto crypto) {
        if (crypto == null)
            throw new RuntimeException("The parameter 'crypto' must be != null");

        this.crypto = crypto;
        this.value = crypto.encipher(value);
    }

    public void increment() {
        setValue(getValue() + 1);
    }

    public void decrement() {
        setValue(getValue()-1);
    }

    public void setListener(ChangeListener listener){
        this.listener = listener;
    }
}
