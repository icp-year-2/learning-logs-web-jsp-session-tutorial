package com.learninglogs.dao;

import com.learninglogs.entity.User;
import com.learninglogs.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User DAO Implementation — JDBC operations for users.
 * New for Week 5: handles user registration and lookup.
 *
 * Follows the same JDBC pattern as TopicDaoImpl and EntryDaoImpl:
 *   1. Get connection
 *   2. Prepare SQL with ? placeholders
 *   3. Set parameters
 *   4. Execute query/update
 *   5. Close connection in finally block
 */
public class UserDaoImpl implements UserDao {

    // ============================================================
    // TODO 2: User DAO Implementation
    // ============================================================
    // Implement the THREE methods from UserDao interface using JDBC.
    // Follow the same pattern as TopicDaoImpl (provided complete).
    //
    // 1. insertUser(User user):
    //    - First check if username already exists (findByUsername)
    //    - Then check if email already exists (findByEmail)
    //    - If either exists, return false (duplicate)
    //    - SQL: INSERT INTO users (username, email, password) VALUES (?, ?, ?)
    //    - Note: the password passed here is ALREADY hashed by PasswordUtil
    //
    // 2. findByUsername(String username):
    //    - SQL: SELECT * FROM users WHERE LOWER(username) = LOWER(?)
    //    - LOWER() makes the search case-insensitive
    //    - Returns a User object built from the ResultSet, or null
    //
    // 3. findByEmail(String email):
    //    - SQL: SELECT * FROM users WHERE LOWER(email) = LOWER(?)
    //    - Same pattern as findByUsername but searches by email
    //    - Returns a User object or null
    //
    // CONCEPT: The duplicate check in insertUser happens at the Java
    // level (calling findByUsername/findByEmail before INSERT). The
    // database also has UNIQUE constraints on username and email as
    // a safety net, but checking in Java lets us return a meaningful
    // error message to the user.
    //
    // The complete code:
    //
    //   @Override
    //   public boolean insertUser(User user) {
    //       if (findByUsername(user.getUsername()) != null) {
    //           System.out.println("Username already exists: " + user.getUsername());
    //           return false;
    //       }
    //       if (findByEmail(user.getEmail()) != null) {
    //           System.out.println("Email already exists: " + user.getEmail());
    //           return false;
    //       }
    //
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setString(1, user.getUsername());
    //           statement.setString(2, user.getEmail());
    //           statement.setString(3, user.getPassword());
    //           statement.executeUpdate();
    //           return true;
    //       } catch (SQLException e) {
    //           System.out.println("Error inserting user: " + e.getMessage());
    //           return false;
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //   }
    //
    //   @Override
    //   public User findByUsername(String username) {
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "SELECT * FROM users WHERE LOWER(username) = LOWER(?)";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setString(1, username);
    //           ResultSet rs = statement.executeQuery();
    //           if (rs.next()) {
    //               return new User(
    //                   rs.getInt("id"),
    //                   rs.getString("username"),
    //                   rs.getString("email"),
    //                   rs.getString("password"),
    //                   rs.getTimestamp("created_at"),
    //                   rs.getTimestamp("updated_at")
    //               );
    //           }
    //       } catch (SQLException e) {
    //           System.out.println("Error finding user by username: " + e.getMessage());
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //       return null;
    //   }
    //
    //   @Override
    //   public User findByEmail(String email) {
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setString(1, email);
    //           ResultSet rs = statement.executeQuery();
    //           if (rs.next()) {
    //               return new User(
    //                   rs.getInt("id"),
    //                   rs.getString("username"),
    //                   rs.getString("email"),
    //                   rs.getString("password"),
    //                   rs.getTimestamp("created_at"),
    //                   rs.getTimestamp("updated_at")
    //               );
    //           }
    //       } catch (SQLException e) {
    //           System.out.println("Error finding user by email: " + e.getMessage());
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //       return null;
    //   }
    //
    // ============================================================
    @Override
    public boolean insertUser(User user) {
        if (findByUsername(user.getUsername()) != null) {
            System.out.println("Username already exists: " + user.getUsername());
            return false;
        }
        if (findByEmail(user.getEmail()) != null) {
            System.out.println("Email already exists: " + user.getEmail());
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public User findByUsername(String username) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE LOWER(username) = LOWER(?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding user by username: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE LOWER(email) = LOWER(?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding user by email: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }
}
