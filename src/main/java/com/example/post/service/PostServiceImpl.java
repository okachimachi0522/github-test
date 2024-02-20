package com.example.post.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.post.model.posts.Attachment;
import com.example.post.model.posts.Post;
import com.example.post.model.users.User;
import com.example.post.repository.PostRepository;
import com.example.post.util.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {
	private final PostRepository postRepository;
    private final FileService fileService;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Transactional
    @Override
    public Post savePost(Post post) {
        postRepository.savePost(post);
        Attachment attachment = post.getAttachment();
        if (attachment != null) {
            attachment.setPostId(post.getId());
            postRepository.saveAttachment(attachment);
        }
        return post;
    }

    @Override
    public List<Post> getAllPosts(int startRecord, int pagePerCount) {
    	RowBounds rowBounds = new RowBounds(startRecord, pagePerCount);
        return postRepository.findAllPosts(rowBounds);
    }
    
    @Override
    public Post readPost(Long postId) {
        Post post = postRepository.findPostById(postId);
        post.incrementViews();
        postRepository.updatePost(post);
        return post;
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findPostById(postId);
    }

    @Transactional
    @Override
    public void removePost(Long postId, User loginUser) {
        Post post = postRepository.findPostById(postId);
        if (post.getUser().getId().equals(loginUser.getId())) {
            if (post.getAttachment() != null) {
                String fullPath = uploadPath + "/" + post.getAttachment().getSavedFilename();
                fileService.deleteFile(fullPath);
                postRepository.removeAttachment(post.getAttachment().getId());
            }
            postRepository.removePost(postId);
        }
    }

    @Transactional
    @Override
    public Post updatePost(Post post) {
        Post findPost = postRepository.findPostById(post.getId());
        // 게시글 내용을 업데이트 한다.
        postRepository.updatePost(post);
        // 첨부파일이 있으면 첨부파일을 추가한다.
        if (post.getAttachment() != null) {
            postRepository.saveAttachment(post.getAttachment());
        }

        return post;
    }

    // 첨부파일 삭제
    @Transactional
    @Override
    public void deleteAttachment(Long postId, Long attachmentId, User loginUser) {
        Post findPost = postRepository.findPostById(postId);
        if (findPost != null && findPost.getUser().getId().equals(loginUser.getId())
                && findPost.getAttachment() != null && findPost.getAttachment().getId() == attachmentId) {
            String fullPath = uploadPath + "/" + findPost.getAttachment().getSavedFilename();
            fileService.deleteFile(fullPath);
            postRepository.removeAttachment(attachmentId);
        }
    }
        
        @Override
        public int getTotal() {
        return postRepository.getTotal();
        
    }

}









