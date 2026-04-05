# Learning Logs — Week 7 Session Management Tutorial

## Adding Login Sessions, Logout, User-Scoped Topics, and Route Protection

> **In Weeks 5-6, you built login and registration — but the app didn't remember who was logged in.** Now you'll add session management so each user sees only their own topics, plus an authentication filter that protects all routes.

---

## The Problem

Right now, the app has a critical gap:

| What Works | What's Missing |
|-----------|---------------|
| Users can register and log in | Login doesn't persist — refreshing loses the login state |
| Topics have a `user_id` column | TopicServlet uses `fetchAllTopics()` with no user filter — ALL topics from every user are visible |
| Header shows "Username" | It's static text — doesn't show who's actually logged in |
| Logout button exists | It links to `#` — clicking it does nothing |
| Anyone can access `/topic` directly | No protection — unauthenticated users can bypass login |

**After this tutorial**, all of these will be fixed.

---

## What's Already Done

Everything from Week 6 is provided complete:

| Category | Files | Status |
|----------|-------|--------|
| Entities | `Topic.java` (with userId), `Entry.java`, `User.java` | Provided |
| DAOs | `TopicDao`/`Impl`, `EntryDao`/`Impl`, `UserDao`/`Impl` | Provided |
| Servlets | `TopicServlet`, `EntryServlet`, `RegisterServlet`, `LoginServlet` | Provided |
| Utils | `DatabaseConnection`, `ValidationUtil`, `PasswordUtil` | Provided |
| JSPs | All 6 JSPs with JSTL/c:out XSS protection | Provided |
| Error page | `error404.jsp` + web.xml config | Provided |
| CSS | All 7 CSS files | Provided |
| SQL | Schema + seed data (now with 2 test users) | Provided |
| References | 5 reference guides (sessions, filters, session utility, cookies*, forward vs redirect) | Provided |

> *\*04-cookies.md is a preview for the Workshop, where you'll build a CookieUtil and use cookies for "remember username" functionality. The tutorial focuses on sessions, not cookies — but the reference is included so you can read ahead.*

---

## What You'll Build

9 TODOs across 9 files — building session management from the ground up:

| # | File | What You'll Build |
|---|------|-------------------|
| 1 | `SessionUtil.java` | **NEW** — Utility class wrapping HttpSession (set, get, invalidate) |
| 2 | `TopicDao.java` | Add user-scoped method signatures (fetchAllTopicsByUserId, searchTopicsByUserId) |
| 3 | `TopicDaoImpl.java` | Implement user-scoped queries with `WHERE user_id = ?` |
| 4 | `LoginServlet.java` | Store User object in session after successful login |
| 5 | `LogoutServlet.java` | **NEW** — Invalidate session and redirect to login |
| 6 | `TopicServlet.java` | Replace hardcoded `userId=1` with session user's ID |
| 7 | `topic-list.jsp` | Show logged-in username + working logout link |
| 8 | `topic-add-edit.jsp` | Show logged-in username + working logout link |
| 9 | `AuthenticationFilter.java` | **NEW** — Protect all routes, redirect unauthenticated users |

---

## Architecture

```
Week 7 adds session management layer:

                    ┌─────────────────────┐
                    │ AuthenticationFilter │ ← Intercepts ALL requests (TODO 9)
                    │    @WebFilter("/*")  │
                    └────────┬────────────┘
                             │
              ┌──────────────┼──────────────┐
              │              │              │
         /login         /topic         /logout
              │              │              │
       LoginServlet   TopicServlet   LogoutServlet
       (TODO 4)       (TODO 6)       (TODO 5)
              │              │              │
       Store user      Read user      Invalidate
       in session     from session     session
              │              │              │
              └──────────────┼──────────────┘
                             │
                      ┌──────┴──────┐
                      │ SessionUtil │ ← Central session management (TODO 1)
                      └─────────────┘
```

### Session Flow

```
Login:
  Browser → POST /login → LoginServlet verifies password
    → SessionUtil.setAttribute(request, "user", user)  ← stores User in session
    → Server sends JSESSIONID cookie to browser
    → Redirect to /topic

Every subsequent request:
  Browser sends JSESSIONID cookie → Server finds session
    → AuthenticationFilter checks: session has "user"?
    → Yes → allow through → TopicServlet reads user from session
    → No  → redirect to /login

Logout:
  Browser → GET /logout → LogoutServlet
    → SessionUtil.invalidateSession(request)  ← destroys session
    → Redirect to /login
    → AuthenticationFilter blocks all protected pages
```

---

## TODO Order Explained

The TODOs follow a bottom-up approach — build the foundation first, then use it:

