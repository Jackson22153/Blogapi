package com.phucx.blogapi.model;

import jakarta.persistence.Entity;
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
@Data @ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(name = "Category.addCategory", procedureName = "addCategory",
        parameters = {
            @StoredProcedureParameter(name="category", mode = ParameterMode.IN, type = String.class),
        })
})
public class Category {
    @Id
    private Integer id;
    private String category;
}
