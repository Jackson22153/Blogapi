package com.phucx.blogapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(Bookmark.class)
@Table(name = "bookmarks")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Bookmark.addPostToBookMarks", procedureName = "addPostToBookMarks",
        parameters = {
            @StoredProcedureParameter(name = "postID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "username", mode = ParameterMode.IN, type = String.class),
        }),
    @NamedStoredProcedureQuery(name = "Bookmark.removePostFromBookmarks", procedureName = "removePostFromBookmarks",
        parameters = {
            @StoredProcedureParameter(name = "postID", mode = ParameterMode.IN, type = Integer.class),
            @StoredProcedureParameter(name = "username", mode = ParameterMode.IN, type = String.class),
        }),
})
public class Bookmark {
    @Id
    private Integer postID;
    @Id
    private Integer userID;
}
