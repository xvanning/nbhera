package com.general;

import com.general.extension.boot.ExtensionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ExtensionConfiguration(value = "com.general")
public class NbheraDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbheraDemoApplication.class, args);
        System.out.println("======== NbheraDemoApplication ======");

    }

}
