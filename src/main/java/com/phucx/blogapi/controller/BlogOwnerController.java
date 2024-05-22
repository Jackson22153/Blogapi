package com.phucx.blogapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.blogapi.constant.WebConstant;
import com.phucx.blogapi.model.Bookmark;
import com.phucx.blogapi.model.BookmarkPostInfo;
import com.phucx.blogapi.model.PostInfo;
import com.phucx.blogapi.model.ResponseFormat;
import com.phucx.blogapi.service.post.PostService;
import com.phucx.blogapi.service.uploadFile.UploadFileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/blogOwner")
public class BlogOwnerController {
    @Autowired
    private PostService postService;
    @Autowired
    private UploadFileService uploadFileService;
    
    // bookmarks
    @GetMapping("/bookmarks/posts")
    public ResponseEntity<List<BookmarkPostInfo>> getBookmarksPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber, Authentication authentication){
        log.info("username: {}", authentication.getName());
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = authentication.getName();
        List<BookmarkPostInfo> posts = postService.getPostsInBookmarks(username, pageNumber, WebConstant.PAGE_SIZE);
 
        return ResponseEntity.ok().body(posts);
    }
    @PostMapping("/bookmarks/posts")
    public ResponseEntity<ResponseFormat> addPostToBookmarks(@RequestBody Bookmark bookmark, Authentication authentication){
        Boolean status = postService.addPostToBookmarks(bookmark.getPostID(), authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }
    @DeleteMapping("/bookmarks/posts")
    public ResponseEntity<ResponseFormat> removePostFromBookmarks(@RequestParam(name = "postID") Integer postID, Authentication authentication){
        Boolean status = postService.removePostFromBookmarks(postID, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }




    @GetMapping("/posts")
    public ResponseEntity<List<PostInfo>> getPosts(
        @RequestParam(name = "page", required = false) Integer pageNumber, Authentication authentication){
        log.info("username: {}", authentication.getName());
        pageNumber = pageNumber!=null?pageNumber:0;
        String username = authentication.getName();
        List<PostInfo> posts = postService.getPostsByUser(username, pageNumber, WebConstant.PAGE_SIZE);
 
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/posts/{postID}")
    public ResponseEntity<PostInfo> getPostDetail(
        @PathVariable Integer postID,
        Authentication authentication
    ){
        String username = authentication.getName();
        PostInfo post = postService.getPostByUser(username, postID);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping("/posts/search")
    public ResponseEntity<List<PostInfo>> searchPosts(
        @RequestParam(name = "l") String letter,
        @RequestParam(name = "page", required = false) Integer pagenumber,
        Authentication authentication
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        String username = authentication.getName();
        List<PostInfo> posts = postService.searchPostsOfUser(
            letter, username, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/posts/category/{category}")
    public ResponseEntity<List<PostInfo>> getPostsByCategory(
        @PathVariable String category,
        @RequestParam(name = "page", required = false) Integer pagenumber,
        Authentication authentication
    ){
        pagenumber = pagenumber!=null?pagenumber:0;
        String username = authentication.getName();
        List<PostInfo> posts = postService.getPostsOfUserByCategory(
            username, category, pagenumber, WebConstant.PAGE_SIZE);
        return ResponseEntity.ok().body(posts);
    }

    @PutMapping("/posts")
    public ResponseEntity<ResponseFormat> createPost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.addPost(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @PostMapping("/posts")
    public ResponseEntity<ResponseFormat> updatePost(@RequestBody PostInfo postInfo, Authentication authentication){
        Boolean status = postService.updatePostOfUser(postInfo, authentication.getName());
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile file){
        return ResponseEntity.ok().body(uploadFileService.uploadImage(file));
    }

}
