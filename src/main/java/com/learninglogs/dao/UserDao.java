package com.learninglogs.dao;

import com.learninglogs.entity.User;

/**
 * User DAO Interface — defines database operations for users.
 * New for Week 5: supports registration and login.
 */
public interface UserDao {

    // ============================================================
    // TODO 1: User DAO Interface — Method Signatures
    // ============================================================
    // Define THREE method signatures for user database operations.
    //
    // This interface follows the same pattern as TopicDao and EntryDao:
    // - Define what operations are available (the "contract")
    // - The implementation class (UserDaoImpl) provides the JDBC code
    //
    // Methods to declare:
    //   1. boolean insertUser(User user)
    //      - Inserts a new user into the database
    //      - Returns true on success, false on failure (e.g., duplicate)
    //
    //   2. User findByUsername(String username)
    //      - Finds a user by their username
    //      - Returns the User object, or null if not found
    //      - Used by RegisterServlet to check for duplicate usernames
    //        and by LoginServlet (workshop) to find the user logging in
    //
    //   3. User findByEmail(String email)
    //      - Finds a user by their email
    //      - Returns the User object, or null if not found
    //      - Used by RegisterServlet to check for duplicate emails
    //
    // CONCEPT: An interface defines the "what" (method signatures),
    // not the "how" (implementation). This is the DAO pattern —
    // the servlet calls these methods without knowing the JDBC details.
    //
    // The complete code:
    //
    //   boolean insertUser(User user);
    //   User findByUsername(String username);
    //   User findByEmail(String email);
    //
    // ============================================================
    boolean insertUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
}
