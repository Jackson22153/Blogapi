package com.phucx.blogapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.blogapi.model.PostInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.phucx.blogapi.constant.PostStatus;



@Repository
public interface PostInfoRepository extends JpaRepository<PostInfo, Integer>{

    @Modifying
    @Transactional
    @Procedure("insertPost")
    void insertPost(String content, String title, String username, String img, LocalDate date, String category, String status);

    @Modifying
    @Transactional
    @Procedure("updatePost")
    void updatePost(Integer id, String title, String content, String img, String category, String status);

    @Modifying
    @Transactional
    @Procedure("deletePost")
    void deletePost(Integer postID);

    @Query("""
        SELECT p FROM PostInfo p \
        GROUP BY category, id
            """)
    List<PostInfo> findAllGroupByCategoryAndId();
    Page<PostInfo> findByCategoryOrderById(String category, Pageable pageable);
    Page<PostInfo> findByCategory(String category, Pageable pageable);
    @Query("""
        SELECT p FROM PostInfo p WHERE id IN ?1 \
        ORDER BY id DESC
            """)
    List<PostInfo> findAllByPostIdOrderByIdDesc(List<Integer> postIDs);
    
    List<PostInfo> findByUser(String user);
    Page<PostInfo> findByUser(String user, Pageable pageable);
    Optional<PostInfo> findByIdAndUser(Integer id, String user);
    @Query(value = """
        SELECT * FROM PostInfo \
        ORDER BY RAND() \
        LIMIT 4
            """, nativeQuery = true)
    List<PostInfo> findRandomPosts();
    Page<PostInfo> findByTitleLikeAndStatus(String title, PostStatus status, Pageable pageable);
    Page<PostInfo> findByTitleLike(String title, Pageable pageable);
    @Query("""
        SELECT p \
        FROM PostInfo p \
        WHERE p.category=?2 AND p.title LIKE ?1 AND p.status=?3 \
        GROUP BY p.id \
        ORDER BY p.id DESC
            """)
    Page<PostInfo> findByTitleLikeAndCategoryAndStatusOrderByPostIdDesc(String title, String category, PostStatus status, Pageable pageable);
    List<PostInfo> findByTitle(String title);
    Page<PostInfo> findByTitle(String title, Pageable pageable);
    Page<PostInfo> findByTitleLikeAndUser(String title, String user, Pageable pageable);

    @Query("""
        SELECT pi FROM PostInfo pi JOIN Post p on pi.id=p.id \
        WHERE p.categoryID=?1 \
        Order by pi.id DESC
            """)
    Page<PostInfo> findByCategoryOrderByIdDesc(Integer categoryID, Pageable pageable);

    @Query("""
        SELECT pi FROM PostInfo pi JOIN Post p on pi.id=p.id \
        WHERE p.categoryID=?1 AND p.status=?2 \
        Order by pi.id DESC
            """)
    Page<PostInfo> findByCategoryAndStatusOrderByIdDesc(Integer categoryID, PostStatus status, Pageable pageable);

    Optional<PostInfo> findByUserAndId(String user, Integer id);

    Page<PostInfo> findByUserAndCategory(String user, String category, Pageable pageable);

    Page<PostInfo> findByStatus(PostStatus status, Pageable pageable);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Post SET status=?2 WHERE id=?1
            """)
    Integer updatePostStatus(Integer id, PostStatus status);

    Optional<PostInfo> findByIdAndStatus(Integer id, PostStatus status);
}
