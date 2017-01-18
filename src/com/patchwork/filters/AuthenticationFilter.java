package com.patchwork.filters;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    /**
     * Default constructor. 
     */
    public AuthenticationFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		Principal user = req.getUserPrincipal();
		boolean isLoggedIn = user != null;
		boolean isHome = req.getRequestURI().equals(req.getContextPath());
		boolean isHomePlus = req.getRequestURI().equals(req.getContextPath() + "/");
		
		String path = req.getRequestURI().substring(req.getContextPath().length());
		boolean isStatic = path.startsWith("/static");
		boolean isLogin = path.startsWith("/Login");
		boolean isRegister = path.startsWith("/Register");
		boolean isLoginRequired = path.startsWith("/LoginRequired");
		boolean isSinglePlayer = path.startsWith("/Play/Single");
		boolean isJSON = path.startsWith("/JSON");
		if( isJSON || isLoggedIn || isHome || isHomePlus || isStatic || isLogin || isRegister || isLoginRequired || isSinglePlayer) {
			// pass the request along the filter chain
			chain.doFilter(request, response);			
		} else {
			res.sendRedirect(req.getContextPath());
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}