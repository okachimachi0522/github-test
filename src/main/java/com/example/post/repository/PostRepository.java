package com.example.post.repository;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.example.post.model.posts.Attachment;
import com.example.post.model.posts.Post;

public interface PostRepository {
	// 게시글 저장
	void savePost(Post post);
	
	// 글 전체 조회
	List<Post> findAllPosts(RowBounds rowBounds);
	
	// 아이디로 글 상세 조회
	Post findPostById(Long postId);
	
	// 글 수정
	void updatePost(Post post);
	
	// 글 삭제
	void removePost(Long postId);
	
	// 첨부파일 조회
	Attachment findAttachment(Long postId);
	
	// 첨부파일 저장
	void saveAttachment(Attachment attachment);
	
	// 첨부파일 수정
	void updateAttachment(Attachment attachment);
	
	// 첨부파일 삭제
	void removeAttachment(Long attachmentId);
	
	// 전체 게시글 갯수
	int getTotal();
}









