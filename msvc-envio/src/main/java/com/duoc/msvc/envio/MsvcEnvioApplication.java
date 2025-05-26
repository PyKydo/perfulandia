package com.duoc.msvc.envio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcEnvioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcEnvioApplication.class, args);
	}

}
