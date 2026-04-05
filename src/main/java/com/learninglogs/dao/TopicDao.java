package com.learninglogs.dao;

import com.learninglogs.entity.Topic;
import java.util.ArrayList;

/**
 * Topic DAO Interface — defines database operations for topics.
 * Complete from Week 2: insertTopic, fetchAllTopics, findTopicByName.
 * Week 4 adds: findTopicById, updateTopic, deleteTopic, searchTopics.
 * Week 5: insertTopic now includes userId from the Topic object.
 * Week 7: adds user-scoped methods (fetchAllTopicsByUserId, searchTopicsByUserId).
 */
public interface TopicDao {
    boolean insertTopic(Topic topic);
    ArrayList<Topic> fetchAllTopics();
    Topic findTopicByName(String name);

    Topic findTopicById(int id);
    boolean updateTopic(Topic topic);
    boolean deleteTopic(int id);
    ArrayList<Topic> searchTopics(String keyword);

    // ============================================================
    // TODO 2: User-Scoped DAO Method Signatures
    // ============================================================
    // Add two new method signatures for fetching topics that belong
    // to a specific user.
    //
    // Currently, fetchAllTopics() and searchTopics() return ALL topics
    // in the database regardless of who created them. After adding
    // session management, we need methods that filter by userId.
    //
    // Add these two methods:
    //
    //   1. fetchAllTopicsByUserId(int userId)
    //      - Returns only topics belonging to the given user
    //      - Used by TopicServlet to show each user their own topics
    //
    //   2. searchTopicsByUserId(int userId, String keyword)
    //      - Searches topics by name, but only within a user's topics
    //      - Used by TopicServlet's search action
    //
    // CONCEPT: These methods mirror the existing fetchAllTopics() and
    // searchTopics(), but add a userId parameter for filtering.
    // The old methods still exist (they might be useful for admin
    // features later), but TopicServlet will switch to these new ones.
    //
    // The complete code:
    //
    //   ArrayList<Topic> fetchAllTopicsByUserId(int userId);
    //   ArrayList<Topic> searchTopicsByUserId(int userId, String keyword);
    //
    // ============================================================
    ArrayList<Topic> fetchAllTopicsByUserId(int userId);
    ArrayList<Topic> searchTopicsByUserId(int userId, String keyword);
}
