package ru.klemtsov.raspberrypi;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Triggers {

    private GpioController gpioController;
    private GpioPinDigital btn;

    @PostConstruct
    public void init(){
        System.out.printf("Запуск класса Triggers");
        gpioController = GpioFactory.getInstance();
        btn = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);
        btn.setShutdownOptions(true);
        btn.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                System.out.printf("Кнопка " + gpioPinDigitalStateChangeEvent.getState());
            }
        });

    }

    public GpioController getGpioController() {
        return gpioController;
    }
}
