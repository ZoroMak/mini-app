package org.example.miniapp.entity;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class Status {
    private final String token;
    private final String status = "increased";

    public Status(String token){
        this.token = token;
    }
}
