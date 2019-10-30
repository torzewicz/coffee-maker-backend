package com.hasiok.coffemaker.model;

import lombok.Data;

@Data
public class MixingContainer {

    private static MixingContainer instance = null;

    private Double currentCoffeeLevel;
    private Double currentSugarLevel;
    private Double currentMilkLevel;

    private MixingContainer() {
        this.currentCoffeeLevel = 0.5;
        this.currentSugarLevel = 0.5;
        this.currentMilkLevel = 0.5;
    }

    public static MixingContainer getInstance() {
        if (instance == null) {
            return new MixingContainer();
        } else return instance;
    }
}
