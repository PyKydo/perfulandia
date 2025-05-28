package com.duoc.msvc.pago;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcPagoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcPagoApplication.class, args);
	}
}
