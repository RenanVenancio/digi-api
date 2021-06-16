package com.techzone.digi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.techzone.digi.service.DBService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbService;
	
    @Bean
    public boolean initDatabase(){
        System.out.println("Perfil de Testes Ativado!");
    	dbService.initDatabase();
        return true;
    }
}
