package com.example.post.model.posts;

import java.time.LocalDateTime;

public class PostConverter {
	public static Post postCreateFormToPost(PostCreateForm postCreateForm) {
		Post post = new Post();
		post.setTitle(postCreateForm.getTitle());
		post.setContent(postCreateForm.getContent());
		post.setCreateTime(LocalDateTime.now());
		return post;
	}
	
	public static PostEditForm postToPostEditForm(Post post) {
		PostEditForm postEditForm = new PostEditForm();
		postEditForm.setId(post.getId());
		postEditForm.setTitle(post.getTitle());
		postEditForm.setContent(post.getContent());
		postEditForm.setUser(post.getUser());
		postEditForm.setViews(post.getViews());
		postEditForm.setAttachment(post.getAttachment());
		postEditForm.setCreateTime(post.getCreateTime());
		return postEditForm;
	}
	
	public static Post postEditFormToPost(PostEditForm postEditForm) {
		Post post = new Post();
		post.setId(postEditForm.getId());
		post.setTitle(postEditForm.getTitle());
		post.setContent(postEditForm.getContent());
		return post;
	}
}
