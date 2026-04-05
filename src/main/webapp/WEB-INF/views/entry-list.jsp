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

      <nav class="navbar">
        <ul>
          <li><a href="${pageContext.request.contextPath}/topic">&lt; Back (Topics)</a></li>
          <li><p>Entries List (${entries.size()})</p></li>
          <li><a href="${pageContext.request.contextPath}/entry?action=new&topicid=${topic.id}">+New Entry</a></li>
        </ul>
      </nav>

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
