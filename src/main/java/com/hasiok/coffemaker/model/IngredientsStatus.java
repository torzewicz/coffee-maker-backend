package com.hasiok.coffemaker.model;

import com.hasiok.coffemaker.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientsStatus {

    private AlarmType coffeeLevelStatus;
    private Double currentCoffeeLevel;
    private AlarmType sugarLevelStatus;
    private Double currentSugarLevel;
    private AlarmType milkLevelStatus;
    private Double currentMilkLevel;
}
