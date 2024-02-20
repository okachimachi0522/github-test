package com.example.post.service;

import java.util.List;

import com.example.post.model.posts.Post;
import com.example.post.model.users.User;

public interface PostService {
	// 게시글 등록
	Post savePost(Post post);
	
	// 글 전체 조회
	List<Post> getAllPosts(int startRecord, int pagePerCount);
	
	// 글 읽기
    Post readPost(Long postId);
	
	// 아이디로 글 조회
	Post getPostById(Long postId);
	
	// 글 삭제
	void removePost(Long postId, User loginUser);
	
	// 글 수정
	Post updatePost(Post post);
	
	// 첨부파일 삭제
    void deleteAttachment(Long postId, Long attachmentId, User loginUser);
    
    //게시글 전체 갯수
    int getTotal();
    
    
}
