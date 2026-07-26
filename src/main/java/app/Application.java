package app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean()
	CommandLineRunner commandLineRunner(){
		return args -> {
			System.out.println("Server Running on http://localhost:5000");
			System.out.println("Server Running on http://localhost:5000/swagger-ui.html");
		};
	}

}
