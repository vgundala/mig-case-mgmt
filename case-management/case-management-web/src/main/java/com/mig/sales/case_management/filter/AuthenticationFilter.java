package com.mig.sales.case_management.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestURI = httpRequest.getRequestURI();

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean isLoginRequest = requestURI.endsWith("login.jsp") || requestURI.endsWith("login.do") || requestURI.endsWith("register.jsp") || requestURI.endsWith("register.do");

        if (loggedIn || isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect("login.jsp");
        }
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}