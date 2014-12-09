package br.com.animvs.engine2.utils;

import com.badlogic.gdx.Preferences;

public class IntegerFromProperties extends IntegerCrypto {

    private String key;
    private Preferences pref;

    @Override
    public void setValue(int value) {
        super.setValue(value);

        pref.putInteger(key, getCrypto().encipher(value));
        pref.flush();
    }

    public void increment(){
        setValue(getValue() + 1);
    }

    public void decrement(){
        setValue(getValue() - 1);
    }

    public IntegerFromProperties(Preferences pref, int defValue, String key, AnimvsIntCrypto crypto) {
        super(0, crypto);
        //TODO: Multi profiles (one per google+ user):

        if (key == null)
            throw new RuntimeException("The parameter 'key' must be != null");

        if (pref == null)
            throw new RuntimeException("The parameter 'pref' must be != null");

        this.pref = pref;
        this.key = key;

        if (pref.contains(key))
            setValue(crypto.decipher(pref.getInteger(key)));
        else
            setValue(defValue);
    }
}
