package com.hasiok.coffemaker.service;

import com.google.gson.Gson;
import com.hasiok.coffemaker.controller.CoffeeController;
import com.hasiok.coffemaker.model.Alert;
import com.hasiok.coffemaker.model.MixingContainer;
import com.hasiok.coffemaker.respository.AlertRepository;
import com.hasiok.coffemaker.thread.IngredientsWatcher;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
public class CoffeeService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AlertRepository alertRepository;
    private MixingContainer mixingContainer;

    private static final Logger LOGGER = Logger.getLogger(CoffeeController.class);
    private static final Gson GSON = new Gson();
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public CoffeeService(SimpMessagingTemplate simpMessagingTemplate, AlertRepository alertRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.alertRepository = alertRepository;
        this.mixingContainer = MixingContainer.getInstance();
    }

    public MixingContainer getIngredients() {
        LOGGER.debug("Returning ingredients: " + GSON.toJson(this.mixingContainer));
        return this.mixingContainer;
    }

    public MixingContainer updateIngredientsLevel(MixingContainer mixingContainer) {
        mixingContainer.setCurrentCoffeeLevel(Math.round(mixingContainer.getCurrentMilkLevel() * 100) / 100D);
        mixingContainer.setCurrentMilkLevel(Math.round(mixingContainer.getCurrentMilkLevel() * 100) / 100D);
        mixingContainer.setCurrentSugarLevel(Math.round(mixingContainer.getCurrentSugarLevel() * 100) / 100D);
        LOGGER.debug("Updating container with values: " + GSON.toJson(mixingContainer));
        this.mixingContainer = mixingContainer;
        new IngredientsWatcher(mixingContainer, simpMessagingTemplate, alertRepository).start();
        return this.mixingContainer;
    }

    public Alert handleAlert(Alert alert) {
        LOGGER.debug("Received new alert: " + GSON.toJson(alert));
        return alertRepository.save(alert);
    }

    public List<Alert> getAllAlerts() {
        LOGGER.debug("Getting all alerts for admin");
        return alertRepository.findAllByOrderByTimestampDesc();
    }
}
