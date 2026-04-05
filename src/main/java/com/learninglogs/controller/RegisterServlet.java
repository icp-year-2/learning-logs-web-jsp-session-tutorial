package com.learninglogs.controller;

import com.learninglogs.dao.UserDao;
import com.learninglogs.dao.UserDaoImpl;
import com.learninglogs.entity.User;
import com.learninglogs.utils.PasswordUtil;
import com.learninglogs.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * RegisterServlet — handles user registration.
 *
 * URL: /register
 *
 * GET  /register → forward to register.jsp (show empty form)
 * POST /register → validate → hash password → insert user → redirect to /login
 */

// ============================================================
// TODO 7: Servlet Class Declaration + doGet
// ============================================================
// Create the RegisterServlet class that handles registration requests.
//
// Steps:
//   1. Add @WebServlet("/register") annotation above the class
//   2. Make the class extend HttpServlet
//   3. Create a UserDao field: new UserDaoImpl()
//   4. Override doGet to forward to register.jsp
//
// CONCEPTS:
// - @WebServlet("/register") maps this servlet to the URL /register.
// - The DAO field is created once and reused for all requests.
// - doGet simply shows the empty registration form — no data needed.
// - Same forward pattern as TopicServlet's "new" action.
//
// The complete code:
//
//   @WebServlet("/register")
//   public class RegisterServlet extends HttpServlet {
//
//       private final UserDao userDao = new UserDaoImpl();
//
//       @Override
//       protected void doGet(HttpServletRequest request,
//                            HttpServletResponse response)
//               throws ServletException, IOException {
//           request.getRequestDispatcher("/WEB-INF/views/register.jsp")
//                  .forward(request, response);
//       }
//   }
//
// ============================================================
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDao userDao = new UserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
               .forward(request, response);
    }

    // ============================================================
    // TODO 8: doPost — Extract Parameters + Validate
    // ============================================================
    // Handle the registration form submission. Extract the four
    // form parameters and validate each one using ValidationUtil.
    //
    // Steps:
    //   1. Override doPost method
    //   2. Extract parameters: username, email, password, cpassword
    //   3. Create a StringBuilder for collecting error messages
    //   4. Validate username:
    //      - Not null/empty, alphanumeric starting with letter, 5+ chars
    //   5. Validate email: valid format
    //   6. Validate password: meets strength requirements
    //   7. Validate confirm password: matches password
    //   8. If errors exist, set "error" attribute and forward back
    //
    // CONCEPTS:
    // - StringBuilder collects ALL validation errors into one string,
    //   so the user sees all problems at once (not one at a time).
    // - Forward (not redirect) on error — this preserves the request
    //   parameters so ${param.username} can show what was typed.
    // - ValidationUtil methods are provided complete — you just call them.
    //
    // The complete code:
    //
    //   @Override
    //   protected void doPost(HttpServletRequest request,
    //                         HttpServletResponse response)
    //           throws ServletException, IOException {
    //
    //       String username = request.getParameter("username");
    //       String email = request.getParameter("email");
    //       String password = request.getParameter("password");
    //       String confirmPassword = request.getParameter("cpassword");
    //
    //       StringBuilder errors = new StringBuilder();
    //
    //       if (ValidationUtil.isNullOrEmpty(username)
    //               || !ValidationUtil.isAlphanumericStartingWithLetter(username)
    //               || username.length() < 5) {
    //           errors.append("Username must be alphanumeric, start with a letter, and be at least 5 characters. ");
    //       }
    //       if (!ValidationUtil.isValidEmail(email)) {
    //           errors.append("Invalid email format. ");
    //       }
    //       if (!ValidationUtil.isValidPassword(password)) {
    //           errors.append("Password must be 8+ characters with uppercase, number, and symbol. ");
    //       }
    //       if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
    //           errors.append("Passwords do not match. ");
    //       }
    //
    //       if (!errors.isEmpty()) {
    //           request.setAttribute("error", errors.toString().trim());
    //           request.getRequestDispatcher("/WEB-INF/views/register.jsp")
    //                  .forward(request, response);
    //           return;
    //       }
    //
    //       // TODOs 9-10 continue here
    //   }
    //
    // ============================================================
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("cpassword");

        StringBuilder errors = new StringBuilder();

        if (ValidationUtil.isNullOrEmpty(username)
                || !ValidationUtil.isAlphanumericStartingWithLetter(username)
                || username.length() < 5) {
            errors.append("Username must be alphanumeric, start with a letter, and be at least 5 characters. ");
        }
        if (!ValidationUtil.isValidEmail(email)) {
            errors.append("Invalid email format. ");
        }
        if (!ValidationUtil.isValidPassword(password)) {
            errors.append("Password must be 8+ characters with uppercase, number, and symbol. ");
        }
        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
            errors.append("Passwords do not match. ");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("error", errors.toString().trim());
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
            return;
        }

        // ============================================================
        // TODO 9: Hash Password + Create User Object
        // ============================================================
        // Now that validation passed, hash the password and create
        // a User object ready for database insertion.
        //
        // Steps:
        //   1. Hash the password using PasswordUtil.getHashPassword()
        //   2. Create a new User object with username, email, and
        //      the HASHED password (never store plaintext!)
        //
        // CONCEPT: This is where lecture theory meets code. The BCrypt
        // hash includes a random salt, so even if two users have the
        // same password, their hashes will be different. The hash is
        // a 60-character string like: $2a$10$N9qo8uLO...
        //
        // The complete code:
        //
        //   String hashedPassword = PasswordUtil.getHashPassword(password);
        //   User user = new User(username, email, hashedPassword);
        //
        // ============================================================
        String hashedPassword = PasswordUtil.getHashPassword(password);
        User user = new User(username, email, hashedPassword);

        // ============================================================
        // TODO 10: Insert User + Handle Result
        // ============================================================
        // Insert the user into the database and handle the result.
        //
        // Steps:
        //   1. Call userDao.insertUser(user)
        //   2. If it returns false (duplicate username/email):
        //      - Set error message
        //      - Forward back to register.jsp
        //      - Return (stop processing)
        //   3. If successful:
        //      - Redirect to the login page
        //
        // CONCEPT: Same Forward vs Redirect pattern as TopicServlet:
        // - Forward on ERROR → keeps the request (error message + typed values)
        // - Redirect on SUCCESS → Post-Redirect-Get pattern (prevents
        //   double-submit if user refreshes the page)
        //
        // The complete code:
        //
        //   boolean success = userDao.insertUser(user);
        //
        //   if (!success) {
        //       request.setAttribute("error", "Username or email already exists.");
        //       request.getRequestDispatcher("/WEB-INF/views/register.jsp")
        //              .forward(request, response);
        //       return;
        //   }
        //
        //   response.sendRedirect(request.getContextPath() + "/login");
        //
        // ============================================================
        boolean success = userDao.insertUser(user);

        if (!success) {
            request.setAttribute("error", "Username or email already exists.");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                   .forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }
}