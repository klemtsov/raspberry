package ru.klemtsov.raspberrypi;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.springframework.stereotype.Component;
import ru.klemtsov.raspberrypi.seven_led.SevenLedController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;

@Component
public class Triggers {

    private GpioController gpioController;

    private SevenLedController sevenLedController;


    @PostConstruct
    public void init() {
        System.out.println("Запуск класса Triggers");
        gpioController = GpioFactory.getInstance();
        sevenLedController = new SevenLedController(gpioController);
        sevenLedController.start();
        for (int i = 0; i < 1000; i++){
            sevenLedController.setValue(i);
            try {
                Thread.sleep(1000L);
                System.out.printf("Вывод цифры %4d\n", i);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }

    @PreDestroy
    public void destroy(){
        sevenLedController.stop();
    }

    public GpioController getGpioController() {
        return gpioController;
    }

}
