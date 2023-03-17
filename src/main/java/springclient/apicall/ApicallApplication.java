package springclient.apicall;

import java.util.Map;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ApicallApplication {

	private static final String DESTINATION = "https://open.er-api.com/v6/latest";

	@Bean
	ApplicationRunner applicationRunner() {
		return args -> {
			// RestTemplate 예제
			RestTemplate rt = new RestTemplate();
			Map<String, Map<String, Object>> rtResponse = rt.getForObject(DESTINATION, Map.class);

			printResponse(rtResponse, "restTemplate");

			// WebClient 예제
			WebClient wc = WebClient.create();
			Map<String, Map<String, Object>> wcResponse = wc.get().uri(DESTINATION).retrieve().bodyToMono(Map.class).block();

			printResponse(wcResponse, "webClient");


		};
	}

	private void printResponse(Map<String, Map<String, Object>> response, String client) {
		System.out.println();
		System.out.println("==========" + client + "==========");
//		System.out.println(response);
//		System.out.println(response.get("base_code"));
		System.out.println("달러 대비 원화 : " + response.get("rates").get("KRW"));
		System.out.println("==========================");
	}

	public static void main(String[] args) {
		SpringApplication.run(ApicallApplication.class, args);
	}

}
