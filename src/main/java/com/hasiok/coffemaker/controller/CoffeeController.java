package com.hasiok.coffemaker.controller;

import com.hasiok.coffemaker.model.Alert;
import com.hasiok.coffemaker.model.MixingContainer;
import com.hasiok.coffemaker.service.CoffeeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CoffeeController {

    private final CoffeeService coffeeService;

    public CoffeeController(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @MessageMapping("/ingredients")
    @SendTo("/topic/ingredients")
    public MixingContainer getIngredients() {
        return this.coffeeService.getIngredients();
    }

    @MessageMapping("/ingredients/update")
    @SendTo("/topic/ingredients/update")
    public MixingContainer updateIngredientsLevel(@Payload MixingContainer mixingContainer) {
        return this.coffeeService.updateIngredientsLevel(mixingContainer);
    }

    @MessageMapping("/alert")
    @SendTo("/topic/admin/alert")
    public Alert handleAlert(@Payload Alert alert) {
        return this.coffeeService.handleAlert(alert);
    }

    @MessageMapping("/alert/all")
    @SendTo("/topic/admin/alert/all")
    public List<Alert> getAllAlerts() {
        return this.coffeeService.getAllAlerts();
    }


}
