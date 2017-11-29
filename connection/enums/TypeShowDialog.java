package com.strategy.intecom.vtc.vtctracking.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr. Ha on 6/1/16.
 */
public enum TypeShowDialog {

    TYPE_SHOW_MESSAGE_INFO(0),
    TYPE_SHOW_MESSAGE_CONFIRM(1),
    ;

    private static final Map<Integer, TypeShowDialog> typesByValue = new HashMap<>();

    private final int valuesDialogType;

    TypeShowDialog(int value) {
        this.valuesDialogType = value;
    }

    public int getValuesTypeDialog() {
        return valuesDialogType;
    }

    static {
        for (TypeShowDialog type : TypeShowDialog.values()) {
            typesByValue.put(type.valuesDialogType, type);
        }
    }

    public static TypeShowDialog forValue(int value) {
        TypeShowDialog type = typesByValue.get(value);
        if (type == null)
            return TypeShowDialog.TYPE_SHOW_MESSAGE_INFO;
        return typesByValue.get(value);
    }

}