```
TODO 1: SessionUtil ────────── Utility (foundation)
TODO 2: TopicDao ──────────── Interface (method signatures)
TODO 3: TopicDaoImpl ──────── Implementation (database queries)
TODO 4: LoginServlet ──────── Create session on login
TODO 5: LogoutServlet ─────── Destroy session on logout
TODO 6: TopicServlet ──────── Use session to scope topics to user
TODO 7: topic-list.jsp ────── Display session data in UI
TODO 8: topic-add-edit.jsp ── Display session data in UI
TODO 9: AuthenticationFilter ─ Protect all routes (uses SessionUtil)
```

**Why this order?**
- TODOs 1-3: Data layer first (DAO before Servlet — avoids runtime errors)
- TODOs 4-5: Session lifecycle (create and destroy)
- TODO 6: The big payoff — topics become user-scoped
- TODOs 7-8: Visual confirmation (see the username in the header)
- TODO 9: Route protection last (relies on everything else working)

---

## Project Structure

```
learning-logs-web-jsp-session-tutorial/
├── README.md
├── pom.xml
├── sql/
│   ├── learninglog.sql                     (schema — unchanged from Week 5)
│   └── seed.sql                            (UPDATED — 2 test users for isolation testing)
├── references/
│   ├── 01-http-sessions.md                   (Tutorial — core concept)
│   ├── 02-servlet-filters.md                 (Tutorial — for TODO 9)
│   ├── 03-session-utility-pattern.md         (Tutorial — for TODO 1)
│   ├── 04-cookies.md                         (Workshop preview — cookies used in Workshop)
│   └── 05-forward-vs-redirect.md             (Tutorial — filter/servlet context)
├── src/main/
│   ├── java/com/learninglogs/
│   │   ├── controller/
│   │   │   ├── TopicServlet.java           ← TODO 6: use session userId
│   │   │   ├── EntryServlet.java           (provided — unchanged)
│   │   │   ├── LoginServlet.java           ← TODO 4: store session
│   │   │   ├── RegisterServlet.java        (provided — unchanged)
│   │   │   ├── LogoutServlet.java          ← TODO 5: NEW — logout
│   │   │   └── filter/
│   │   │       └── AuthenticationFilter.java ← TODO 9: NEW — route protection
│   │   ├── entity/
│   │   │   ├── Topic.java                  (provided)
│   │   │   ├── Entry.java                  (provided)
│   │   │   └── User.java                   (provided)
│   │   ├── dao/
│   │   │   ├── TopicDao.java               ← TODO 2: add user-scoped methods
│   │   │   ├── TopicDaoImpl.java           ← TODO 3: implement user-scoped queries
│   │   │   ├── EntryDao.java               (provided)
│   │   │   ├── EntryDaoImpl.java           (provided)
│   │   │   ├── UserDao.java                (provided)
│   │   │   └── UserDaoImpl.java            (provided)
│   │   └── utils/
│   │       ├── DatabaseConnection.java     (provided)
│   │       ├── ValidationUtil.java         (provided)
│   │       ├── PasswordUtil.java           (provided)
│   │       └── SessionUtil.java            ← TODO 1: NEW — session utility
│   └── webapp/
│       ├── error404.jsp                    (provided)
│       ├── static/
│       │   ├── css/                        (all provided — no changes)
│       │   ├── images/book.png
│       │   └── js/.gitkeep
│       └── WEB-INF/
│           ├── views/
│           │   ├── topic-list.jsp          ← TODO 7: user info + logout
│           │   ├── topic-add-edit.jsp      ← TODO 8: user info + logout
│           │   ├── entry-list.jsp          (provided — workshop scope)
│           │   ├── entry-add-edit.jsp      (provided — workshop scope)
│           │   ├── login.jsp               (provided)
│           │   └── register.jsp            (provided)
│           └── web.xml                     (provided — unchanged)
```

---

## Key Concepts

### HTTP is Stateless
Each HTTP request is independent — the server doesn't remember who sent the previous request. Sessions solve this by storing data on the server, linked to a cookie (JSESSIONID) in the browser.

### Sessions vs Cookies

| | Cookie | Session |
|---|---|---|
| **Where** | Browser (client-side) | Server (server-side) |
| **What's stored** | Small text (username, preferences) | Anything (User objects, lists) |
| **Size** | ~4 KB limit | No practical limit |
| **Security** | User can see and edit it | Hidden from user |
| **Lifetime** | Controlled by `maxAge` | Controlled by timeout |

**How they connect:** Sessions use ONE cookie internally — `JSESSIONID`. This cookie is just a random ID (like a locker key). The actual data (your User object) stays safely on the server.

```
1. Browser → POST /login (username, password)
2. Server: "Password correct!" → creates session (server-side locker)
   → stores User object in session
   → sends back: Set-Cookie: JSESSIONID=ABC123
3. Browser saves JSESSIONID cookie automatically
4. Browser → GET /topic (Cookie: JSESSIONID=ABC123)
5. Server reads ABC123 → finds the session → gets User object
   → "This is testuser — show their topics"
6. Every subsequent request: browser keeps sending JSESSIONID=ABC123
```

