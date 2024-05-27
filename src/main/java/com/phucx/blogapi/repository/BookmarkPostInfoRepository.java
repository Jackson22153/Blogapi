package com.phucx.blogapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.blogapi.compositeKey.BookmarkPostID;
import com.phucx.blogapi.model.BookmarkPostInfo;
import java.util.Optional;



@Repository
public interface BookmarkPostInfoRepository extends JpaRepository<BookmarkPostInfo, BookmarkPostID>{
    Page<BookmarkPostInfo> findByUser(String user, Pageable pageable);
    Optional<BookmarkPostInfo> findByIdAndUser(Integer id, String user);
}
