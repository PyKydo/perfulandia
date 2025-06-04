package com.duoc.msvc.usuario.init;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class LoadDatabase {
}
