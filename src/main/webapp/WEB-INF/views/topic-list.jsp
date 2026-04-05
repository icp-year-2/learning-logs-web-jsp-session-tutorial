<%-- ============================================================
     TODO 1: Add the fmt Taglib Directive
     ============================================================
     You already have the core JSTL taglib (prefix="c"). Now add
     the FORMATTING taglib so you can use <fmt:formatDate>.

     Add this directive AFTER the existing core taglib:

       <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

     CONCEPT: JSTL has multiple tag libraries:
       - Core (c:) — loops, conditions, output (you already use this)
       - Formatting (fmt:) — date/number formatting (NEW this week)

     NOTE: The URI is "jakarta.tags.fmt" (Jakarta EE namespace).
     The lecturer's slides show "http://java.sun.com/jsp/jstl/fmt"
     which is the OLD Java EE namespace — do NOT use that.
     Our project uses Jakarta EE (Tomcat 10+), so we use
     "jakarta.tags.fmt" — same pattern as "jakarta.tags.core".

     No new Maven dependencies needed — the fmt library is already
     included in the JSTL 3.0 dependency from Week 4.

     The complete code:

       <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
     ============================================================ --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Learning Log</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/topic-list.css" />
  </head>

  <body>
    <div class="page">

      <header class="header">
        <div class="logo">
          <a href="${pageContext.request.contextPath}/topic" style="text-decoration: none">
            <img src="${pageContext.request.contextPath}/static/images/book.png" alt="LL" />
          </a>
          <h3>Learning Log</h3>
        </div>
        <%-- ============================================================
             TODO 7: Display Logged-In Username and Logout Link
             ============================================================
             Replace the static "Username" text and placeholder "#" logout
             link with dynamic session data.

             Currently the header shows:
               <div class="usersession">
                 <h3>Username</h3>
                 <a href="#" class="logout">Logout</a>
               </div>

             Change it to use the User object stored in the session:

               <div class="usersession">
                 <h3><c:out value="${sessionScope.user.username}" /></h3>
                 <a href="${pageContext.request.contextPath}/logout" class="logout"
                    onclick="return confirm('Are you sure you want to logout?');">Logout</a>
               </div>

             CONCEPT: ${sessionScope.user.username} reads from the SESSION
             scope — this is the User object stored by LoginServlet (TODO 4).

             Breaking it down:
               - sessionScope = the session (server-side storage)
               - .user = the object stored with key "user"
               - .username = calls getUsername() on the User entity

             EL automatically calls the getter method — you write
             ${sessionScope.user.username} and EL calls user.getUsername().

             We wrap it in <c:out> for XSS safety (consistent with Week 6).

             The logout link points to /logout (LogoutServlet, TODO 5).
             The confirm dialog prevents accidental logouts.

             The complete code:

               <div class="usersession">
                 <h3><c:out value="${sessionScope.user.username}" /></h3>
                 <a href="${pageContext.request.contextPath}/logout" class="logout"
                    onclick="return confirm('Are you sure you want to logout?');">Logout</a>
               </div>
             ============================================================ --%>
        <div class="usersession">
          <h3>Username</h3>
          <a href="#" class="logout">Logout</a>
        </div>
      </header>

      <%-- ============================================================
           TODO 3: Display Topic Count Using .size()
           ============================================================
           Show how many topics exist in the navbar title area.

           Replace the static text "Topic Lists" with a dynamic count
           using the .size() method on the topics collection:

             <p>Topic Lists (${topics.size()})</p>

           CONCEPT: In EL (Expression Language), you can call Java
           methods directly. ${topics.size()} calls the .size() method
           on the ArrayList that the servlet set as a request attribute.
           This shows students the current count without needing any
           extra Java code.

           Examples of what this displays:
             - "Topic Lists (5)" — when 5 topics exist
             - "Topic Lists (0)" — when no topics exist

           You could also store it in a variable first with <c:set>:
             <c:set var="topicCount" value="${topics.size()}" />
             <p>Topic Lists (${topicCount})</p>

           Both approaches work — .size() inline is simpler for
           a single use. <c:set> is useful when you need the value
           multiple times.

           The complete code:

             <p>Topic Lists (${topics.size()})</p>
           ============================================================ --%>
      <nav class="navbar">
        <ul>
          <li></li>
          <li><p>Topic Lists (${topics.size()})</p></li>
          <li><a href="${pageContext.request.contextPath}/topic?action=new">+New Topic</a></li>
        </ul>
      </nav>

      <main class="content">
        <div class="search">
          <form action="${pageContext.request.contextPath}/topic" method="get">
            <input type="hidden" name="action" value="search" />
            <label for="search">Topic: </label>
            <input type="text" name="search" placeholder="Search..." value="<c:out value='${searchKeyword}' default='' />" />
            <button type="submit">SEARCH</button>
          </form>
        </div>
        <div class="topicContainer">
          <ul>
            <%-- ============================================================
                 TODO 2: Format Topic Dates with fmt:formatDate
                 ============================================================
                 Each topic has a createdAt timestamp (java.sql.Timestamp).
                 Currently it would display as something like:
                   "2026-03-24 10:30:15.0" (raw database timestamp)

                 Use fmt:formatDate to format it nicely:
                   "Mar 24, 2026"

                 Add a date display inside each topic item, AFTER the
                 topic name link. Use this tag:

                   <span class="date">
                     <fmt:formatDate value="${topic.createdAt}"
                       pattern="MMM d, yyyy" />
                   </span>

                 CONCEPT: fmt:formatDate converts a Java Date/Timestamp
                 object into a formatted string. The pattern uses Java's
                 SimpleDateFormat symbols:
                   - MMM  = abbreviated month (Jan, Feb, Mar...)
                   - d    = day of month (1, 2, ... 31)
                   - yyyy = 4-digit year (2026)

                 So pattern="MMM d, yyyy" turns a timestamp into
                 "Mar 24, 2026". Other patterns you could use:
                   - "dd/MM/yyyy"      -> "24/03/2026"
                   - "EEEE, MMMM d"    -> "Monday, March 24"
                   - "yyyy-MM-dd"      -> "2026-03-24"

                 The complete code (inside each topicItem li):

                   <span class="date">
                     <fmt:formatDate value="${topic.createdAt}"
                       pattern="MMM d, yyyy" />
                   </span>

                 TODO 4: Use c:out for XSS-Safe Topic Names
                 ============================================================
                 Replace the raw EL expression ${topic.name} with
                 c:out for XSS protection.

                 Currently the topic name link uses:
                   ${status.count}. ${topic.name}

                 Change it to:
                   ${status.count}. <c:out value="${topic.name}" />

                 CONCEPT: XSS (Cross-Site Scripting) is a security
                 vulnerability where an attacker injects malicious
                 HTML/JavaScript into a page. For example, if someone
                 creates a topic named:
                   (script)alert('hacked')(/script)

                 With raw ${topic.name}, the browser would EXECUTE
                 that script — the attacker's code runs on your page!

                 With <c:out value="${topic.name}" />, the output is
                 HTML-escaped — angle brackets become &lt; and &gt;
                 so the browser displays it as TEXT instead of
                 executing it.

                 c:out escapes these characters:
                   < -> &lt;    > -> &gt;    & -> &amp;
                   " -> &quot;  ' -> &#039;

                 RULE: Always use c:out when displaying user-provided
                 data. Topic names come from user input — they MUST be
                 escaped. Static text (like "Learning Log") doesn't
                 need c:out because it's not user-controlled.

                 The complete code:

                   <a class="topic" href="...">${status.count}. <c:out value="${topic.name}" /></a>
                 ============================================================ --%>
            <c:forEach var="topic" items="${topics}" varStatus="status">
              <li class="topicItem">
                <a class="topic" href="${pageContext.request.contextPath}/entry?topicid=${topic.id}">${status.count}. <c:out value="${topic.name}" /></a>
                <span class="date">
                  <fmt:formatDate value="${topic.createdAt}" pattern="MMM d, yyyy" />
                </span>
                <a class="edit" href="${pageContext.request.contextPath}/topic?action=edit&topicid=${topic.id}">Edit</a>
                <form action="${pageContext.request.contextPath}/topic" method="post">
                  <input type="hidden" name="action" value="delete" />
                  <input type="hidden" name="topicid" value="${topic.id}" />
                  <button class="submit" type="submit"
                    onclick="return confirm('Are you sure you want to delete?');">
                    Delete
                  </button>
                </form>
              </li>
            </c:forEach>
          </ul>
        </div>
      </main>

      <footer class="footer">
        <h3>&copy; Learning Logs</h3>
      </footer>

    </div>
  </body>
</html>
