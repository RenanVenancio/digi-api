package com.techzone.digi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {

//	@Autowired
//	private DBService dbService;
	
    @Bean
    public boolean initDatabase(){
        System.out.println("Perfil de Desenvolvimento!");
//    	dbService.initDatabase();
        return true;
    }
}
