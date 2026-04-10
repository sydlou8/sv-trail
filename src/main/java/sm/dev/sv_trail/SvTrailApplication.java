package sm.dev.sv_trail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SvTrailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SvTrailApplication.class, args);
	}

}
