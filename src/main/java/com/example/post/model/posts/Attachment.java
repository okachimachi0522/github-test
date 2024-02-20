package com.example.post.model.posts;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Attachment {
	private Long id;
	private Long postId;
	private String originalFilename;
	private String savedFilename;
	private long fileSize;
	private LocalDateTime uploadDate;
	
	public Attachment(String originalFilename, String savedFilename, long fileSize) {
		this.originalFilename = originalFilename;
		this.savedFilename = savedFilename;
		this.fileSize = fileSize;
		this.uploadDate = LocalDateTime.now();
	}
}



