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
    private String username; // 겹치면 안된다네
    @Column(nullable = false)
    private String password; // BCrypt
    @CreationTimestamp
    private ZonedDateTime createAt = ZonedDateTime.now(ZoneOffset.UTC);
    @Column(nullable = false)
    private String role; // 보는 사람들이나 권한으로 느낌을 구분할 수 있겠다(User, Admin 이런거)
}
