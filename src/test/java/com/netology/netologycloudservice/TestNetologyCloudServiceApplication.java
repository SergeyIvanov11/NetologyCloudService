package com.netology.netologycloudservice;

import org.springframework.boot.SpringApplication;

public class TestNetologyCloudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(NetologyCloudServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
