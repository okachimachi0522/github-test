package com.example.post.controller;

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

import com.example.post.model.users.User;
import com.example.post.model.users.UserConverter;
import com.example.post.model.users.UserLoginForm;
import com.example.post.model.users.UserRegisterForm;
import com.example.post.model.users.UserUpdateForm;
import com.example.post.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
@Controller
public class UserController {
	
	private final UserService userService;
	
	// 회원가입 폼 이동
	@GetMapping("register")
	public String registerForm(Model model) {
		UserRegisterForm userRegisterForm = new UserRegisterForm();
		model.addAttribute("userRegisterForm", userRegisterForm);
		return "users/register";
	}
	
	// 회원가입 처리
	@PostMapping("register")
	public String register(@Validated @ModelAttribute UserRegisterForm userRegisterForm,
						   BindingResult bindingResult) {
		// 유효성 검증에 실패하면 실행 
		if (bindingResult.hasErrors()) {
			return "users/register";
		}
		
		// 아이디 중복 확인
		User findUser = userService.findUserByUsername(userRegisterForm.getUsername());
		if (findUser != null) {
			bindingResult.reject("duplicate username", "이미 가입된 아이디 입니다.");
			return "users/register";
		}
		
		User user = UserConverter.userRegisterFormToUser(userRegisterForm);
		userService.registerUser(user);
		return "redirect:/";
	}
	
	// 로그인 폼 이동
	@GetMapping("login")
	public String loginForm(Model model) {
		model.addAttribute("userLoginForm", new UserLoginForm());
		return "users/login";
	}
	
	// 로그인 처리
	@PostMapping("login")
	public String login(@Validated @ModelAttribute UserLoginForm userLoginForm,
						BindingResult bindingResult,
						HttpServletRequest request,
						@RequestParam(name = "redirectURL", defaultValue = "/") String redirectURL) {
		if (bindingResult.hasErrors()) {
			return "users/login";
		}
		
		User findUser = userService.findUserByUsername(userLoginForm.getUsername());
		// 파라미터로 받은 아이디에 해당하는 회원 정보를 로그로 출력
		log.info("findUser: {}", findUser);

		if (findUser != null && findUser.getPassword().equals(userLoginForm.getPassword())) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", findUser);
		} else {
			bindingResult.reject("login error", "회원정보가 없거나 패스워드가 다릅니다.");
			return "users/login";
		}

		return "redirect:" + redirectURL;
	}
	
	// 로그아웃 처리
	@GetMapping("logout")
	public String logout(HttpServletRequest request) {
		// getSession(false) 세션이 없으면 세션을 새로 생성하지 않는다.
		HttpSession session = request.getSession(false);
//		session.removeAttribute("loginUser");
		if (session != null) {
			session.invalidate();			
		}
		
		return "redirect:/";
	}
	
	// 회원정보 수정 폼 이동
	@GetMapping("update")
	public String updateForm(@SessionAttribute(name = "loginUser", required = false) User loginUser,
							 Model model) {
		if (loginUser != null) {
			User findUser = userService.findUserById(loginUser.getId());
			model.addAttribute("userUpdateForm", UserConverter.userToUserUpdateForm(findUser));
		} 
		return "users/update";
	}
	
	// 회원정보 수정
	@PostMapping("update/{userId}")
	public String update(@SessionAttribute(name = "loginUser", required = false) User loginUser,
						 @PathVariable(name = "userId") Long userId,
						 @ModelAttribute UserUpdateForm userUpdateForm) {
		User findUser = userService.findUserById(userId);
		if (findUser != null && findUser.getId().equals(userId)) {
			userUpdateForm.setId(userId);
			userService.updateUser(UserConverter.userUpdateFormToUser(userUpdateForm));
		}
		return "redirect:/";
	}
	
}





