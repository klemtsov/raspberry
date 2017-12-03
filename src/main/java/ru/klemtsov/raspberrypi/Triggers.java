package ru.klemtsov.raspberrypi;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Triggers {

    @PostConstruct
    public void init(){
        System.out.printf("Запуск класса Triggers");
    }
}
