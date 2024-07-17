package com.phucx.blogapi.service.post;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.springframework.data.domain.Page;

import com.phucx.blogapi.constant.PostStatus;
import com.phucx.blogapi.model.BookmarkPostInfo;
import com.phucx.blogapi.model.Post;
import com.phucx.blogapi.model.PostInfo;

public interface PostService {
    // get postinfo
    public Page<PostInfo> getAllPosts(int pageNumber, int pageSize);
    public List<Post> getPosts(List<Integer> postIDs);
    public Page<PostInfo> getPostsByUser(String username, int pageNumber, int pageSize);
    public PostInfo getPostByUser(String username, int postID);
    public Page<PostInfo> getPostsOfUserByCategory(String username, String category, int pageNumber, int pageSize);
    public PostInfo getPostInfo(int postID) throws NameNotFoundException;
    public List<PostInfo> getPostsRandomly();
    // search posts
    public Page<PostInfo> searchPostsByStatus(String postTitle, PostStatus status, int pageNumber, int pageSize);
    public Page<PostInfo> searchPostsByCategoryAndStatus(String postTitle, String category, PostStatus status, int pageNumber, int pageSize);
    public Page<PostInfo> searchPostsOfUser(String postTitle, String username, int pageNumber, int pageSize);
    public Page<PostInfo> searchAllPosts(String postTitle, int pageNumber, int pageSize);
    // add new post
    public Boolean addPost(PostInfo post, String username);
    // update post
    public Boolean updatePost(PostInfo post);
    public Boolean updatePostOfUser(PostInfo post, String username);
    // confirm and cancel post
    public Boolean confirmPost(PostInfo post) throws NameNotFoundException;
    public Boolean cancelPost(PostInfo post) throws NameNotFoundException;
    // get post by category
    public Page<PostInfo> getPostsByCategory(String category, int pageNumber, int pageSize);
    public Page<PostInfo> getPostsByCategoryAndStatus(String category, PostStatus status, int pageNumber, int pageSize);
    // delete post
    public Boolean deletePost(Integer postID, String username) throws NameNotFoundException;
    public Boolean deletePost(Integer postID) throws NameNotFoundException;
    // get posts by status
    public Page<PostInfo> getPostsByStatus(PostStatus status, int pageNumber, int pageSize);
    public PostInfo getPostByStatus(Integer postID, PostStatus status) throws NameNotFoundException;
    // bookmars
    public Boolean addPostToBookmarks(Integer postID, String username);
    public Boolean removePostFromBookmarks(Integer postID, String username);
    public Page<BookmarkPostInfo> getPostsInBookmarks(String username, int pageNumber, int pageSize);
    public Boolean isInBookmarks(Integer postID, String username);
}
