package springclient.apicall;

import java.util.Map;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApicallApplication {

	private static final String DESTINATION = "https://open.er-api.com/v6/latest";

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			RestTemplate rt = new RestTemplate();
			Map<String, Map<String, Object>> rtResponse = rt.getForObject(DESTINATION, Map.class);

			System.out.println("==========");
			System.out.println(rtResponse);
			System.out.println(rtResponse.get("base_code"));
			System.out.println(rtResponse.get("rates").get("KRW"));


		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ApicallApplication.class, args);
	}

}
