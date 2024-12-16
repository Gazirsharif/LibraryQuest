package com.libraryquest.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Завершение текущей сессии
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Кодируем сообщение перед отправкой
        String message = URLEncoder.encode("Вы успешно вышли из системы", StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/login?logoutMessage=" + message);
    }
}
