package springclient.apicall;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class ApicallApplication {

	private static final String DESTINATION = "https://open.er-api.com/v6/latest";

	private final ErApi erApi;

	public ApicallApplication(ErApi erApi) {
		this.erApi = erApi;
	}

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

			// HttpInterface 예제
			Map hiResponse = erApi.getLatest();
			printResponse(hiResponse, "httpInterface");
		};
	}

	@Configuration
	static class Config {
		@Bean
		WebClient webClient() {
			return WebClient.create();
		}
		@Bean
		HttpServiceProxyFactory httpServiceProxyFactory() {
			return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient())).build();
		}
		@Bean
		ErApi erApi() {
			return httpServiceProxyFactory().createClient(ErApi.class);
		}
	}

	@HttpExchange(url = DESTINATION)
	interface ErApi {
		@GetExchange
		Map getLatest();
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
