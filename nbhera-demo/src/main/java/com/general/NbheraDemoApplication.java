package com.general;

import com.general.extension.boot.ExtensionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ExtensionConfiguration(value = "com.general")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class NbheraDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NbheraDemoApplication.class, args);
        System.out.println("======== NbheraDemoApplication ======");

    }

}
