package com.example.post.controller;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import com.example.post.model.posts.Attachment;
import com.example.post.model.posts.Post;
import com.example.post.model.posts.PostConverter;
import com.example.post.model.posts.PostCreateForm;
import com.example.post.model.posts.PostEditForm;
import com.example.post.model.users.User;
import com.example.post.service.PostService;
import com.example.post.util.FileService;
import com.example.post.util.PageNavigator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("posts")
@RequiredArgsConstructor
@Controller
public class PostController {
	
	private final PostService postService;
	private final FileService fileService;
	
	private int countPerPage = 10; //페이지 당 게시글 수
	private int pagePerGroup = 5; //페이지 이동 그룹 당 표시할 페이지 수
	
	@Value("${file.upload.path}")
    private String uploadPath;
	
	// 게시글 목록 조회
	@GetMapping
	public String listPosts(@RequestParam(name = "page", defaultValue = "1") int page,
							Model model) {
		int  total = postService.getTotal();
		PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total);
		List<Post> posts = postService.getAllPosts(navi.getStartRecord(), navi.getCountPerPage());
		model.addAttribute("posts", posts);
		model.addAttribute("navi", navi);
		
		return "posts/list";
	}
	
	// 게시글 상세 조회
	@GetMapping("{postId}")
	public String viewPost(@PathVariable(name = "postId") Long postId,
						   Model model) {
		Post post = postService.readPost(postId);
		model.addAttribute("post", post);
		
		return "posts/view";
	}
	
	// 게시글 작성 폼
	@GetMapping("create")
	public String createPostForm(@SessionAttribute(name = "loginUser", required = false) User loginUser,
								 Model model) {
		model.addAttribute("postCreateForm", new PostCreateForm());
		return "posts/create";
	}
	
	// 게시글 작성 처리
	@PostMapping("create")
	public String createPost(@SessionAttribute(name = "loginUser", required = false) User loginUser,
							 @Validated @ModelAttribute PostCreateForm postCreateForm,
							 BindingResult bindingResult,
							 @RequestParam(name = "file", required = false) MultipartFile file) {
		if (bindingResult.hasErrors()) {
			return "posts/create";
		}
		
		Post post = PostConverter.postCreateFormToPost(postCreateForm);
		post.setUser(loginUser);
		
		if (file != null && file.getSize() > 0) {
            Attachment attachment = fileService.saveFile(file);
            post.setAttachment(attachment);
        }
		
		postService.savePost(post);
	
		return "redirect:/posts";
	}
	
	// 게시글 삭제
	@GetMapping("remove/{postId}")
	public String removePost(@SessionAttribute(name = "loginUser", required = false) User loginUser,
							 @PathVariable(name = "postId") Long postId) {
		postService.removePost(postId, loginUser);
		return "redirect:/posts";
	}
	
	// 게시글 수정 폼
	@GetMapping("edit/{postId}")
	public String editPostForm(@SessionAttribute(name = "loginUser", required = false) User loginUser,
							   @PathVariable(name = "postId") Long postId,
							   Model model) {
		Post post = postService.getPostById(postId);
		if (post == null || !post.getUser().getId().equals(loginUser.getId())) {
			log.info("수정 권한 없음");
			return "redirect:/posts";
		}
		
		PostEditForm postEditForm = PostConverter.postToPostEditForm(post);
		model.addAttribute("postEditForm", postEditForm);
		return "posts/edit";
	}
	
	// 게시글 수정
	@PostMapping("edit/{postId}")
	public String editPost(@SessionAttribute(name = "loginUser", required = false) User loginUser,
						   @PathVariable(name = "postId") Long postId,
						   @Validated @ModelAttribute PostEditForm postEditForm,
						   BindingResult bindingResult,
						   @RequestParam(name = "file", required = false) MultipartFile file) {
		if (bindingResult.hasErrors()) {
			return "posts/edit";
		}
		
		Post findPost = postService.getPostById(postId);
		if (findPost == null || !findPost.getUser().getId().equals(loginUser.getId())) {
			log.info("수정 권한 없음");
			return "redirect:/posts";
		}
		postEditForm.setId(postId);
		Post post = PostConverter.postEditFormToPost(postEditForm);
		
        if (file != null && !file.isEmpty()) {
            Attachment attachment = fileService.saveFile(file);
            attachment.setPostId(postId);
            post.setAttachment(attachment);
        }
		
		postService.updatePost(post);
		
		return "redirect:/posts";
	}
	
	// 첨부파일 다운로드
    @GetMapping("download/{postId}/{attachmentId}")
    public ResponseEntity<Resource> download(@PathVariable(name = "postId") Long postId,
                                             @PathVariable(name = "attachmentId") Long attachmentId) throws MalformedURLException {
        Post findPost = postService.getPostById(postId);
        if (findPost == null || findPost.getAttachment() == null) {
            return null;
        }
        Attachment attachment = findPost.getAttachment();
        String fullPath = uploadPath + "/" + attachment.getSavedFilename();
        UrlResource resource = new UrlResource("file:" + fullPath);
        String encodingFilename = UriUtils.encode(attachment.getOriginalFilename(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodingFilename + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    // 첨부파일 삭제
    @GetMapping("deleteAttachment/{postId}/{attachmentId}")
    public String deleteAttachment(@SessionAttribute(name = "loginUser", required = false) User loginUser,
                                   @PathVariable(name = "postId") Long postId,
                                   @PathVariable(name = "attachmentId") Long attachmentId) {
       	postService.deleteAttachment(postId, attachmentId, loginUser);
        return "redirect:/posts/" + postId;
    }
	
}










