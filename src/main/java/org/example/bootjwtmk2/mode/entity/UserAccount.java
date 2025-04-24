package org.example.bootjwtmk2.mode.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Entity //JPA
@Data //Lombok
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password; // BCrypt
    @CreationTimestamp
    private ZonedDateTime createAt = ZonedDateTime.now(ZoneOffset.UTC);
}
