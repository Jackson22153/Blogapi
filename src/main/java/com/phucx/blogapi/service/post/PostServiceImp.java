package com.phucx.blogapi.service.post;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.blogapi.constant.PostStatus;
import com.phucx.blogapi.model.BookmarkPostInfo;
import com.phucx.blogapi.model.Category;
import com.phucx.blogapi.model.Post;
import com.phucx.blogapi.model.PostInfo;
import com.phucx.blogapi.model.User;
import com.phucx.blogapi.repository.BookmarkPostInfoRepository;
import com.phucx.blogapi.repository.BookmarkRepository;
import com.phucx.blogapi.repository.PostInfoRepository;
import com.phucx.blogapi.repository.PostRepository;
import com.phucx.blogapi.service.category.CategoryService;
import com.phucx.blogapi.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImp implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostInfoRepository postInfoRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private BookmarkPostInfoRepository bookmarkPostInfoRepository;

    @Override
    public Page<PostInfo> getAllPosts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findAll(pageable);
        return posts;
    }

    @Override
    public List<Post> getPosts(List<Integer> postIDs) {
        return postRepository.findAllById(postIDs);
    }

    @Override
    public Page<PostInfo> getPostsByUser(String user, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByUser(user, pageable);
        return posts;
    }
    @Override
    public PostInfo getPostByUser(String user, int postID) {
        return postInfoRepository.findByIdAndUser(postID, user)
            .orElseThrow(()-> new RuntimeException("Post "+ postID + " of User "+ user + " does not found"));
    }

    @Override
    public List<PostInfo> getPostsRandomly() {
        List<PostInfo> posts = postInfoRepository.findRandomPosts();
        return posts;
    }

    @Override
    public Page<PostInfo> searchPostsByStatus(String postTitle, PostStatus status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByTitleLikeAndStatus("%"+postTitle+"%", status, pageable);
        return posts;
    }

    @Override
    public Page<PostInfo> searchPostsOfUser(String postTitle, String user, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByTitleLikeAndUser("%"+postTitle+"%", user, pageable);
        return posts;
    }

    @Override
    public Page<PostInfo> searchAllPosts(String postTitle, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByTitleLike("%"+postTitle+"%", pageable);
        return posts;
    }

    @Override
    @Transactional
    public Boolean addPost(PostInfo post, String username) {
        log.info("addPost(post={}, username={})", post, username);
        User user = userService.getUserByUsername(username);
        LocalDate time = LocalDate.now();
        try {
            postInfoRepository.insertPost(post.getContent(), post.getTitle(), user.getUsername(), 
                post.getImg(), time, post.getCategory(), PostStatus.Pending.name());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Page<PostInfo> getPostsByCategory(String category, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Category fetchedCategory = categoryService.getCategory(category);
        Page<PostInfo> posts = postInfoRepository.findByCategoryOrderByIdDesc(fetchedCategory.getId(), pageable);
        return posts;
    } 


    @Override
    public Page<PostInfo> searchPostsByCategoryAndStatus(
        String postTitle, String category, PostStatus status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByTitleLikeAndCategoryAndStatusOrderByPostIdDesc(
            "%" + postTitle + "%", category, status, pageable);
        return posts;
    }

    @Override
    public Boolean updatePost(PostInfo post) {
        log.info("updatePost({})", post);
        try {
            PostInfo fetchedPost = postInfoRepository.findById(post.getId())
                .orElseThrow(()-> new NameNotFoundException("Post " + post.getId() + " does not found"));
            postInfoRepository.updatePost(fetchedPost.getId(), post.getTitle(), post.getContent(), 
                post.getImg(), post.getCategory(), PostStatus.Pending.name());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PostInfo getPostInfo(int postID) throws NameNotFoundException {
        return postInfoRepository.findById(postID)
            .orElseThrow(()-> new NameNotFoundException("Post "+postID+" does not found"));
    }

    @Override
    public Boolean updatePostOfUser(PostInfo post, String username) {
        log.info("updatePostOfUser(post={}, username={})", post, username);
        try {
            PostInfo fetchedPost = postInfoRepository.findByUserAndId(username, post.getId())
                .orElseThrow(()-> new NameNotFoundException("Post " + post.getId() + " of user "+username+" does not found"));
            postInfoRepository.updatePost(fetchedPost.getId(), post.getTitle(), post.getContent(), 
                post.getImg(), post.getCategory(), PostStatus.Pending.name());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Page<PostInfo> getPostsOfUserByCategory(String username, String category, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByUserAndCategory(username, category, pageable);
        return posts;
    }

    @Override
    public Page<PostInfo> getPostsByStatus(PostStatus status, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostInfo> posts = postInfoRepository.findByStatus(status, pageable);
        return posts;
    }


    private Boolean updatePostStatus(PostInfo post) throws NameNotFoundException {
        log.info("updatePostStatus(postID={}, status={})", post.getId(), post.getStatus().name());
        PostInfo fetchedPost = postInfoRepository.findById(post.getId())
            .orElseThrow(()-> new NameNotFoundException("Post "+post.getId()+"does not found"));
        if(fetchedPost.getStatus().name().equalsIgnoreCase(post.getStatus().name()))
            throw new RuntimeException("Post "+post.getId()+" can not be updated its status");
        Integer check = postInfoRepository.updatePostStatus(fetchedPost.getId(), post.getStatus());
        if(check>0) return true;
        return false;
    }

    @Override
    public Boolean confirmPost(PostInfo post) throws NameNotFoundException {
        log.info("confirmPost(postID={})", post.getId());
        post.setStatus(PostStatus.Successful);
        return this.updatePostStatus(post);
    }

    @Override
    public Boolean cancelPost(PostInfo post) throws NameNotFoundException {
        log.info("cancelPost(postID={})", post.getId());
        post.setStatus(PostStatus.Canceled);
        return this.updatePostStatus(post);
    }

    @Override
    public PostInfo getPostByStatus(Integer postID, PostStatus status) throws NameNotFoundException {
        return postInfoRepository.findByIdAndStatus(postID, status)
            .orElseThrow(()-> new NameNotFoundException("Post " + postID + " does not found"));
    }

    @Override
    public Page<PostInfo> getPostsByCategoryAndStatus(String category, PostStatus status, int pageNumber,
            int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Category fetchedCategory = categoryService.getCategory(category);
        Page<PostInfo> posts = postInfoRepository.findByCategoryAndStatusOrderByIdDesc(
            fetchedCategory.getId(), status, pageable);
        return posts;
    }

    @Override
    public Boolean addPostToBookmarks(Integer postID, String username) {
        log.info("addPostToBookmarks(postID={}, username={})", postID, username);
        try {
            Optional<BookmarkPostInfo> postInfoOp = bookmarkPostInfoRepository.findByIdAndUser(postID, username);
            if(postInfoOp.isEmpty()){
                bookmarkRepository.addPostToBookMarks(postID, username);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean removePostFromBookmarks(Integer postID, String username) {
        log.info("removePostFromBookmarks(postID={}, username={})", postID, username);
        try {
            Optional<BookmarkPostInfo> postInfoOp = bookmarkPostInfoRepository.findByIdAndUser(postID, username);
            if(postInfoOp.isPresent()){
                bookmarkRepository.removePostFromBookmarks(postID, username);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Page<BookmarkPostInfo> getPostsInBookmarks(String username, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<BookmarkPostInfo> posts = bookmarkPostInfoRepository.findByUser(username, pageable);
        return posts;
    }

    @Override
    public Boolean isInBookmarks(Integer postID, String username) {
        Optional<BookmarkPostInfo> post = bookmarkPostInfoRepository.findByIdAndUser(postID, username);
        return post.isPresent();
    }

    @Override
    public Boolean deletePost(Integer postID, String username) throws NameNotFoundException {
        log.info("deletePost(postID={}, username={})", postID, username);
        PostInfo fetchedPostInfo = postInfoRepository.findByIdAndUser(postID, username)
            .orElseThrow(()-> new NameNotFoundException("Post "+postID+" of user "+username+"does not found"));
        try {
            postInfoRepository.deletePost(fetchedPostInfo.getId());
            return true;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean deletePost(Integer postID) throws NameNotFoundException {
        log.info("deletePost(postID={})", postID);
        PostInfo fetchedPostInfo = postInfoRepository.findById(postID)
            .orElseThrow(()-> new NameNotFoundException("Post "+postID+" does not found"));
        try {
            postInfoRepository.deletePost(fetchedPostInfo.getId());
            return true;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            return false;
        }
    }
}
