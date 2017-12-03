package ru.klemtsov.raspberrypi;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Component
public class Triggers {

    private GpioController gpioController;
    private GpioPinDigitalInput btn;
    private GpioPinDigitalOutput led;

    @PostConstruct
    public void init(){
        System.out.println("Запуск класса Triggers");
        gpioController = GpioFactory.getInstance();
        btn = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        btn.setShutdownOptions(true);
        btn.setDebounce(50);

        led = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01,"LED1", PinState.LOW);
        led.setShutdownOptions(true);
        btn.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                System.out.println(String.format("%s Кнопка %s", Calendar.getInstance().getTime(), gpioPinDigitalStateChangeEvent.getState()));
                if (gpioPinDigitalStateChangeEvent.getState().isHigh()){
                 //   led.toggle();
                }
            }
        });
    }

    public GpioController getGpioController() {
        return gpioController;
    }
}
