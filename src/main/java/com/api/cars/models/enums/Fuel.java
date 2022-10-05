package com.api.cars.models.enums;

public enum Fuel {
    GASOLINE(1),
    DIESEL(2),
    BIO_DIESEL(3),
    ETHANOL(4);

    private final int code;

    private Fuel(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static Fuel valueOf(int code){
        for (Fuel fuel : Fuel.values()){
            if (fuel.getCode() == code){
                return fuel;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}

