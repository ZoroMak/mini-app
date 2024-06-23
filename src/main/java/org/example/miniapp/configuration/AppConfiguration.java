package org.example.miniapp.configuration;



import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.example.miniapp.ApiCaller;
import org.example.miniapp.ApiService;
import org.example.miniapp.entity.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {
    @Bean
    @Scope("singleton")
    public Student student(){
        return new Student();
    }
    @Bean
    public RestTemplate restTemplate(){
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
    @Bean
    public ApiService apiService(RestTemplate restTemplate, Student student){
        return new ApiService(restTemplate, student);
    }

    @Bean
    public ApiCaller apiCaller(ApiService apiService){
        return new ApiCaller(apiService);
    }
}
