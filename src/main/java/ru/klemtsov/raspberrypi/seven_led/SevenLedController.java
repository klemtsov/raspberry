package ru.klemtsov.raspberrypi.seven_led;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class SevenLedController {

    private Thread worker;
    private SevenLedIndicator indicator;

    public SevenLedController(GpioController controller) {
        indicator = new SevenLedIndicator();
        GpioPinDigitalOutput[] leds = {
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_00, "LED_A", PinState.HIGH),

                controller.provisionDigitalOutputPin(RaspiPin.GPIO_01, "LED_B", PinState.HIGH),

                controller.provisionDigitalOutputPin(RaspiPin.GPIO_02, "LED_C", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_03, "LED_D", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_04, "LED_E", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_05, "LED_F", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_06, "LED_G", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_07, "LED_DP", PinState.HIGH)
        };

        for (GpioPinDigitalOutput pin :
                leds) {
            pin.setShutdownOptions(true);
        }

        GpioPinDigitalOutput[] parts = {
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_08, "LED_P1", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_09, "LED_P2", PinState.HIGH),
                controller.provisionDigitalOutputPin(RaspiPin.GPIO_10, "LED_P3", PinState.HIGH)
        };

        for (GpioPinDigitalOutput pin :
                parts) {
            pin.setShutdownOptions(true);
        }


        indicator.init(leds, parts);

        worker = new Thread(indicator);
    }

    public void setValue(int value) {
        indicator.setValue(value);
    }

    public void start() {
        worker.start();
    }

    public void stop() {
        worker.interrupt();
        try {
            wait(1000L);
            System.out.printf("Worker завершен!\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
