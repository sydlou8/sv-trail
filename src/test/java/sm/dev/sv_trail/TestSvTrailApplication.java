package sm.dev.sv_trail;

import org.springframework.boot.SpringApplication;

public class TestSvTrailApplication {

	public static void main(String[] args) {
		SpringApplication.from(SvTrailApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
