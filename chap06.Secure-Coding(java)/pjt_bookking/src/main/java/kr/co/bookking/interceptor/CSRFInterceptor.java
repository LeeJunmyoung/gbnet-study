package kr.co.bookking.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.co.bookking.util.CSRFTokenManager;

public class CSRFInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("CSRF 인터셉터 클래스 실행");

		String reqToken = CSRFTokenManager.getTokenFromRequest(request);
		String sessionToken = request.getSession().getAttribute("CSRF_TOKEN") == null ? "" : (String)request.getSession().getAttribute("CSRF_TOKEN");
		
		if(StringUtils.isEmpty(reqToken) == true) {
			System.out.println("Token fails");
			return false;
		}else if(reqToken.equals(sessionToken) == false) {
			System.out.println("Token not equal");
			System.out.println("TOKEN :" + reqToken);
			System.out.println("CsrfToken :" + sessionToken);
			return false;
		}
		
		System.out.println("TOKEN : " + reqToken);
		
		request.getSession().removeAttribute("CSRF_TOKEN");
		
		return true;
	}

}
