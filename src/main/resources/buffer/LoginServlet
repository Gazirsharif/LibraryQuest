package com.libraryquest.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Проверка введенных данных (например, через базу данных)
        if (isValidUser(username, password)) {
            // Создаем сессию, если ее нет
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            resp.sendRedirect(req.getContextPath() + "/welcome"); // Перенаправление на страницу приветствия
        } else {
            // Неверные данные, вернуться на страницу входа с ошибкой
            req.setAttribute("error", "Неверное имя пользователя или пароль.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private boolean isValidUser(String username, String password) {
        // Логика проверки пользователя
        return true; // Здесь должна быть ваша логика
    }
}
