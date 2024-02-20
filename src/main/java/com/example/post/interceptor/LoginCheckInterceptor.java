package com.example.post.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null) {
			log.info("미 인증 사용자 요청");
			// 로그인 페이지로 리다이렉트
			response.sendRedirect("/users/login?redirectURL=" + requestURI);
			return false;
		}
		return true;
	}
}






