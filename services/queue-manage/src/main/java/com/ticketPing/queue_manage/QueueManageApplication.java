package com.ticketPing.queue_manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ticketPing.queue_manage", "exception"})
public class QueueManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueManageApplication.class, args);
	}

}
