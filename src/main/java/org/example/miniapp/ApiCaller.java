package org.example.miniapp;

import lombok.RequiredArgsConstructor;
import org.example.miniapp.entity.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiCaller {

    private final ApiService apiService;

    public void callAllApis() {
        try {
            Role roles = apiService.getRoles();
            apiService.signUp(roles);
            String code = apiService.getCode();
            apiService.setStatus(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
