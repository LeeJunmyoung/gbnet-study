package kr.co.bookking.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class CSRFTokenManager {
	public static String makeToken(HttpSession session) {
		String token = null;
		
		token = (String) session.getAttribute("CSRF_TOKEM");
		if(token == null) {
			token = UUID.randomUUID().toString();
			session.setAttribute("CSRF_TOKEN", token);
		}
		return token;
	}
	public static String getTokenFromRequest(HttpServletRequest request) {
		
		return request.getParameter("csrf");
	}
}
