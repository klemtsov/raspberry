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
    private GpioPinDigitalInput enRPin;
    private GpioPinDigitalInput enLPin;
    private GpioPinDigitalOutput enCPin;
    private int encoderCounter = 0;


    @PostConstruct
    public void init() {
        System.out.println("Запуск класса Triggers");
        gpioController = GpioFactory.getInstance();
        btn = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
        btn.setShutdownOptions(true);
        btn.setDebounce(50);

        led = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED1", PinState.LOW);
        led.setShutdownOptions(true);
        btn.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                System.out.println(String.format("%s Кнопка %s", Calendar.getInstance().getTime(), gpioPinDigitalStateChangeEvent.getState()));
                //led.setState(gpioPinDigitalStateChangeEvent.getState());
                if (gpioPinDigitalStateChangeEvent.getState().isHigh()) {
                    led.toggle();
                }
            }
        });

        led.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                System.out.println(String.format("led %s state %s", gpioPinDigitalStateChangeEvent.getPin(), gpioPinDigitalStateChangeEvent.getState()));
            }
        });

        connectEncoder();

    }

    public GpioController getGpioController() {
        return gpioController;
    }

    private void connectEncoder() {
        enRPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        enRPin.setShutdownOptions(true);
        enRPin.addListener(new GpioPinListenerDigital() {
                               @Override
                               public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                                   System.out.println(String.format("Правый обработчик: правый контакт %s, левый контакт %s", gpioPinDigitalStateChangeEvent.getState(), enLPin.getState()));
                                   if (enRPin.isHigh()) {
                                       if (enLPin.isHigh()) {
                                           encoderCounter++;
                                       } else {
                                           encoderCounter--;
                                       }
                                       System.out.println(String.format("Счетчик = %d", encoderCounter));
                                   }
                               }
                           }
        );

        enLPin = gpioController.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
        enLPin.setShutdownOptions(true);
        enLPin.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpioPinDigitalStateChangeEvent) {
                System.out.println(String.format("Левый обработчик: правый контакт %s, левый контакт %s", enRPin.getState(), gpioPinDigitalStateChangeEvent.getState()));
            }
        });

    }
}
