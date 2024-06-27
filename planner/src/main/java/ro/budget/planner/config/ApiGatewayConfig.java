package ro.budget.planner.config;

import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class ApiGatewayConfig {

    private final String apiUrl = "https://w68n9wulv8.execute-api.us-east-1.amazonaws.com/BudgetStage";

    private final RestTemplate restTemplate;

    public ApiGatewayConfig(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void subscription(String email) {
        String requestBody = "{\"email\": \"" + email + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/subscription", HttpMethod.POST, requestEntity, String.class);
        System.out.println("Response from subscription API: " + response.getBody());
    }

    public void publishMessage() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl + "/publish", HttpMethod.POST, requestEntity, String.class);
        System.out.println("Response from publish API: " + response.getBody());
    }

    @Scheduled(cron = "0 30 9 ? * THU")  // Rulează metoda în fiecare joi la 9:30 AM
    public void scheduledPublishMessage() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Executing scheduledPublishMessage() at: " + now);
        publishMessage();
    }
}
