package com.api.cars.models.enums;

public enum EnginePosition {
    FRONT(1),
    CENTRAL(2),
    REAR(3);

    private final int code;

    private EnginePosition(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static EnginePosition valueOf(int code){
        for (EnginePosition enginePosition : EnginePosition.values()){
            if (enginePosition.getCode() == code){
                return enginePosition;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}

