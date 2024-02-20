package com.example.post.filter;

import java.io.IOException;

import org.springframework.util.PatternMatchUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginCheckFilter implements Filter {
	
	private static final String[] whiteList = {
				"/", 
				"/users/register",
				"/users/login",
				"/users/logout",
				"/posts",
				"/error"
			};

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String requestURI = httpServletRequest.getRequestURI();
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		try {
			log.info("로그인 인증 체크 필터 시작");
			
			boolean simpleMatch = PatternMatchUtils.simpleMatch(whiteList, requestURI);
//			log.info("simpleMatch: {}", simpleMatch);
			if (!simpleMatch) {
				log.info("로그인 인증 체크 로직 실행");
				HttpSession session = httpServletRequest.getSession(false);
				if (session == null || session.getAttribute("loginUser") == null) {
					log.info("미인증 사용자 요청");
					httpServletResponse.sendRedirect("/users/login");
					return;
				}
			}
			
			chain.doFilter(request, response);
		} catch (Exception e) {
			
		} finally {
			log.info("로그인 인증 체크 필터 종료");
		}
	}

}






