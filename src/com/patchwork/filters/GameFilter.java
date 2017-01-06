package com.patchwork.filters;

import java.io.IOException;

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
 * Servlet Filter implementation class GameIdFilter
 */
@WebFilter("/Play/*")
public class GameFilter implements Filter {

    /**
     * Default constructor. 
     */
    public GameFilter() {
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
		
		String pathInfo = req.getPathInfo();
		Boolean isSubpath = pathInfo.startsWith("/");
		Boolean isInt = false;
		Boolean isSingle = false;
		if(isSubpath) {
			String gameIdString = pathInfo.substring(1);
			try {
				Integer.parseInt(gameIdString);
				isInt = true;
			} catch (NumberFormatException e) {}
			
			if(pathInfo.equalsIgnoreCase("/Single")) {
				isSingle = true;
			}
		}
		// pass the request along the filter chain
		if( isSubpath && (isInt || isSingle) )
			chain.doFilter(request, response);
		else
			res.sendRedirect(req.getContextPath());
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
