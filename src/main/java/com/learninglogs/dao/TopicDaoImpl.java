package com.learninglogs.dao;

import com.learninglogs.entity.Topic;
import com.learninglogs.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Topic DAO Implementation — JDBC operations for topics.
 * Complete from Week 2: insertTopic, fetchAllTopics, findTopicByName.
 * Week 4 adds: findTopicById, updateTopic, deleteTopic, searchTopics.
 *
 * Week 5 changes:
 *   - insertTopic: SQL now includes user_id column
 *   - All Topic constructors: now include rs.getInt("user_id")
 *
 * Week 7 adds:
 *   - fetchAllTopicsByUserId: fetch topics for a specific user
 *   - searchTopicsByUserId: search topics for a specific user
 */
public class TopicDaoImpl implements TopicDao {

    @Override
    public boolean insertTopic(Topic topic) {
        if (findTopicByName(topic.getName()) != null) {
            System.out.println("Topic already exists: " + topic.getName());
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            // UPDATED for Week 5 — added user_id column
            // Week 4 was: INSERT INTO topics (name) VALUES (?)
            String sql = "INSERT INTO topics (name, user_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, topic.getName());
            statement.setInt(2, topic.getUserId());  // NEW for Week 5
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting topic: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public ArrayList<Topic> fetchAllTopics() {
        ArrayList<Topic> topics = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM topics";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                // UPDATED for Week 5 — added rs.getInt("user_id")
                Topic topic = new Topic(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                topics.add(topic);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching topics: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return topics;
    }

    @Override
    public Topic findTopicByName(String name) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM topics WHERE LOWER(name) = LOWER(?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Topic(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding topic: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public Topic findTopicById(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM topics WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new Topic(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error finding topic: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean updateTopic(Topic topic) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE topics SET name = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, topic.getName());
            statement.setInt(2, topic.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating topic: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean deleteTopic(int id) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM topics WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting topic: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public ArrayList<Topic> searchTopics(String keyword) {
        ArrayList<Topic> topics = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM topics WHERE LOWER(name) LIKE LOWER(?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Topic topic = new Topic(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                topics.add(topic);
            }
        } catch (SQLException e) {
            System.out.println("Error searching topics: " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return topics;
    }

    // ============================================================
    // TODO 3: Implement User-Scoped Topic Queries
    // ============================================================
    // Implement the two methods you declared in TopicDao (TODO 2).
    //
    // 1. fetchAllTopicsByUserId(int userId)
    //    - Same as fetchAllTopics() but with: WHERE user_id = ?
    //    - Use PreparedStatement to set the userId parameter
    //
    // 2. searchTopicsByUserId(int userId, String keyword)
    //    - Same as searchTopics() but with: WHERE user_id = ? AND LOWER(name) LIKE LOWER(?)
    //    - Two parameters: userId and "%" + keyword + "%"
    //
    // CONCEPT: These are the same JDBC patterns you've used since Week 2.
    // The only difference is the WHERE clause filters by user_id.
    //
    // This is what makes topics USER-SCOPED — each user only sees
    // their own topics. Without this, all users share the same topics
    // (which is how the app worked in Weeks 4-6 with hardcoded userId=1).
    //
    // The complete code:
    //
    //   @Override
    //   public ArrayList<Topic> fetchAllTopicsByUserId(int userId) {
    //       ArrayList<Topic> topics = new ArrayList<>();
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "SELECT * FROM topics WHERE user_id = ?";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setInt(1, userId);
    //           ResultSet rs = statement.executeQuery();
    //           while (rs.next()) {
    //               Topic topic = new Topic(
    //                   rs.getInt("id"),
    //                   rs.getString("name"),
    //                   rs.getInt("user_id"),
    //                   rs.getTimestamp("created_at"),
    //                   rs.getTimestamp("updated_at")
    //               );
    //               topics.add(topic);
    //           }
    //       } catch (SQLException e) {
    //           System.out.println("Error fetching topics by user: " + e.getMessage());
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //       return topics;
    //   }
    //
    //   @Override
    //   public ArrayList<Topic> searchTopicsByUserId(int userId, String keyword) {
    //       ArrayList<Topic> topics = new ArrayList<>();
    //       Connection conn = null;
    //       try {
    //           conn = DatabaseConnection.getConnection();
    //           String sql = "SELECT * FROM topics WHERE user_id = ? AND LOWER(name) LIKE LOWER(?)";
    //           PreparedStatement statement = conn.prepareStatement(sql);
    //           statement.setInt(1, userId);
    //           statement.setString(2, "%" + keyword + "%");
    //           ResultSet rs = statement.executeQuery();
    //           while (rs.next()) {
    //               Topic topic = new Topic(
    //                   rs.getInt("id"),
    //                   rs.getString("name"),
    //                   rs.getInt("user_id"),
    //                   rs.getTimestamp("created_at"),
    //                   rs.getTimestamp("updated_at")
    //               );
    //               topics.add(topic);
    //           }
    //       } catch (SQLException e) {
    //           System.out.println("Error searching topics by user: " + e.getMessage());
    //       } finally {
    //           DatabaseConnection.closeConnection(conn);
    //       }
    //       return topics;
    //   }
    //
    // ============================================================
    @Override
    public ArrayList<Topic> fetchAllTopicsByUserId(int userId) {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Topic> searchTopicsByUserId(int userId, String keyword) {
        return new ArrayList<>();
    }
}
