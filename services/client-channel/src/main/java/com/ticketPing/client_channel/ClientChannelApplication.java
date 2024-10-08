package com.ticketPing.client_channel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@ComponentScan(basePackages = {"com.ticketPing.client_channel", "common"})
@SpringBootApplication
public class ClientChannelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientChannelApplication.class, args);
	}

}
