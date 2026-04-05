<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Learning Log — Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/auth.css" />
  </head>

  <body>
    <div class="auth-page">

      <div class="auth-header">
        <img src="${pageContext.request.contextPath}/static/images/book.png" alt="LL" />
        <h1>Learning Logs</h1>
      </div>

      <%-- ============================================================
           TODO 8: Use c:out for Login Form Values
           ============================================================
           Same pattern as register.jsp (TODO 7) — replace raw EL
           with c:out for XSS protection.

           Two changes:

           1. Error message — use c:out inside the existing c:if:

              Currently:
                <p class="error">${error}</p>

              Change to:
                <p class="error"><c:out value="${error}" /></p>

           2. Username input — use c:out in the value attribute:

              Currently:
                value="${param.username}"

              Change to:
                value="<c:out value='${param.username}' default='' />"

           CONCEPT: Same reflected XSS risk as register.jsp. The
           username the user typed is echoed back via ${param.username}
           — without c:out, malicious input could inject HTML/JS.

           After completing this TODO and TODO 7 (register.jsp), you
           will have applied c:out to EVERY page in the app that
           displays user-provided data:
             - topic-list.jsp (Tutorial TODO 4)
             - topic-add-edit.jsp (Tutorial TODOs 5-6)
             - entry-list.jsp (Workshop TODOs 3-4)
             - entry-add-edit.jsp (Workshop TODOs 5-6)
             - register.jsp (Workshop TODO 7)
             - login.jsp (Workshop TODO 8)

           The complete form code:

             <div class="auth-form">
               <form action="${pageContext.request.contextPath}/login" method="post">
                 <h2>Login</h2>

                 <c:if test="${not empty error}">
                   <p class="error"><c:out value="${error}" /></p>
                 </c:if>

                 <input type="text" name="username" placeholder="Username"
                        value="<c:out value='${param.username}' default='' />" required />
                 <input type="password" name="password" placeholder="Password" required />

                 <button type="submit">Login</button>

                 <p class="link">Don't have an account?
                   <a href="${pageContext.request.contextPath}/register">Register</a>
                 </p>
               </form>
             </div>
           ============================================================ --%>
      <div class="auth-form">
        <form action="${pageContext.request.contextPath}/login" method="post">
          <h2>Login</h2>

          <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}" /></p>
          </c:if>

          <input type="text" name="username" placeholder="Username"
                 value="<c:out value='${param.username}' default='' />" required />
          <input type="password" name="password" placeholder="Password" required />

          <button type="submit">Login</button>

          <p class="link">Don't have an account?
            <a href="${pageContext.request.contextPath}/register">Register</a>
          </p>
        </form>
      </div>

    </div>
  </body>
</html>
