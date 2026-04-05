<%-- ============================================================
     TODO 1: Add the fmt Taglib Directive
     ============================================================
     You already have the core JSTL taglib (prefix="c"). Now add
     the FORMATTING taglib so you can use <fmt:formatDate>.

     Add this directive AFTER the existing core taglib:

       <%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

     CONCEPT: This is the same directive you added to topic-list.jsp
     in the Tutorial. Every JSP that needs date formatting must
     import the fmt taglib separately — it's not shared between files.

     NOTE: The URI is "jakarta.tags.fmt" (Jakarta EE namespace).
     Same pattern as "jakarta.tags.core" — NOT the old
     "http://java.sun.com/jsp/jstl/fmt" from the lecturer's slides.

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/entry-list.css" />
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

      <%-- ============================================================
           TODO 2: Display Entry Count Using .size()
           ============================================================
           Show how many entries exist in the navbar title area.

           Replace the static text "Entries List" with a dynamic count
           using the .size() method on the entries collection:

             <p>Entries List (${entries.size()})</p>

           CONCEPT: Same pattern as topic-list.jsp where you used
           ${topics.size()} to show the topic count. EL can call
           Java methods directly — ${entries.size()} calls .size()
           on the ArrayList that EntryServlet set as a request
           attribute.

           Examples of what this displays:
             - "Entries List (3)" — when 3 entries exist for this topic
             - "Entries List (0)" — when no entries exist yet

           The complete code:

             <p>Entries List (${entries.size()})</p>
           ============================================================ --%>
      <nav class="navbar">
        <ul>
          <li><a href="${pageContext.request.contextPath}/topic">&lt; Back (Topics)</a></li>
          <li><p>Entries List (${entries.size()})</p></li>
          <li><a href="${pageContext.request.contextPath}/entry?action=new&topicid=${topic.id}">+New Entry</a></li>
        </ul>
      </nav>

      <%-- ============================================================
           TODO 3: Use c:out for the Topic Name (XSS Fix)
           ============================================================
           Replace the raw ${topic.name} in the topic title heading
           with c:out for XSS protection.

           Currently the page uses:
             <h2 class="topic-title">${topic.name}</h2>

           Change it to:
             <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

           WHY THIS MATTERS — remember the Tutorial?
           When you created a topic named:
             (script)alert('xss')(/script)

           The topic-list.jsp displayed it safely because you added
           c:out there (Tutorial TODO 4). But when you CLICKED that
           topic to view its entries, THIS page (entry-list.jsp)
           triggered the alert popup — because it was using raw
           ${topic.name} without c:out.

           This is the XSS vulnerability from the Tutorial — and
           NOW you're fixing it. After this change, the same data
           will be safe on BOTH pages.

           ALSO: The search bar has a similar issue — the search
           keyword value uses raw ${searchKeyword} which could be
           exploited. Fix it too:

             Currently:
               value="${searchKeyword}"

             Change to:
               value="<c:out value='${searchKeyword}' default='' />"

           CONCEPT: This demonstrates a critical security lesson:
           c:out must be applied EVERYWHERE user data is displayed,
           not just on one page. The same data (topic.name) was safe
           on topic-list.jsp but dangerous here — because each JSP
           outputs data independently.

           The complete code:

             <input type="text" name="search" placeholder="Search..."
                    value="<c:out value='${searchKeyword}' default='' />" />

             <h2 class="topic-title"><c:out value="${topic.name}" /></h2>
           ============================================================ --%>
      <main class="content">
        <div class="search-bar">
          <form action="${pageContext.request.contextPath}/entry" method="get">
            <input type="hidden" name="action" value="search" />
            <input type="hidden" name="topicid" value="${topic.id}" />
            <label for="search">Entry: </label>
            <input type="text" name="search" placeholder="Search..." value="<c:out value='${searchKeyword}' default='' />" />
            <button type="submit">SEARCH</button>
          </form>
        </div>

        <h2 class="topic-title"><c:out value="${topic.name}" /></h2>

        <div class="entry-grid">
          <%-- ============================================================
               TODO 4: Format Dates + Use c:out for Entry Fields
               ============================================================
               Inside each entry card, make TWO types of changes:

               1. FORMAT THE DATE — replace the raw timestamp with
                  fmt:formatDate (same pattern as topic-list.jsp):

                  Currently:
                    <p class="date">Date: ${entry.createdAt}</p>

                  Change to:
                    <p class="date">Date:
                      <fmt:formatDate value="${entry.createdAt}"
                        pattern="MMM d, yyyy" />
                    </p>

                  This turns "2026-03-24 10:30:15.0" into "Mar 24, 2026".

               2. USE c:out FOR ENTRY FIELDS — replace raw EL with
                  c:out for all user-provided data:

                  a) Entry title:
                     ${entry.title}  ->  <c:out value="${entry.title}" />

                  b) Entry text:
                     ${entry.text}  ->  <c:out value="${entry.text}" />

                  c) Entry link (the display text, not the href):
                     ${entry.link}  ->  <c:out value="${entry.link}" />

                  NOTE: The href="${entry.link}" stays as raw EL because
                  it's a URL attribute, not displayed text. The DISPLAY
                  text inside the <a> tag is what needs escaping.

               CONCEPT: entry-list.jsp has MORE places to apply c:out
               than topic-list.jsp did. Topic list only had the topic
               name. Entry cards have title, text, AND link — all from
               user input, all needing XSS protection.

               The complete entry card code:

                 <div class="entry-card">
                   <div class="entry-header">
                     <div>
                       <h3><c:out value="${entry.title}" /></h3>
                       <p class="date">Date:
                         <fmt:formatDate value="${entry.createdAt}"
                           pattern="MMM d, yyyy" />
                       </p>
                     </div>
                     <div class="photo">Photo</div>
                   </div>

                   <p class="entry-text"><c:out value="${entry.text}" /></p>

                   <p class="link">
                     Link: <a href="${entry.link}"><c:out value="${entry.link}" /></a>
                   </p>

                   ... (entry-actions div stays the same — no user text) ...
                 </div>
               ============================================================ --%>
          <c:forEach var="entry" items="${entries}">
            <div class="entry-card">
              <div class="entry-header">
                <div>
                  <h3><c:out value="${entry.title}" /></h3>
                  <p class="date">Date:
                    <fmt:formatDate value="${entry.createdAt}"
                      pattern="MMM d, yyyy" />
                  </p>
                </div>
                <div class="photo">Photo</div>
              </div>

              <p class="entry-text"><c:out value="${entry.text}" /></p>

              <p class="link">
                Link: <a href="${entry.link}"><c:out value="${entry.link}" /></a>
              </p>

              <div class="entry-actions">
                <a href="${pageContext.request.contextPath}/entry?action=edit&entryid=${entry.id}&topicid=${topic.id}">
                  <button>Edit</button>
                </a>
                <form action="${pageContext.request.contextPath}/entry" method="post">
                  <input type="hidden" name="action" value="delete" />
                  <input type="hidden" name="entryid" value="${entry.id}" />
                  <input type="hidden" name="topicid" value="${topic.id}" />
                  <button class="danger" type="submit"
                    onclick="return confirm('Are you sure you want to delete?');">
                    Delete
                  </button>
                </form>
              </div>
            </div>
          </c:forEach>
        </div>
      </main>

      <footer class="footer">
        <h3>&copy; Learning Logs</h3>
      </footer>

    </div>
  </body>
</html>
