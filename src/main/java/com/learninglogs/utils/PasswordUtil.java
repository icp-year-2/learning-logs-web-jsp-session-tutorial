package com.learninglogs.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil — BCrypt password hashing utility.
 * New for Week 5: bridges the lecture theory on hashing to actual code.
 *
 * Uses the jBCrypt library to hash and verify passwords.
 */
public class PasswordUtil {

    // ============================================================
    // TODO 3: BCrypt Password Utility
    // ============================================================
    // Implement TWO static methods that wrap the BCrypt library.
    // This bridges the lecture theory (salt, cost factor, hashing)
    // to actual code you'll use in RegisterServlet.
    //
    // Background (from lecture):
    // - BCrypt is a ONE-WAY hash function — you can hash a password,
    //   but you can NEVER reverse it back to the original.
    // - BCrypt automatically generates a random SALT (16 bytes) and
    //   embeds it in the hash output. No separate salt storage needed.
    // - The COST FACTOR controls how slow the hashing is:
    //   cost=10 means 2^10 = 1024 iterations. Higher = slower = more
    //   resistant to brute-force attacks, but slower login.
    // - Hash output format: $2a$[cost]$[22-char salt][31-char hash]
    //   Example: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
    //
    // Methods to implement:
    //
    // 1. getHashPassword(String inputPassword):
    //    - Generate a salt: BCrypt.gensalt(COST)
    //    - Hash the password with the salt: BCrypt.hashpw(inputPassword, salt)
    //    - Return the 60-character hash string
    //    - Used by RegisterServlet (TODO 9) before storing in database
    //
    // 2. checkPassword(String passwordTyped, String hashedPassword):
    //    - Compare a plaintext password against a stored hash
    //    - BCrypt.checkpw() extracts the salt from the hash,
    //      re-hashes the input, and compares
    //    - Returns true if they match, false otherwise
    //    - Used by LoginServlet in the workshop (not this tutorial)
    //
    // The complete code:
    //
    //   // Cost factor: 2^COST iterations. 10-12 is typical.
    //   // Higher = slower = more brute-force resistant, but slower login.
    //   private static final int COST = 10;
    //
    //   public static String getHashPassword(String inputPassword) {
    //       String salt = BCrypt.gensalt(COST);
    //       return BCrypt.hashpw(inputPassword, salt);
    //   }
    //
    //   public static boolean checkPassword(String passwordTyped, String hashedPassword) {
    //       return BCrypt.checkpw(passwordTyped, hashedPassword);
    //   }
    //
    // ============================================================

    // Cost factor: 2^COST iterations. 10-12 is typical.
    // Higher = slower = more brute-force resistant, but slower login.
    private static final int COST = 10;

    public static String getHashPassword(String inputPassword) {
        String salt = BCrypt.gensalt(COST);
        return BCrypt.hashpw(inputPassword, salt);
    }

    public static boolean checkPassword(String passwordTyped, String hashedPassword) {
        return BCrypt.checkpw(passwordTyped, hashedPassword);
    }
}
