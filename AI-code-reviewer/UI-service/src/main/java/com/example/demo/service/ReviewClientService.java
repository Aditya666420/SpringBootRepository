package com.example.demo.service;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.dto.DashboardResponse;
import com.example.demo.dto.ErrorResponse;
import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReviewClientService {

//    private final WebClient webClient =
//            WebClient.builder().build();
	
	@Autowired
	private WebClient webClient;

    @Value("${review.service.url}")
    private String reviewServiceUrl;

    public ReviewResponse reviewCode(String code,String language,String token) throws JsonMappingException, JsonProcessingException {

        ReviewRequest request = new ReviewRequest();

       
        request.setCode(code);
        request.setLanguage(language);

        try {
        	String reviewUri=reviewServiceUrl+"api/reviews";
        	System.out.println("Review uri : "+reviewUri);
            return  webClient.post()
                            .uri(reviewUri)
                            .header(HttpHeaders.AUTHORIZATION, token)
                            .bodyValue(request)
                            .retrieve()
                            .bodyToMono(ReviewResponse.class)
                            .block();

            

        } catch (WebClientResponseException ex) {

        	  ObjectMapper mapper =
        	            new ObjectMapper();

        	    ErrorResponse error =
        	            mapper.readValue(
        	                    ex.getResponseBodyAsString(),
        	                    ErrorResponse.class);
        	    String errorText = """
        	            ERROR

        	            %s

        	            %s
        	            """
        	            .formatted(
        	                    error.getError(),
        	                    error.getMessage());
        	    ReviewResponse resp = new ReviewResponse();
        	    resp.setReview(errorText);

        	    return resp;

        } catch (Exception ex) {

        	  ObjectMapper mapper =
        	            new ObjectMapper();

        	    ErrorResponse error =
        	            mapper.readValue(
        	                    ((WebClientResponseException) ex).getResponseBodyAsString(),
        	                    ErrorResponse.class);

        	    String errorText = """
        	            ERROR

        	            %s

        	            %s
        	            """
        	            .formatted(
        	                    error.getError(),
        	                    error.getMessage());
        	    ReviewResponse resp = new ReviewResponse();
        	    resp.setReview(errorText);

        	    return resp;
        }
    }
    
    public List<DashboardResponse> getResponseForALL(){
    	
    
   
    	
	    	try {
	        	String reviewUri=reviewServiceUrl+"api/reviews";
	
	            List<DashboardResponse> resp =
	                    webClient.get()
	                            .uri(reviewUri)
	                            
	                            .retrieve()
	                            .bodyToFlux(DashboardResponse.class)
	                            .collectList()
	                            .block();
	                            
	
	            return resp;
	
	        } catch (WebClientResponseException ex) {
	
	        	  
	        	  ex.printStackTrace();
	        	  return Collections.emptyList();
	    	
	    }
	    	 catch (Exception ex) {

	    		    ex.printStackTrace();
	    		    return Collections.emptyList();
	    		}
    }
    public List<DashboardResponse> getResponseForUser(String token){
    	
        
    	   
    	
    	try {
        	String reviewUri=reviewServiceUrl+"api/reviews"+"/userReview";

            List<DashboardResponse> resp =
                    webClient.get()
                            .uri(reviewUri)
                            .header(HttpHeaders.AUTHORIZATION,  token)
                            .retrieve()
                            .bodyToFlux(DashboardResponse.class)
                            .collectList()
                            .block();
                            

            return resp;

        } catch (WebClientResponseException ex) {

        	  
        	  ex.printStackTrace();
        	  return Collections.emptyList();
    	
    }
    	 catch (Exception ex) {

    		    ex.printStackTrace();
    		    return Collections.emptyList();
    		}
}
    public List<DashboardResponse> getResponseForALLByUserId(String token){
    	
        
    	   
    	
    	try {
        	String reviewUri=reviewServiceUrl+"api/reviews/userReview";

            List<DashboardResponse> resp =
                    webClient.get()
                            .uri(reviewUri)
                            .header(HttpHeaders.AUTHORIZATION,token)
                            .retrieve()
                            .bodyToFlux(DashboardResponse.class)
                            .collectList()
                            .block();
                            

            return resp;

        } catch (WebClientResponseException ex) {

        	  
        	  ex.printStackTrace();
        	  return Collections.emptyList();
    	
    }
    	 catch (Exception ex) {

    		    ex.printStackTrace();
    		    return Collections.emptyList();
    		}
}
    public DashboardResponse getResponseForId(String id,String token){
    	
        
    	   
    	
    	try {
        	String reviewUri=reviewServiceUrl+"api/reviews"+"/" +id;

            DashboardResponse resp =
                    webClient.get()
                            .uri(reviewUri)
                            .header(HttpHeaders.AUTHORIZATION,token)
                            .retrieve()
                            .bodyToMono(DashboardResponse.class)  // mono is for one object
                            .block();
                            

            return resp;

        } catch (WebClientResponseException ex) {

        	  
        	  ex.printStackTrace();
        	  return null;
    	
    }
    	 catch (Exception ex) {

    		    ex.printStackTrace();
    		    return null;
    		}
    }
    
 public String deleteForId(String id,String token){
    	
        System.out.println("Delete token : "+token);
    	   
    	
    	try {
        	String reviewUri=reviewServiceUrl+"api/reviews"+"/" +id;

            String resp =
                    webClient.delete()
                            .uri(reviewUri)
                            .header(HttpHeaders.AUTHORIZATION,token)
                            .retrieve()
                            .bodyToMono(String.class)  // mono is for one object
                            .block();
                            

            return resp;

        } catch (WebClientResponseException ex) {

        	  
        	  ex.printStackTrace();
        	  return null;
    	
    }
    	 catch (Exception ex) {

    		    ex.printStackTrace();
    		    return null;
    		}
}
}
