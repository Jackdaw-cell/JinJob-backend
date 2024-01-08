package com.jackdaw.jinjobbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.jackdaw.jinjobbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.jackdaw")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.jackdaw.jinjobbackendserviceclient.service"})
public class JinjobBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JinjobBackendUserServiceApplication.class, args);
    }

}
