package com.api.cars.models.enums;

public enum Traction {

    AWD(1),
    FWD(2),
    RWD(3);

    private final int code;

    private Traction(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static Traction valueOf(int code) {
        for (Traction value : Traction.values()){
            if (value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}
