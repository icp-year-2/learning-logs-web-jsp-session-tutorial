<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Learning Log</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/main.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/entry-add.css" />
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
        <div class="usersession">
          <h3>Username</h3>
          <a href="#" class="logout">Logout</a>
        </div>
      </header>

      <nav class="navbar">
        <ul>
          <li><a href="${pageContext.request.contextPath}/entry?topicid=${param.topicid}">&lt; Back (Entries)</a></li>
          <li><p>${empty entry ? 'Add Entry' : 'Edit Entry'}</p></li>
          <li></li>
        </ul>
      </nav>

      <%-- ============================================================
           TODO 5: Use c:choose for Error/Success Display + c:out
           ============================================================
           Replace the simple c:if error display with a
           c:choose/c:when block — same pattern as topic-add-edit.jsp
           from the Tutorial (TODO 5).

           Currently, entry-add-edit.jsp uses:
             <c:if test="${not empty error}">
               <p style="color: red; ...">${error}</p>
             </c:if>

           This only handles ONE condition. Replace it with c:choose
           to handle error OR success OR nothing:

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

           ALSO: Replace the raw ${topic.name} in the heading with
           c:out for XSS protection (same fix as entry-list.jsp):

             <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

           CONCEPT: c:choose vs c:if
             - c:if = single condition, no else branch
             - c:choose = multiple branches, only ONE executes
             - c:when = each condition to check (like else-if)
             - c:otherwise = the default/fallback (like else)

           NOTE: We also use c:out for the error and success messages
           — consistent with the XSS protection pattern. The success
           message isn't set by the current servlet, but this prepares
           the page for future enhancements (same reasoning as the
           tutorial's topic-add-edit.jsp).

           The complete code:

             <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

             <div class="form-card">
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
      <main class="content">
        <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

        <div class="form-card">
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
               The entry form pre-fills values for edit mode using raw EL:

                 value="${empty entry ? '' : entry.title}"

               This is vulnerable to XSS! Replace with c:out using the
               default attribute — same pattern as topic-add-edit.jsp
               (Tutorial TODO 6).

               For INPUT fields (title, link, image), use c:out inside
               the value attribute:

                 value="<c:out value='${entry.title}' default='' />"

               For TEXTAREA (text/description), use c:out between the
               opening and closing tags:

                 <textarea ...><c:out value="${entry.text}" default="" /></textarea>

               CONCEPT: c:out's "default" attribute provides a fallback
               value when the expression is null:
                 - Add mode: entry is null -> outputs "" (empty)
                 - Edit mode: entry exists -> outputs escaped value

               This replaces the verbose EL ternary:
                 ${empty entry ? '' : entry.title}  (raw, XSS vulnerable)
               with:
                 <c:out value='${entry.title}' default='' />  (escaped + null-safe)

               Apply to ALL FOUR form fields:
                 1. title input: value="<c:out value='${entry.title}' default='' />"
                 2. text textarea: <c:out value="${entry.text}" default="" />
                 3. link input: value="<c:out value='${entry.link}' default='' />"
                 4. image input: value="<c:out value='${entry.image}' default='' />"

               The complete form code:

                 <form action="${pageContext.request.contextPath}/entry" method="post">
                   <input type="hidden" name="action" value="${empty entry ? 'add' : 'edit'}" />
                   <input type="hidden" name="topicid" value="${param.topicid}" />
                   <c:if test="${not empty entry}">
                     <input type="hidden" name="entryid" value="${entry.id}" />
                   </c:if>

                   <div class="form-row">
                     <label for="title">Title:</label>
                     <input type="text" id="title" name="title" placeholder="Title"
                            value="<c:out value='${entry.title}' default='' />" />
                   </div>

                   <div class="form-row">
                     <label for="text">Description:</label>
                     <textarea id="text" name="text" rows="4"
                       placeholder="Description"><c:out value="${entry.text}" default="" /></textarea>
                   </div>

                   <div class="form-row">
                     <label for="link">Link:</label>
                     <input type="text" id="link" name="link" placeholder="Link URL"
                            value="<c:out value='${entry.link}' default='' />" />
                   </div>

                   <div class="form-row">
                     <label for="image">Image:</label>
                     <input type="text" id="image" name="image" placeholder="Image URL"
                            value="<c:out value='${entry.image}' default='' />" />
                   </div>

                   <div class="form-actions">
                     <button type="submit">Save</button>
                   </div>
                 </form>
               ============================================================ --%>
          <form action="${pageContext.request.contextPath}/entry" method="post">
            <input type="hidden" name="action" value="${empty entry ? 'add' : 'edit'}" />
            <input type="hidden" name="topicid" value="${param.topicid}" />
            <c:if test="${not empty entry}">
              <input type="hidden" name="entryid" value="${entry.id}" />
            </c:if>

            <div class="form-row">
              <label for="title">Title:</label>
              <input type="text" id="title" name="title" placeholder="Title"
                     value="<c:out value='${entry.title}' default='' />" />
            </div>

            <div class="form-row">
              <label for="text">Description:</label>
              <textarea id="text" name="text" rows="4"
                placeholder="Description"><c:out value="${entry.text}" default="" /></textarea>
            </div>

            <div class="form-row">
              <label for="link">Link:</label>
              <input type="text" id="link" name="link" placeholder="Link URL"
                     value="<c:out value='${entry.link}' default='' />" />
            </div>

            <div class="form-row">
              <label for="image">Image:</label>
              <input type="text" id="image" name="image" placeholder="Image URL"
                     value="<c:out value='${entry.image}' default='' />" />
            </div>

            <div class="form-actions">
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
