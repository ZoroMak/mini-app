package org.example.miniapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.miniapp.entity.Role;
import org.example.miniapp.entity.Status;
import org.example.miniapp.entity.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApiService {

    private final RestTemplate restTemplate;
    private final Student student;
    @Value("${base.url}")
    private String baseUrl;

    @Value("${api.getRoles.endpoint}")
    private String getRolesEndpoint;
    @Value("${api.signUp.endpoint}")
    private String signUpEndpoint;
    @Value("${api.getCode.endpoint}")
    private String getCodeEndpoint;
    @Value("${api.setStatus.endpoint}")
    private String setStatusEndpoint;

    public Role getRoles() {
        try {
            ResponseEntity<Role> responseEntity = restTemplate.getForEntity(baseUrl + getRolesEndpoint, Role.class);
            log.info("Список ролей получен");
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Ошибка получения ролей");
        }
    }

    public void signUp(Role roles) throws JsonProcessingException {
        student.setRole(roles.roles().get(1));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = new ObjectMapper().writeValueAsString(student);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    baseUrl + signUpEndpoint,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            log.info("Ответ сервера: " + responseEntity.getBody());
        }catch (HttpClientErrorException ex){
            throw new RuntimeException("Ошибка при записи в таблицу кандидатов");
        }
    }

    public String getCode() {
        String email = student.getEmail();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + getCodeEndpoint)
                .queryParam("email", email);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(builder.toUriString(), String.class);
            log.info("Код получен");
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("Ошибка получения кода");
        }
    }

    public void setStatus(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        code = validCode(code);
        String token = encodeEmailAndCode(student.getEmail(), code);
        Status status = new Status(token);
        String jsonBody = new ObjectMapper().writeValueAsString(status);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    baseUrl + setStatusEndpoint,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            log.info("Ответ сервера: " + responseEntity.getBody());
        }catch (HttpClientErrorException ex){
            throw new RuntimeException("Ошибка установки статуса записи в таблицу кандидатов");
        }
    }
    private static String validCode(String code){
        return code.substring(1, code.length() - 1);
    }
    private static String encodeEmailAndCode(String email, String code) {
        String combined = email + ":" + code;
        return Base64.getEncoder().encodeToString(combined.getBytes());
    }
}
