package com.learninglogs.controller;

import com.learninglogs.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * LogoutServlet — handles user logout.
 *
 * URL: /logout
 *
 * GET /logout -> invalidate session -> redirect to /login
 *
 * New for Week 7.
 */

// ============================================================
// TODO 5: Logout Servlet
// ============================================================
// Create a servlet that logs the user out by destroying their session.
//
// Steps:
//   1. Add @WebServlet("/logout") annotation
//   2. Make the class extend HttpServlet
//   3. Override doGet method
//   4. Call SessionUtil.invalidateSession(request) to destroy the session
//   5. Redirect to the login page
//
// CONCEPTS:
// - Logout = destroy the session (remove all stored data)
// - We use GET (not POST) because logout is an idempotent action
//   — calling it multiple times has the same effect as calling it once.
// - After invalidation, the AuthenticationFilter (TODO 9) will prevent
//   the user from accessing protected pages until they log in again.
// - SessionUtil.invalidateSession() safely handles the case where
//   there's no session (e.g., user clicks logout twice).
//
// FLOW:
//   User clicks "Logout" link -> GET /logout
//     -> SessionUtil.invalidateSession(request)
//     -> response.sendRedirect(contextPath + "/login")
//     -> Login page displayed (session gone, filter blocks other pages)
//
// The complete code:
//
//   @WebServlet("/logout")
//   public class LogoutServlet extends HttpServlet {
//
//       @Override
//       protected void doGet(HttpServletRequest request,
//                            HttpServletResponse response)
//               throws ServletException, IOException {
//           SessionUtil.invalidateSession(request);
//           response.sendRedirect(request.getContextPath() + "/login");
//       }
//   }
//
// ============================================================
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
    }
}
