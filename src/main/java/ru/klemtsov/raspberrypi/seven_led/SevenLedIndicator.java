package ru.klemtsov.raspberrypi.seven_led;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import java.util.concurrent.atomic.AtomicInteger;

 class SevenLedIndicator implements Runnable {

    private final static int[] _0 = {
            1, 1, 1, 1, 1, 1, 0, 0
    };

    private final static int[] _1 = {
            0, 1, 1, 0, 0, 0, 0, 0
    };

    private final static int[] _2 = {
            1, 1, 0, 1, 1, 0, 1, 0
    };

    private final static int[] _3 = {
            1, 1, 1, 1, 0, 0, 1, 0
    };

    private final static int[] _4 = {
            0, 1, 1, 0, 0, 1, 1, 0
    };

    private final static int[] _5 = {
            1, 0, 1, 1, 0, 1, 1, 0
    };

    private final static int[] _6 = {
            1, 0, 1, 1, 1, 1, 1, 0
    };

    private final static int[] _7 = {
            1, 1, 1, 0, 0, 0, 0, 0
    };

    private final static int[] _8 = {
            1, 1, 1, 1, 1, 1, 1, 0
    };

    private final static int[] _9 = {
            1, 1, 1, 1, 0, 1, 1, 0
    };

    private static final int MAX_LED_SEGMENTS = 8;

    private GpioPinDigitalOutput[] pins;

    private GpioPinDigitalOutput[] digits;

    private AtomicInteger value;

    public void setValue(int value) {
        this.value.set(value);
    }

    public void init(final GpioPinDigitalOutput[] segments, final GpioPinDigitalOutput[] parts) {
        this.pins = segments;
        this.digits = parts;
        this.value = new AtomicInteger(0);
    }

    /**
     * Задаем цифру на индикаторе от 0 до 9
     *
     * @param indicator
     */
    public void setIndicator(int indicator) {
        int[] data;

        switch (indicator) {
            case 0:
                data = _0;
                break;
            case 1:
                data = _1;
                break;
            case 2:
                data = _2;
                break;
            case 3:
                data = _3;
                break;
            case 4:
                data = _4;
                break;
            case 5:
                data = _5;
                break;
            case 6:
                data = _6;
                break;
            case 7:
                data = _7;
                break;
            case 8:
                data = _8;
                break;
            case 9:
                data = _9;
                break;
            default:
                return;
        }

        int idx = 0;
        for (GpioPinDigitalOutput pin :
                pins) {
            pin.setState(data[idx] == 0 ? PinState.HIGH : PinState.LOW);
            idx++;
            if (idx > MAX_LED_SEGMENTS) {
                return;
            }
        }
    }

    private void clear(){
        for (GpioPinDigitalOutput pin :
                pins) {
            pin.setState(PinState.HIGH);
        }

    }



    private void setDigit(int digit){
        int d = 0;
        clear();
        switch (digit){
            case 0:
                digits[2].setState(PinState.HIGH);
                d = value.get() % 10;
                setIndicator(d);
                digits[0].setState(PinState.LOW);
                break;
            case 1:
                digits[0].setState(PinState.HIGH);
                d = value.get() % 10;
                setIndicator(d);
                digits[1].setState(PinState.LOW);
                break;
            case 2:
                digits[1].setState(PinState.HIGH);
                d = value.get() % 10;
                setIndicator(d);
                digits[2].setState(PinState.LOW);
                break;
        }
    }


    @Override
    public void run() {
        int d =0;
        while (true){
            if (Thread.interrupted()){
                System.out.printf("Поток завершен\n");
                break;
            }
            setDigit(value.get());
            d++;
            if (d > 2){
                d = 0;
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
