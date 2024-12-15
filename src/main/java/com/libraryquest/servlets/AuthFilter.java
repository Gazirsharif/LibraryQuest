package com.libraryquest.servlets;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean loginRequest = httpRequest.getRequestURI().endsWith("/login") || httpRequest.getRequestURI().endsWith("/register");

        // Исключения для статических ресурсов
        boolean staticResource = httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/css") ||
                httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/js") ||
                httpRequest.getRequestURI().startsWith(httpRequest.getContextPath() + "/images");

        if (loggedIn || loginRequest || staticResource) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
        }
    }
}