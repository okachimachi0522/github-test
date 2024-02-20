package com.example.post.model.posts;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostCreateForm {
	@NotBlank
	private String title;
	@NotBlank
	private String content;
}
