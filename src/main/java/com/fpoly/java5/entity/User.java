package com.fpoly.java5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(name = "fullname", nullable = false, length = 50)
    private String fullname;

    @Column(length = 50)
    private String email;

    private String image;

    @Column(name = "admin", nullable = false)
    private boolean admin;
}
