package br.edu.ufrn.tads.ecommercepw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan 
@SpringBootApplication
public class EcommercepwApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommercepwApplication.class, args);
	}

}