**On logout:** `session.invalidate()` destroys the server-side data. The browser still has the JSESSIONID cookie, but the server no longer recognizes it — the AuthenticationFilter sees no valid session and redirects to `/login`.

> **Note:** The JSESSIONID cookie contains NO user data — just a random ID. All actual data stays on the server. That's why sessions are secure for authentication. In the Workshop, you'll create your OWN cookies (like remembering the last username on the login form) — a different use case from the automatic JSESSIONID.

### See It In Your Browser

After completing the TODOs and logging in, you can see the session cookie:

1. Open **DevTools** (F12 or right-click → Inspect)
2. Go to **Application** tab (Chrome) or **Storage** tab (Firefox)
3. Click **Cookies** → `http://localhost:9090`
4. You'll see `JSESSIONID` with a value like `A1B2C3D4E5F6...`

You can also see it in the **Network** tab:
- Click any request → **Request Headers** → `Cookie: JSESSIONID=...`
- On the login response → **Response Headers** → `Set-Cookie: JSESSIONID=...`

Try this experiment:
1. Log in and note the JSESSIONID value
2. Delete the cookie (right-click → Delete in DevTools)
3. Refresh the page — you'll be redirected to login (server can't find your session)

### HttpSession API
```java
// Create or get existing session
HttpSession session = request.getSession();

// Get existing session (returns null if none exists)
HttpSession session = request.getSession(false);

// Store data
session.setAttribute("user", userObject);

// Read data
User user = (User) session.getAttribute("user");

// Set timeout (30 minutes)
session.setMaxInactiveInterval(30 * 60);

// Destroy session
session.invalidate();
```

### Session Scope in JSP (EL)
```jsp
<%-- Read from session scope --%>
${sessionScope.user.username}

<%-- EL calls user.getUsername() automatically --%>
<%-- "sessionScope" tells EL to look in the session, not request --%>
```

### Servlet Filters
Filters are middleware — they intercept requests before they reach servlets:
```
Client Request → Filter → Servlet → Response
                   ↓
            (if blocked)
                   ↓
              Redirect to /login
```

### Forward vs Redirect
| | Forward | Redirect |
|---|---|---|
| URL in browser | Stays the same | Changes |
| Request attributes | Preserved | Lost |
| Use for | Errors (keep form data) | Success (PRG pattern) |
| Filter interaction | Internal — filter doesn't re-intercept | New request — filter intercepts again |

---

## Test Users

The seed data includes two users for testing topic isolation:

| Username | Password | Topics |
|----------|----------|--------|
| `testuser` | `Test@123` | Python, Web Development, Data Science, Machine Learning, Cybersecurity |
| `demouser` | `Test@123` | Java, Databases, Cloud Computing |

**Test topic isolation:** Log in as `testuser` — you should see 5 topics. Log out, then log in as `demouser` — you should see 3 different topics.

---

## Getting Started

### 1. Set Up the Database
Open phpMyAdmin (`http://localhost/phpmyadmin`) and run:
1. `sql/learninglog.sql` — creates the schema
2. `sql/seed.sql` — adds sample data (2 users + topics + entries)

### 2. Build and Run
```bash
mvn clean package cargo:run
```

### 3. Access the App
Open `http://localhost:9090/learning-logs/topic`

**Before completing the TODOs:** The app works but login doesn't persist, all topics from every user are visible (no user filtering), the header shows static "Username" text, and anyone can access any page directly by URL.

**After completing all TODOs:** Login persists via sessions, each user sees only their own topics, the header shows the logged-in username with a working logout link, and unauthenticated users are automatically redirected to the login page.

---

## Troubleshooting

| Problem | Cause | Fix |
|---------|-------|-----|
| Port 9090 in use | Another app using the port | `mvn clean package cargo:run -Dcargo.servlet.port=9191` |
| Database connection error | MySQL not running | Start XAMPP MySQL |
| `NullPointerException` in TopicServlet | Session user is null (TODO 4 not done yet) | Complete TODO 4 first, or log in before testing |
| Infinite redirect loop after TODO 9 | Filter redirects to /login, which redirects again | Make sure LoginServlet.doGet uses `forward` (not `sendRedirect`) |
| Login works but topics empty | Using wrong DAO method | Ensure TODO 6 uses `fetchAllTopicsByUserId` (not `fetchAllTopics`) |
| "Username" still showing in header | JSP TODO not done | Complete TODOs 7-8 |
| Entry pages still show "Username" | Expected — that's the Workshop | Entry pages are updated in the Workshop |
