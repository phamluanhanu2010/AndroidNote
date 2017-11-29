package com.strategy.intecom.vtc.vtctracking.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thuy Chi on 7/7/16.
 */
public enum TypeActionConnection {

    TYPE_ACTION(0),

    TYPE_ACTION_LOGIN(1),

    TYPE_ACTION_REGISTER(2),

    TYPE_ACTION_GET_GA_ID(3),

    TYPE_ACTION_LOGIN_FB(4),

    TYPE_ACTION_LOGIN_GOOGLE(5),

    TYPE_ACTION_GET_BALANCE(6),

    TYPE_ACTION_FIRST_OPEN_SERVER(7),

    TYPE_ACTION_CHANGE_PASSWORD(8),

    TYPE_ACTION_GET_IS_INSTALLED(9),

    TYPE_ACTION_RESET_PASS_EMAIL(10),

    TYPE_ACTION_RESET_PASS_PHONE(11),

    TYPE_ACTION_GET_PACKAGE_MONTH_CARD_INFO(12),

    TYPE_ACTION_GET_PACKAGE_NORMAL_INFO(13),

    TYPE_ACTION_CREATE_ORDER(14),

    TYPE_ACTION_CONFIRM_ORDER(15),

    TYPE_ACTION_GET_ACCOUNT_DETAIL(16),

    TYPE_ACTION_UPDATE_PROFILE(17),

    TYPE_ACTION_AUTO_LOGIN(18),

    TYPE_ACTION_GET_WAP_INFO(19);

    private static final Map<Integer, TypeActionConnection> typesByValue = new HashMap<>();

    private final int valuesConnectionType;

    TypeActionConnection(int value) {
        this.valuesConnectionType = value;
    }

    public int getValuesTypeDialog() {
        return valuesConnectionType;
    }

    static {
        for (TypeActionConnection type : TypeActionConnection.values()) {
            typesByValue.put(type.valuesConnectionType, type);
        }
    }

    public static TypeActionConnection forValue(int value) {
        TypeActionConnection type = typesByValue.get(value);
        if (type == null)
            return TypeActionConnection.TYPE_ACTION;
        return typesByValue.get(value);
    }
}
