package com.appspot.aniekanedwardakai.jireh;

import com.google.android.gms.common.api.BooleanResult;

/**
 * Created by Teddy on 7/10/2016.
 */
public class ModifiableBooleanValue {

    Boolean value = false;

    public ModifiableBooleanValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
