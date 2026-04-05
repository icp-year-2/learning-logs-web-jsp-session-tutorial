<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Learning Log — Register</title>
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
           TODO 7: Use c:out for Registration Form Values
           ============================================================
           The registration form currently displays user-provided data
           using raw EL expressions — vulnerable to XSS:

             ${error}            — error message from servlet
             ${param.username}   — username the user typed
             ${param.email}      — email the user typed

           Replace each with c:out for XSS protection:

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

           3. Email input — same pattern:

              Currently:
                value="${param.email}"

              Change to:
                value="<c:out value='${param.email}' default='' />"

           CONCEPT: ${param.username} reads the form parameter directly
           from the request URL/body. This is REFLECTED user input —
           whatever the user typed gets echoed back into the page. If
           someone submits a username like:
             " onfocus="alert('xss')
           the raw value attribute becomes:
             value="" onfocus="alert('xss')"
           which injects an event handler into the HTML!

           c:out escapes the quotes and angle brackets, preventing
           the injection. This is called REFLECTED XSS — the attack
           payload comes from the request and is "reflected" back.

           The same rule from the Tutorial applies: if the data came
           from a user, use c:out. ${param.*} values ALWAYS come from
           users — they must ALWAYS be escaped.

           NOTE: Password fields don't use value retention (security
           practice), so no c:out needed there.

           The complete form code:

             <div class="auth-form">
               <form action="${pageContext.request.contextPath}/register" method="post">
                 <h2>Register</h2>

                 <c:if test="${not empty error}">
                   <p class="error"><c:out value="${error}" /></p>
                 </c:if>

                 <input type="text" name="username" placeholder="Username"
                        value="<c:out value='${param.username}' default='' />" required />
                 <input type="email" name="email" placeholder="Email"
                        value="<c:out value='${param.email}' default='' />" required />
                 <input type="password" name="password" placeholder="Password" required />
                 <input type="password" name="cpassword" placeholder="Confirm Password" required />

                 <button type="submit">Register</button>

                 <p class="link">Already have an account?
                   <a href="${pageContext.request.contextPath}/login">Log in</a>
                 </p>
               </form>
             </div>
           ============================================================ --%>
      <div class="auth-form">
        <form action="${pageContext.request.contextPath}/register" method="post">
          <h2>Register</h2>

          <c:if test="${not empty error}">
            <p class="error"><c:out value="${error}" /></p>
          </c:if>

          <input type="text" name="username" placeholder="Username"
                 value="<c:out value='${param.username}' default='' />" required />
          <input type="email" name="email" placeholder="Email"
                 value="<c:out value='${param.email}' default='' />" required />
          <input type="password" name="password" placeholder="Password" required />
          <input type="password" name="cpassword" placeholder="Confirm Password" required />

          <button type="submit">Register</button>

          <p class="link">Already have an account?
            <a href="${pageContext.request.contextPath}/login">Log in</a>
          </p>
        </form>
      </div>

    </div>
  </body>
</html>
