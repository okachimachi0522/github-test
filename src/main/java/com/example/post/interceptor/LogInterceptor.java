package com.example.post.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
	// 컨트롤러의 핸들러 메소드가 호출되기 전에 실행
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("preHandle 실행");
		String requestURI = request.getRequestURI();
		
		if (handler instanceof HandlerMethod) {
			// 호출할 컨트롤러 메소드의 이름, 파라미터 정보들이 담겨있다.
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			log.info("handlerMethod: {}", handlerMethod);
		}
		
		log.info("REQUEST: {}", requestURI);
		
		return true;
	}

	// 예외가 발생하면 호출되지 않는다.
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("postHandle 실행, {}", modelAndView);
		
	}

	// 예외 발생과 무관하게 호출된다.
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("afterCompletion 실행");
		if (ex != null) {
			log.error("afterCompletion error: ", ex);
		}
	}

}
