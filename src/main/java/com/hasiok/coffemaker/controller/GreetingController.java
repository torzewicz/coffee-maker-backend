package com.hasiok.coffemaker.controller;

import com.hasiok.coffemaker.model.Greeting;
import com.hasiok.coffemaker.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/hello")
    public Greeting greeting(HelloMessage helloMessage) {
        return new Greeting("Hello " + helloMessage.getName());
    }

}
