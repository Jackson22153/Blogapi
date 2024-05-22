package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.phucx.blogapi.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

}
