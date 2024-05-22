package com.phucx.blogapi.model;

import org.springframework.data.annotation.Immutable;

import com.phucx.blogapi.compositeKey.UserRoleID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@ToString
@Immutable
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserRoleID.class)
@Table(name = "usersrolesinfo")
@NamedStoredProcedureQuery(name = "UserRoleInfo.addUserDetail", procedureName = "addUserDetail",
    parameters = {
        @StoredProcedureParameter(name="username", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="password", type = String.class, mode = ParameterMode.IN),
        @StoredProcedureParameter(name="roleIDs_text", type = String.class, mode = ParameterMode.IN),
    })
public class UserRoleInfo {
    @Id
    private Integer id;
    private String username;
    @Id
    private String role;
}
