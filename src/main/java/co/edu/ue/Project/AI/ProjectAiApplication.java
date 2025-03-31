package co.edu.ue.Project.AI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"co.edu.ue.Project.AI.model"})
@EnableJpaRepositories(basePackages = {"co.edu.ue.Project.AI.repository"})
@ComponentScan(basePackages = {"co.edu.ue.Project.AI.service","co.edu.ue.Project.AI.controller","co.edu.ue.Project.AI.repository","co.edu.ue.Project.AI.security"})
public class ProjectAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAiApplication.class, args);
	}

}
