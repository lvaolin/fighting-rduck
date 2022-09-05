package com.dhy.duck.clientspring;

import com.dhy.duck.anntation.Duck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Duck(enable = true,scanBasePackage = "com.dhy.server.itf")
public class ClientSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientSpringApplication.class, args);
    }

}
