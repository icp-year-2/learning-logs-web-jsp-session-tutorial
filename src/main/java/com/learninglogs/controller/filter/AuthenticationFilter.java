package com.learninglogs.controller.filter;

import com.learninglogs.utils.SessionUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * AuthenticationFilter — protects all routes behind login.
 *
 * Acts as middleware (gatekeeper) that intercepts every request and checks
 * if the user is logged in before allowing access.
 *
 * Rules:
 *   - Static resources (.css, .png, .js, .jpg) → always allowed
 *   - /login and /register → allowed only if NOT logged in
 *   - Everything else → allowed only if logged in
 *
 * New for Week 7.
 */

// ============================================================
// TODO 9: Authentication Filter
// ============================================================
// Create a servlet filter that protects all routes.
//
// A filter is like a "gatekeeper" — it sits between the client
// and the servlet. Every request passes through the filter BEFORE
// reaching the servlet. The filter decides whether to:
//   a) Allow the request through: chain.doFilter(request, response)
//   b) Block and redirect: response.sendRedirect(...)
//
// Steps:
//   1. Add @WebFilter("/*") annotation — intercepts ALL URLs
//   2. Implement the Filter interface (jakarta.servlet.Filter)
//   3. Override doFilter method with this logic:
//
//      a) Cast ServletRequest/Response to Http versions
//      b) Extract the path from the URI (remove context path)
//      c) Allow static resources through (CSS, images, JS)
//      d) Check if user is logged in (SessionUtil.getAttribute)
//      e) If NOT logged in:
//         - Allow /login and /register (they need these to log in!)
//         - Redirect everything else to /login
//      f) If logged in:
//         - Redirect /login and /register to /topic (already logged in)
//         - Allow everything else through
//
// CONCEPTS:
// - @WebFilter("/*") means this filter runs for EVERY request
// - chain.doFilter(request, response) = "let the request continue"
//   If you don't call this, the request is blocked.
// - Filter vs Servlet: Servlet handles one URL pattern.
//   Filter intercepts requests and can modify/block them.
// - The filter checks sessions — if no session (or no "user" attribute
//   in session), the user hasn't logged in.
//
// WHY REDIRECT LOGGED-IN USERS AWAY FROM /login AND /register?
// If a logged-in user visits /login, they should go to /topic instead.
// This prevents the confusing experience of seeing a login form
// when you're already logged in.
//
// IMPORTANT — STATIC RESOURCES:
// The filter intercepts ALL requests — including requests for CSS files
// and images! Without the static resource check, the login page would
// load without any styling (CSS blocked by filter).
//
// The complete code:
//
//   @WebFilter("/*")
//   public class AuthenticationFilter implements Filter {
//
//       @Override
//       public void doFilter(ServletRequest request,
//                            ServletResponse response,
//                            FilterChain chain)
//               throws IOException, ServletException {
//
//           HttpServletRequest req = (HttpServletRequest) request;
//           HttpServletResponse res = (HttpServletResponse) response;
//
//           String uri = req.getRequestURI();
//           String contextPath = req.getContextPath();
//           String path = uri.substring(contextPath.length());
//
//           // Allow static resources (CSS, images, JS) through without login
//           if (path.startsWith("/static/")) {
//               chain.doFilter(request, response);
//               return;
//           }
//
//           boolean isLoggedIn = SessionUtil.getAttribute(req, "user") != null;
//           boolean isAuthPage = "/login".equals(path) || "/register".equals(path);
//
//           if (!isLoggedIn && !isAuthPage) {
//               // Not logged in + trying to access protected page -> go to login
//               res.sendRedirect(contextPath + "/login");
//               return;
//           }
//
//           if (isLoggedIn && isAuthPage) {
//               // Already logged in + trying to access login/register -> go to topics
//               res.sendRedirect(contextPath + "/topic");
//               return;
//           }
//
//           // All other cases: allow through
//           chain.doFilter(request, response);
//       }
//   }
//
// ============================================================
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}
