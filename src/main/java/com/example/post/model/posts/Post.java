package com.example.post.model.posts;

import java.time.LocalDateTime;

import com.example.post.model.users.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Post {
	private Long id;					// 게시글 아이디
	private String title;				// 제목
	private String content;				// 내용
	private User user;					// 작성자
	private int views;					// 조회수
	private Attachment attachment;		// 첨부파일
	private LocalDateTime createTime;	// 작성일자
	
	// 조회수 증가
    public void incrementViews() {
        this.views++;
    }
}
