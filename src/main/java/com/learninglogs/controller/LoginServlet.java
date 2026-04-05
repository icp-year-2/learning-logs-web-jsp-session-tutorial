package com.learninglogs.controller;

import com.learninglogs.dao.UserDao;
import com.learninglogs.dao.UserDaoImpl;
import com.learninglogs.entity.User;
import com.learninglogs.utils.PasswordUtil;
import com.learninglogs.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * LoginServlet — handles user login.
 *
 * URL: /login
 *
 * GET  /login -> forward to login.jsp (displays the form)
 * POST /login -> find user, verify password, store session, redirect on success
 *
 * Week 7: doPost now stores the User object in the session after successful
 * authentication (previously just redirected with no session state).
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDao.findByUsername(username);

        if (user == null) {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            request.setAttribute("error", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                   .forward(request, response);
            return;
        }

        // ============================================================
        // TODO 4: Store User in Session
        // ============================================================
        // After successful password verification, store the User object
        // in the session so the app "remembers" who is logged in.
        //
        // Add this ONE line before the redirect:
        //
        //   SessionUtil.setAttribute(request, "user", user);
        //
        // CONCEPT: HTTP is stateless — each request is independent.
        // Without sessions, the server forgets who you are after
        // every request. By storing the User object in the session:
        //
        //   1. The server creates a session (a server-side storage area)
        //   2. The User object is stored under the key "user"
        //   3. A session ID cookie (JSESSIONID) is sent to the browser
        //   4. On future requests, the browser sends this cookie back
        //   5. The server finds the session and retrieves the User object
        //
        // This enables:
        //   - TopicServlet to know WHICH user's topics to show (TODO 6)
        //   - JSP pages to display the username (TODOs 7-8)
        //   - AuthenticationFilter to check if user is logged in (TODO 9)
        //
        // SessionUtil.setAttribute also sets a 30-minute timeout —
        // if the user is inactive for 30 minutes, the session expires
        // and they'll need to log in again.
        //
        // The complete code:
        //
        //   SessionUtil.setAttribute(request, "user", user);
        //
        // ============================================================
        SessionUtil.setAttribute(request, "user", user);

        response.sendRedirect(request.getContextPath() + "/topic");
    }
}
