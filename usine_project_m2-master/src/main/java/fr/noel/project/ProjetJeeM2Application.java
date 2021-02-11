package fr.noel.project;

import fr.noel.project.service.impl.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class ProjetJeeM2Application  {

	public static void main(String[] args) {
		SpringApplication.run(ProjetJeeM2Application.class, args);
	}

}
