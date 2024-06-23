package org.example.miniapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Getter
@Component
public final class Student {
    @Value("${student.lastName}")
    private String last_name;
    @Value("${student.firstName}")
    private String first_name;
    @Value("${student.email}")
    private String email;
    @Setter
    private String role;
}
