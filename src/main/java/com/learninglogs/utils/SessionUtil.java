package com.learninglogs.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * SessionUtil — utility class for session management.
 *
 * Wraps HttpSession operations into clean, reusable static methods.
 * Used by LoginServlet (create session), LogoutServlet (destroy session),
 * TopicServlet (read session), and AuthenticationFilter (check session).
 *
 * New for Week 7.
 */

// ============================================================
// TODO 1: Session Utility Class
// ============================================================
// Create three static methods that wrap the HttpSession API:
//
// 1. setAttribute(request, key, value)
//    - Gets the session from the request (creates one if needed)
//    - Sets a 30-minute timeout (30 * 60 seconds)
//    - Stores the key-value pair in the session
//
// 2. getAttribute(request, key)
//    - Gets the session WITHOUT creating a new one: getSession(false)
//    - If session exists, returns the attribute value
//    - If no session exists, returns null
//
// 3. invalidateSession(request)
//    - Gets the session WITHOUT creating a new one: getSession(false)
//    - If session exists, calls session.invalidate()
//    - If no session, does nothing (safe to call anytime)
//
// CONCEPTS:
// - request.getSession() — gets existing session OR creates new one
// - request.getSession(false) — gets existing session OR returns null
//   (NEVER creates a new session — important for read/delete operations)
// - session.setMaxInactiveInterval(seconds) — auto-expires the session
//   after the specified seconds of inactivity (30 min = 30 * 60 = 1800)
// - session.invalidate() — destroys the session immediately
//   (removes all attributes and marks the session as invalid)
//
// WHY A UTILITY CLASS?
// Without SessionUtil, every servlet would have this code:
//   HttpSession session = request.getSession(false);
//   if (session != null) { ... }
//
// With SessionUtil, servlets just call:
//   SessionUtil.setAttribute(request, "user", user);
//   User user = (User) SessionUtil.getAttribute(request, "user");
//   SessionUtil.invalidateSession(request);
//
// Cleaner, less error-prone, and the null-check is handled once.
//
// The complete code:
//
//   public class SessionUtil {
//
//       public static void setAttribute(HttpServletRequest request,
//                                       String key, Object value) {
//           HttpSession session = request.getSession();
//           session.setMaxInactiveInterval(30 * 60);
//           session.setAttribute(key, value);
//       }
//
//       public static Object getAttribute(HttpServletRequest request,
//                                         String key) {
//           HttpSession session = request.getSession(false);
//           if (session != null) {
//               return session.getAttribute(key);
//           }
//           return null;
//       }
//
//       public static void invalidateSession(HttpServletRequest request) {
//           HttpSession session = request.getSession(false);
//           if (session != null) {
//               session.invalidate();
//           }
//       }
//   }
//
// ============================================================
public class SessionUtil {

    public static void setAttribute(HttpServletRequest request,
                                    String key, Object value) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(30 * 60);
        session.setAttribute(key, value);
    }

    public static Object getAttribute(HttpServletRequest request,
                                      String key) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getAttribute(key);
        }
        return null;
    }

    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
