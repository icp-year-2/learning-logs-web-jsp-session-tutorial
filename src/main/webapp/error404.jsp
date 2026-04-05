<%-- ============================================================
     TODO 7: Create Custom 404 Error Page
     ============================================================
     Create a custom 404 error page that matches the app's style.
     This replaces Tomcat's default ugly 404 error page.

     The page should:
     1. Be a standalone page (no header/navbar — the user is lost!)
     2. Show a large "404" heading so the user knows what happened
     3. Explain the page wasn't found
     4. Provide a "Go to Topics" link to get back to the app

     This file is in webapp/ (NOT in WEB-INF/views/) because error
     pages must be directly accessible by the server — they can't
     go through a servlet.

     CONCEPT: When a user visits a URL that doesn't exist (like
     /learning-logs/nonexistent), Tomcat normally shows its own
     default 404 page with a stack trace. A custom error page:
       - Looks professional (matches your app's design)
       - Hides server details (security — don't expose Tomcat version)
       - Helps the user navigate back to the app

     We use a JSP (not HTML) so we can use ${pageContext.request.contextPath}
     for the "Go to Topics" link. This ensures the link works regardless
     of the app's deployment context path.

     The CSS for this page is in static/css/error.css — a separate
     stylesheet because this page uses a different layout than the
     main app pages (no header/navbar/footer).

     The complete code:

       <%@ page contentType="text/html;charset=UTF-8" language="java" %>
       <!DOCTYPE html>
       <html lang="en">
         <head>
           <meta charset="UTF-8" />
           <meta name="viewport" content="width=device-width, initial-scale=1.0" />
           <title>404 - Page Not Found</title>
           <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css" />
         </head>
         <body>
           <div class="container">
             <h1>404</h1>
             <h2>Oops! Page Not Found: Learning Logs</h2>
             <a href="${pageContext.request.contextPath}/topic" class="btn">Go to Topics</a>
           </div>
         </body>
       </html>
     ============================================================ --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>404 - Page Not Found</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/error.css" />
  </head>
  <body>
    <div class="container">
      <h1>404</h1>
      <h2>Oops! Page Not Found: Learning Logs</h2>
      <a href="${pageContext.request.contextPath}/topic" class="btn">Go to Topics</a>
    </div>
  </body>
</html>
