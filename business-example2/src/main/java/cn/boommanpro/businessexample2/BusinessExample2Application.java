package cn.boommanpro.businessexample2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class BusinessExample2Application {

    public static void main(String[] args) {
        SpringApplication.run(BusinessExample2Application.class, args);
    }

}
