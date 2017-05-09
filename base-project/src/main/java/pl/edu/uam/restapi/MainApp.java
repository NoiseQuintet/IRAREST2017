package pl.edu.uam.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.MetricFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration(exclude = {MetricFilterAutoConfiguration.class})
@Configuration
@ComponentScan
public class MainApp {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(new Class[]{MainApp.class, MainApplicationInitializer.class}, args);
  }
}
