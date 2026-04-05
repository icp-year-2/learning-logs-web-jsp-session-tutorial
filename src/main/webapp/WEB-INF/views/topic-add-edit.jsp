<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Learning Log</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/topic-add.css" />
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
             TODO 8: Display Logged-In Username and Logout Link
             ============================================================
             Same change as TODO 7 (topic-list.jsp) — replace the static
             "Username" and "#" link with dynamic session data.

             Currently:
               <div class="usersession">
                 <h3>Username</h3>
                 <a href="#" class="logout">Logout</a>
               </div>

             Change to:
               <div class="usersession">
                 <h3><c:out value="${sessionScope.user.username}" /></h3>
                 <a href="${pageContext.request.contextPath}/logout" class="logout"
                    onclick="return confirm('Are you sure you want to logout?');">Logout</a>
               </div>

             CONCEPT: Same pattern as TODO 7. Every page that has the
             header needs this change — the username and logout link
             should work on ALL pages, not just the topic list.

             In the Workshop, you'll make this same change to entry pages.

             The complete code:

               <div class="usersession">
                 <h3><c:out value="${sessionScope.user.username}" /></h3>
                 <a href="${pageContext.request.contextPath}/logout" class="logout"
                    onclick="return confirm('Are you sure you want to logout?');">Logout</a>
               </div>
             ============================================================ --%>
        <div class="usersession">
          <h3><c:out value="${sessionScope.user.username}" /></h3>
          <a href="${pageContext.request.contextPath}/logout" class="logout"
             onclick="return confirm('Are you sure you want to logout?');">Logout</a>
        </div>
      </header>

      <nav class="navbar">
        <ul>
          <li><a href="${pageContext.request.contextPath}/topic">&lt; Back (Topic List)</a></li>
          <li><p>${empty topic ? 'Add Topic' : 'Edit Topic'}</p></li>
          <li></li>
        </ul>
      </nav>

      <main class="content">
        <div class="topicContainer">
          <%-- ============================================================
               TODO 5: Use c:choose for Error/Success Message Display
               ============================================================
               Replace the simple c:if error display with a
               c:choose/c:when/c:otherwise block.

               Currently, the page uses:
                 <c:if test="${not empty error}">
                   <p style="color: red; ...">${error}</p>
                 </c:if>

               This only handles ONE condition — there's no "else" branch.
               What if we also want to show a success message? With c:if
               alone, we'd need TWO separate c:if blocks and hope they
               don't both trigger.

               c:choose works like Java's if/else-if/else:

                 <c:choose>
                   <c:when test="${condition1}">
                     ... runs if condition1 is true ...
                   </c:when>
                   <c:when test="${condition2}">
                     ... runs if condition2 is true (and condition1 was false) ...
                   </c:when>
                   <c:otherwise>
                     ... runs if NO conditions matched (the "else") ...
                   </c:otherwise>
                 </c:choose>

               CONCEPT: c:choose vs c:if
                 - c:if = single condition, no else branch
                 - c:choose = multiple branches, only ONE executes
                 - c:when = each condition to check (like else-if)
                 - c:otherwise = the default/fallback (like else)

               Use this structure to show error OR success OR nothing:

                 <c:choose>
                   <c:when test="${not empty error}">
                     <p style="color: red; text-align: center; padding: 5px;">
                       <c:out value="${error}" />
                     </p>
                   </c:when>
                   <c:when test="${not empty success}">
                     <p style="color: green; text-align: center; padding: 5px;">
                       <c:out value="${success}" />
                     </p>
                   </c:when>
                 </c:choose>

               NOTE: We also use c:out here to escape the message
               content — consistent with TODO 4's XSS protection.

               The success message isn't set by the current servlet,
               but this prepares the page for future enhancements
               (e.g., "Topic saved successfully!"). The c:otherwise
               is optional — omitting it means nothing shows when
               there's no error and no success message.

               The complete code:

                 <c:choose>
                   <c:when test="${not empty error}">
                     <p style="color: red; text-align: center; padding: 5px;">
                       <c:out value="${error}" />
                     </p>
                   </c:when>
                   <c:when test="${not empty success}">
                     <p style="color: green; text-align: center; padding: 5px;">
                       <c:out value="${success}" />
                     </p>
                   </c:when>
                 </c:choose>
               ============================================================ --%>
          <c:choose>
            <c:when test="${not empty error}">
              <p style="color: red; text-align: center; padding: 5px;">
                <c:out value="${error}" />
              </p>
            </c:when>
            <c:when test="${not empty success}">
              <p style="color: green; text-align: center; padding: 5px;">
                <c:out value="${success}" />
              </p>
            </c:when>
          </c:choose>
          <%-- ============================================================
               TODO 6: Use c:out for Pre-filled Form Values
               ============================================================
               In the form below, the topic name input uses raw EL to
               pre-fill the value when editing:

                 value="${empty topic ? '' : topic.name}"

               This is vulnerable to XSS! If a topic name contains
               HTML characters (like quotes or angle brackets), they
               could break the form or execute scripts.

               Replace it with c:out using the default attribute:

                 value="<c:out value='${topic.name}' default='' />"

               CONCEPT: c:out has a "default" attribute that provides
               a fallback value when the expression is null or empty.

                 <c:out value="${topic.name}" default="" />

               This does TWO things:
                 1. If topic is null (add mode), outputs "" (empty)
                 2. If topic exists (edit mode), outputs the name
                    with HTML characters escaped

               Compare the approaches:
                 ${topic.name}                        -> raw, XSS vulnerable
                 ${empty topic ? '' : topic.name}     -> raw with null check
                 <c:out value="${topic.name}" default="" /> -> escaped + null-safe

               The c:out approach is both SAFER and CLEANER.

               The complete code:

                 <input type="text" placeholder="Topic Name" name="topic"
                        id="topic" value="<c:out value='${topic.name}' default='' />" />
               ============================================================ --%>
          <form action="${pageContext.request.contextPath}/topic" method="post">
            <input type="hidden" name="action" value="${empty topic ? 'add' : 'edit'}" />
            <c:if test="${not empty topic}">
              <input type="hidden" name="topicid" value="${topic.id}" />
            </c:if>
            <div class="topicItem">
              <label for="topic">Topic: </label>
              <input type="text" placeholder="Topic Name" name="topic"
                     id="topic" value="<c:out value='${topic.name}' default='' />" />
            </div>
            <div class="submit">
              <button type="submit">Save</button>
            </div>
          </form>
        </div>
      </main>

      <footer class="footer">
        <h3>&copy; Learning Logs</h3>
      </footer>

    </div>
  </body>
</html>
