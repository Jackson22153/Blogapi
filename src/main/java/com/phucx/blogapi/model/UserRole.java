package com.phucx.blogapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserRole.class)
@Table(name = "userroles")
public class UserRole {
    @Id
    private Integer userID;
    @Id
    private Integer roleID;
}
