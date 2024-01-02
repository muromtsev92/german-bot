package com.example.germanbot.model;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class User {
    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;

}
