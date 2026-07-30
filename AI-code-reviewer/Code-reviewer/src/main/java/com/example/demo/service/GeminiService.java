package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.GeminiResponse;
import com.example.demo.exception.GeminiServiceException;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String reviewCode(String code,String language) {

        try {

            String prompt = """
                    Review the following code. And give the solution in breif

                    Provide:
                    1. Bugs
                    2. Code Quality Issues
                    3. Performance Improvements
                    4. Best Practices
                    5. Refactored Version

                    Code:
                    """ + code +
                    """
                    Language used :
                    		"""+language;

            String url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                            + apiKey;

            Map<String, Object> body =
                    Map.of(
                            "contents",
                            List.of(
                                    Map.of(
                                            "parts",
                                            List.of(
                                                    Map.of(
                                                            "text",
                                                            prompt
                                                    )
                                            )
                                    )
                            )
                    );

            GeminiResponse response =
                    webClient.post()
                            .uri(url)
                            .bodyValue(body)
                            .retrieve()
                            .bodyToMono(GeminiResponse.class)
                            .block();

            if (response == null ||
                    response.getCandidates() == null ||
                    response.getCandidates().isEmpty()) {

                throw new GeminiServiceException(
                        "Gemini returned an empty response.");
            }

            return response.getCandidates()
                    .get(0)
                    .getContent()
                    .getParts()
                    .get(0)
                    .getText();

        } catch (Exception e) {

            throw new GeminiServiceException(
                    "Unable to connect to Gemini API. Please try again later.");
        }
    }
}