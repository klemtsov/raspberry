package ru.klemtsov.raspberrypi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class RaspberryPiApplication {


	public static void main(String[] args) {


		SpringApplication springApplication = new SpringApplication(RaspberryPiApplication.class);
		springApplication.addListeners(new ApplicationListener<ContextClosedEvent>() {
			@Override
			public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
				Triggers triggers =  contextClosedEvent.getApplicationContext().getBean(Triggers.class);
				System.out.println("SHUTDOWN gpioController...");
				triggers.getGpioController().shutdown();
			}
		});
		springApplication.addListeners( new ApplicationListener<ContextRefreshedEvent>() {
			@Override
			public void onApplicationEvent(ContextRefreshedEvent applicationEvent) {
                System.out.println("refreshed event");
			}
		});
		springApplication.run(args);
	}
}
