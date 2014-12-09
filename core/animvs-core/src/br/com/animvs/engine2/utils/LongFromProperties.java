package br.com.animvs.engine2.utils;

import com.badlogic.gdx.Preferences;

public class LongFromProperties extends LongCrypto {

    private String key;
    private Preferences pref;

    @Override
    public void setValue(long value) {
        super.setValue(value);

        pref.putInteger(key + "-a", getA());
        pref.putInteger(key + "-b", getB());

        pref.flush();
    }

   public LongFromProperties(Preferences pref, long defValue, String key, AnimvsIntCrypto crypto) {
        super(0, crypto);

        if (key == null)
            throw new RuntimeException("The parameter 'key' must be != null");

        if (pref == null)
            throw new RuntimeException("The parameter 'pref' must be != null");

        this.pref = pref;
        this.key = key;

        if (!pref.contains(key + "-a") || !pref.contains(key + "-b"))
            setValue(defValue);
        else
            super.setValue(pref.getInteger(key + "-a"), pref.getInteger(key + "-b"));
    }
}
