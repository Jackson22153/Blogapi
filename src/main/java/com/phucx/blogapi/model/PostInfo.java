package com.phucx.blogapi.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.blogapi.constant.PostStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Immutable
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "postinfo")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "PostInfo.insertPost", procedureName = "insertPost",
        parameters = {
            @StoredProcedureParameter(name="content", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="title", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="username", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="img", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="date", mode = ParameterMode.IN, type = LocalDate.class),
            @StoredProcedureParameter(name="category", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
        }),
    @NamedStoredProcedureQuery(name = "PostInfo.updatePost", procedureName = "updatePost",
        parameters = {
            @StoredProcedureParameter(name="id", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name="title", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="content", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="img", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="category", mode = ParameterMode.IN, type = String.class),
            @StoredProcedureParameter(name="status", mode = ParameterMode.IN, type = String.class),
        }),
})
public class PostInfo {
    @Id
    private Integer id;
    private String img;
    private String title;
    private String content;
    private String category;
    private String user;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
}
