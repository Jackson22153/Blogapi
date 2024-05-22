package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.blogapi.model.Bookmark;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Bookmark>{
    @Modifying
    @Transactional
    @Procedure(name = "addPostToBookMarks")
    void addPostToBookMarks(Integer postID, String username);

    @Modifying
    @Transactional
    @Procedure(name = "removePostFromBookmarks")
    void removePostFromBookmarks(Integer postID, String username);
}
