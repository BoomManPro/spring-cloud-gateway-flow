package cn.boommanpro.unifygateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @author wangqimeng
 * @date 2019
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class UnifyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnifyGatewayApplication.class, args);
    }

}
