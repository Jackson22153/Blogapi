package com.phucx.blogapi.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Immutable;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phucx.blogapi.compositeKey.BookmarkPostID;
import com.phucx.blogapi.constant.PostStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Immutable
@Data @ToString
@IdClass(BookmarkPostID.class)
@Table(name = "bookmarkpostinfo")
public class BookmarkPostInfo {
    @Id
    private Integer id;
    private String img;
    private String title;
    private String content;
    private String category;
    @Id
    private String user;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate time;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
}
