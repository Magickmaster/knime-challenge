package com.jonassigel;

import java.util.function.Function;

public enum AllowedType {
    INT(Integer::valueOf), DOUBLE(Double::valueOf), STRING(Object::toString);

    public final Function<String, ? super Object> stringCaster;

    AllowedType(Function<String, ? super Object> stringCaster) {
        this.stringCaster = stringCaster;
    }
}
