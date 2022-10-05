package com.api.cars.models.enums;

public enum CarCategory {
    SUV(1),
    HATCHBACK(2),
    CROSSOVER(3),
    CONVERTIBLE(4),
    SEDAN(5),
    SPORT_CAR(6),
    COUPE(7),
    MINIVAN(8),
    STATION_WAGON(9),
    PICKUP(10);

    private final int code;

    private CarCategory(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static CarCategory valueOf(int code){
        for (CarCategory category : CarCategory.values()){
            if (category.getCode() == code){
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}
