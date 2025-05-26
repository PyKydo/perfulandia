package com.duoc.msvc.pedido;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcPedidoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcPedidoApplication.class, args);
	}

}
