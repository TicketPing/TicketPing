package com.ticketPing.queue_manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@ComponentScan(basePackages = {"com.ticketPing.queue_manage", "exception", "aop"})
@SpringBootApplication
public class QueueManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueManageApplication.class, args);
	}

}
