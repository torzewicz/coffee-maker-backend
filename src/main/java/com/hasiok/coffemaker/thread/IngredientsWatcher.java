package com.hasiok.coffemaker.thread;

import com.google.gson.Gson;
import com.hasiok.coffemaker.enums.AlarmType;
import com.hasiok.coffemaker.model.Alert;
import com.hasiok.coffemaker.model.IngredientsStatus;
import com.hasiok.coffemaker.model.MixingContainer;
import com.hasiok.coffemaker.respository.AlertRepository;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class IngredientsWatcher extends Thread {

    private MixingContainer mixingContainer;
    private SimpMessagingTemplate simpMessagingTemplate;
    private final AlertRepository alertRepository;

    private static final Logger LOGGER = Logger.getLogger(IngredientsWatcher.class);
    private static final Gson GSON = new Gson();

    public IngredientsWatcher(MixingContainer mixingContainer,
                              SimpMessagingTemplate simpMessagingTemplate,
                              AlertRepository alertRepository) {
        this.mixingContainer = mixingContainer;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.alertRepository = alertRepository;
    }

    @Override
    public void run() {

        IngredientsStatus.IngredientsStatusBuilder ingredientsStatusBuilder = IngredientsStatus.builder();

        if (mixingContainer.getCurrentCoffeeLevel() >= 0.7) {
            ingredientsStatusBuilder.coffeeLevelStatus(AlarmType.OK);
        } else if (mixingContainer.getCurrentCoffeeLevel() >= 0.4) {

            AlarmType alarmType = AlarmType.WARNING;
            ingredientsStatusBuilder.coffeeLevelStatus(alarmType);
            buildAndSentAlert(alarmType, "Low coffee level", mixingContainer.getCurrentCoffeeLevel());

        } else {
            AlarmType alarmType = AlarmType.CRITICAL;
            ingredientsStatusBuilder.coffeeLevelStatus(alarmType);
            buildAndSentAlert(alarmType, "Critical coffee level", mixingContainer.getCurrentCoffeeLevel());
        }

        if (mixingContainer.getCurrentSugarLevel() >= 0.7) {
            ingredientsStatusBuilder.sugarLevelStatus(AlarmType.OK);
        } else if (mixingContainer.getCurrentSugarLevel() >= 0.4) {
            AlarmType alarmType = AlarmType.WARNING;
            ingredientsStatusBuilder.sugarLevelStatus(alarmType);
            buildAndSentAlert(alarmType, "Low sugar level", mixingContainer.getCurrentSugarLevel());
        } else {
            AlarmType alarmType = AlarmType.CRITICAL;
            ingredientsStatusBuilder.sugarLevelStatus(alarmType);
            buildAndSentAlert(alarmType, "Critical sugar level", mixingContainer.getCurrentSugarLevel());
        }

        if (mixingContainer.getCurrentMilkLevel() >= 0.7) {
            ingredientsStatusBuilder.milkLevelStatus(AlarmType.OK);
        } else if (mixingContainer.getCurrentMilkLevel() >= 0.4) {
            AlarmType alarmType = AlarmType.WARNING;
            ingredientsStatusBuilder.milkLevelStatus(alarmType);
            buildAndSentAlert(alarmType, "Low milk level", mixingContainer.getCurrentMilkLevel());

        } else {
            AlarmType alarmType = AlarmType.CRITICAL;
            ingredientsStatusBuilder.milkLevelStatus(alarmType);
            buildAndSentAlert(alarmType, "Critical milk level", mixingContainer.getCurrentMilkLevel());
        }

        IngredientsStatus ingredientsStatus = ingredientsStatusBuilder
                .currentCoffeeLevel(mixingContainer.getCurrentCoffeeLevel())
                .currentSugarLevel(mixingContainer.getCurrentSugarLevel())
                .currentMilkLevel(mixingContainer.getCurrentMilkLevel())
                .build();



        LOGGER.debug("Sending message to admin with values: " + GSON.toJson(ingredientsStatus));
        this.simpMessagingTemplate.convertAndSend("/topic/admin/ingredients", ingredientsStatus);
    }

    private void buildAndSentAlert(AlarmType alarmType, String message, Double value) {

        String info = message + " " + value;

        Alert alert = Alert.builder()
                .alarmType(alarmType)
                .info(info)
                .build();

        LOGGER.debug("Sending alert with info: " + info);
        this.simpMessagingTemplate.convertAndSend("/topic/admin/alert", this.alertRepository.save(alert));
    }
}

