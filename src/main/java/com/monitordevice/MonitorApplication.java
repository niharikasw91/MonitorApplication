package com.monitordevice;

import com.monitordevice.server.MonitorDevices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Main class to start the spring boot application and it starts the Monitoring Device server
 * @author Niharika Sweta
 */
@Slf4j
@SpringBootApplication
public class MonitorApplication {
   public static void main(String[] args) throws IOException {
    SpringApplication.run(MonitorApplication.class, args);
    log.info("Spring boot application");
    //Start the server
    MonitorDevices.getInstance().start();
  }
}