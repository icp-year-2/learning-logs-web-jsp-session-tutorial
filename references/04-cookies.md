# Cookies in Jakarta EE

## What is a Cookie?

A **cookie** is a small piece of data that the server sends to the browser.
The browser stores it and sends it back with every subsequent request to the
same server.

```
1. Browser → Server:  POST /login (email, password)
2. Server → Browser:  Set-Cookie: JSESSIONID=abc123
3. Browser → Server:  GET /topic  (Cookie: JSESSIONID=abc123)
4. Browser → Server:  GET /entry  (Cookie: JSESSIONID=abc123)
   ... cookie sent automatically with every request ...
```

## Cookies vs Sessions

| Feature        | Cookie                         | Session                        |
|----------------|--------------------------------|--------------------------------|
| Stored where?  | Browser (client-side)          | Server (server-side)           |
| Size limit     | ~4 KB per cookie               | No practical limit             |
| Security       | Visible to user, can be edited | Hidden from user               |
| Lifetime       | Controlled by maxAge           | Controlled by timeout          |
| Use case       | Preferences, "remember me"     | Authentication, sensitive data |

**Important:** Sessions USE cookies internally. The `JSESSIONID` cookie is how
the server knows which session belongs to which browser.

## JSESSIONID — The Session Cookie

When you call `request.getSession()`, the server:

1. Creates a session object on the server
2. Generates a unique ID (e.g., `A1B2C3D4E5F6`)
3. Sends a `Set-Cookie: JSESSIONID=A1B2C3D4E5F6` header to the browser
4. On subsequent requests, the browser sends this cookie back
5. The server uses the ID to look up the correct session

You **never** manage JSESSIONID yourself — the server handles it automatically.

## Creating Cookies in Java

```java
import jakarta.servlet.http.Cookie;

// Create a cookie
Cookie cookie = new Cookie("theme", "dark");
cookie.setMaxAge(7 * 24 * 60 * 60);  // 7 days in seconds
cookie.setPath("/");                   // available to entire app
response.addCookie(cookie);            // send to browser
```

### Cookie Properties

| Property   | Method               | Description                              |
|------------|----------------------|------------------------------------------|
| Name       | `new Cookie(name, value)` | Cookie identifier (cannot change)   |
| Value      | `setValue(String)`   | Cookie data                              |
| Max Age    | `setMaxAge(int)`     | Lifetime in seconds (-1 = session cookie)|
| Path       | `setPath(String)`    | URL path where cookie is sent            |
| HttpOnly   | `setHttpOnly(true)`  | Prevents JavaScript access (security)    |
| Secure     | `setSecure(true)`    | Only sent over HTTPS                     |

### Max Age Values

| Value | Meaning                                            |
|-------|----------------------------------------------------|
| > 0   | Cookie expires after this many seconds             |
| 0     | Delete the cookie immediately                      |
| -1    | Session cookie — deleted when browser closes       |

## Reading Cookies

```java
Cookie[] cookies = request.getCookies();
if (cookies != null) {
    for (Cookie cookie : cookies) {
        if ("theme".equals(cookie.getName())) {
            String theme = cookie.getValue();  // "dark"
        }
    }
}
```

**Note:** `getCookies()` returns `null` (not an empty array) if no cookies exist.
Always check for null first!

## Deleting Cookies

There's no "delete" method. Instead, send the same cookie with `maxAge = 0`:

```java
Cookie cookie = new Cookie("theme", "");
cookie.setMaxAge(0);      // tells browser to delete it
cookie.setPath("/");      // must match the original path!
response.addCookie(cookie);
```

**Important:** The `path` must match the original cookie's path, or the browser
won't know which cookie to delete.

## Cookie Utility Pattern

Just like SessionUtil, you can create a CookieUtil for cleaner code:

```java
public class CookieUtil {

    public static void addCookie(HttpServletResponse response,
                                 String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getCookieValue(HttpServletRequest request,
                                        String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteCookie(HttpServletResponse response,
                                    String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
```

## Security Considerations

1. **Never store sensitive data in cookies** — they're visible to the user
   and can be modified. Store sensitive data in the session.

2. **Use HttpOnly** — `cookie.setHttpOnly(true)` prevents JavaScript from
   reading the cookie, protecting against XSS attacks.

3. **Use Secure in production** — `cookie.setSecure(true)` ensures the cookie
   is only sent over HTTPS, not plain HTTP.

4. **Don't trust cookie values** — always validate on the server side.
   A user can manually edit cookie values in their browser.

## Common Use Cases

| Use Case           | Cookie Name      | Example Value     |
|--------------------|------------------|-------------------|
| Session tracking   | JSESSIONID       | A1B2C3D4 (auto)   |
| Remember username  | lastUsername     | john@example.com   |
| Theme preference   | theme            | dark               |
| Language           | locale           | en-US              |
| Remember me        | rememberToken    | encrypted-token    |

## Key Points

- Cookies are **client-side** storage sent with every HTTP request
- Sessions use the **JSESSIONID** cookie internally (managed by the server)
- `setMaxAge(0)` deletes a cookie; `-1` makes it a session cookie
- Always null-check `getCookies()` before iterating
- **Never** store passwords or sensitive data in cookies
- Use `setHttpOnly(true)` and `setSecure(true)` for security
- The Cookie utility pattern keeps servlet code clean and consistent
