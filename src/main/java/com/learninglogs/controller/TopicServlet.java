package com.learninglogs.controller;

import com.learninglogs.dao.TopicDao;
import com.learninglogs.dao.TopicDaoImpl;
import com.learninglogs.entity.Topic;
import com.learninglogs.entity.User;
import com.learninglogs.utils.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * TopicServlet — handles all topic-related HTTP requests.
 *
 * URL: /topic
 *
 * GET actions:
 *   (default)      -> list user's topics -> topic-list.jsp
 *   ?action=new    -> show add form      -> topic-add-edit.jsp
 *   ?action=edit   -> show edit form     -> topic-add-edit.jsp (pre-filled)
 *   ?action=search -> search user's topics -> topic-list.jsp (filtered)
 *
 * POST actions:
 *   action=add     -> insert new topic  -> redirect to /topic
 *   action=edit    -> update topic      -> redirect to /topic
 *   action=delete  -> delete topic      -> redirect to /topic
 *
 * Week 5: doPost "add" action sets userId on the new Topic.
 * Week 7: All operations now use the logged-in user's ID from session
 *          instead of the hardcoded userId=1.
 */

// ============================================================
// TODO 6: Use Session userId in TopicServlet
// ============================================================
// Replace the hardcoded userId=1 with the actual logged-in user's ID
// from the session. This makes topics USER-SCOPED — each user only
// sees and manages their own topics.
//
// THREE changes in this file:
//
// 1. In doGet (default action — list topics):
//    BEFORE (Week 6):
//      ArrayList<Topic> topics = topicDao.fetchAllTopics();
//
//    AFTER (Week 7):
//      User user = (User) SessionUtil.getAttribute(request, "user");
//      ArrayList<Topic> topics = topicDao.fetchAllTopicsByUserId(user.getId());
//
// 2. In doGet (search action):
//    BEFORE:
//      topics = topicDao.fetchAllTopics();
//      ...
//      topics = topicDao.searchTopics(keyword.trim());
//
//    AFTER:
//      User user = (User) SessionUtil.getAttribute(request, "user");
//      topics = topicDao.fetchAllTopicsByUserId(user.getId());
//      ...
//      topics = topicDao.searchTopicsByUserId(user.getId(), keyword.trim());
//
// 3. In doPost (add action):
//    BEFORE:
//      newTopic.setUserId(1);  // Hardcoded userId=1
//
//    AFTER:
//      User user = (User) SessionUtil.getAttribute(request, "user");
//      newTopic.setUserId(user.getId());
//
// CONCEPT: SessionUtil.getAttribute(request, "user") retrieves the
// User object that was stored in the session during login (TODO 4).
// We cast it to User because getAttribute returns Object.
//
// The user variable is obtained at the START of each method because
// we need the user's ID for the DAO calls. The AuthenticationFilter
// (TODO 9) guarantees a logged-in user exists — if not, the filter
// redirects to /login before this servlet is reached.
//
// The complete doGet code:
//
//   @Override
//   protected void doGet(HttpServletRequest request,
//                        HttpServletResponse response)
//           throws ServletException, IOException {
//
//       String action = request.getParameter("action");
//       User user = (User) SessionUtil.getAttribute(request, "user");
//
//       if (action == null) {
//           ArrayList<Topic> topics = topicDao.fetchAllTopicsByUserId(user.getId());
//           request.setAttribute("topics", topics);
//           request.getRequestDispatcher("/WEB-INF/views/topic-list.jsp")
//                  .forward(request, response);
//       }
//       else if ("new".equals(action)) {
//           request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
//                  .forward(request, response);
//       }
//       else if ("edit".equals(action)) {
//           int topicId = Integer.parseInt(request.getParameter("topicid"));
//           Topic topic = topicDao.findTopicById(topicId);
//           request.setAttribute("topic", topic);
//           request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
//                  .forward(request, response);
//       }
//       else if ("search".equals(action)) {
//           String keyword = request.getParameter("search");
//           ArrayList<Topic> topics;
//           if (keyword == null || keyword.trim().isEmpty()) {
//               topics = topicDao.fetchAllTopicsByUserId(user.getId());
//           } else {
//               topics = topicDao.searchTopicsByUserId(user.getId(), keyword.trim());
//           }
//           request.setAttribute("topics", topics);
//           request.setAttribute("searchKeyword", keyword);
//           request.getRequestDispatcher("/WEB-INF/views/topic-list.jsp")
//                  .forward(request, response);
//       }
//   }
//
// The doPost "add" action change (replace the hardcoded userId=1 block):
//
//   BEFORE:
//     Topic newTopic = new Topic(topicName.trim());
//     newTopic.setUserId(1);  // Hardcoded
//     boolean success = topicDao.insertTopic(newTopic);
//
//   AFTER:
//     Topic newTopic = new Topic(topicName.trim());
//     User user = (User) SessionUtil.getAttribute(request, "user");
//     newTopic.setUserId(user.getId());
//     boolean success = topicDao.insertTopic(newTopic);
//
// ============================================================
@WebServlet("/topic")
public class TopicServlet extends HttpServlet {

    private final TopicDao topicDao = new TopicDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        User user = (User) SessionUtil.getAttribute(request, "user");

        if (action == null) {
            ArrayList<Topic> topics = topicDao.fetchAllTopicsByUserId(user.getId());
            request.setAttribute("topics", topics);
            request.getRequestDispatcher("/WEB-INF/views/topic-list.jsp")
                   .forward(request, response);
        }
        else if ("new".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                   .forward(request, response);
        }
        else if ("edit".equals(action)) {
            int topicId = Integer.parseInt(request.getParameter("topicid"));
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                   .forward(request, response);
        }
        else if ("search".equals(action)) {
            String keyword = request.getParameter("search");
            ArrayList<Topic> topics;
            if (keyword == null || keyword.trim().isEmpty()) {
                topics = topicDao.fetchAllTopicsByUserId(user.getId());
            } else {
                topics = topicDao.searchTopicsByUserId(user.getId(), keyword.trim());
            }
            request.setAttribute("topics", topics);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/WEB-INF/views/topic-list.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            String topicName = request.getParameter("topic");

            if (topicName == null || topicName.trim().isEmpty()) {
                request.setAttribute("error", "Topic name cannot be empty.");
                request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            // UPDATED for Week 7 — get userId from session instead of hardcoded 1
            // Week 5-6 was: newTopic.setUserId(1);
            Topic newTopic = new Topic(topicName.trim());
            User user = (User) SessionUtil.getAttribute(request, "user");
            newTopic.setUserId(user.getId());
            boolean success = topicDao.insertTopic(newTopic);

            if (!success) {
                request.setAttribute("error", "Topic already exists.");
                request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/topic");
        }
        else if ("edit".equals(action)) {
            int topicId = Integer.parseInt(request.getParameter("topicid"));
            String topicName = request.getParameter("topic");

            if (topicName == null || topicName.trim().isEmpty()) {
                request.setAttribute("error", "Topic name cannot be empty.");
                Topic topic = topicDao.findTopicById(topicId);
                request.setAttribute("topic", topic);
                request.getRequestDispatcher("/WEB-INF/views/topic-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Topic topic = new Topic(topicName.trim());
            topic.setId(topicId);
            topicDao.updateTopic(topic);
            response.sendRedirect(request.getContextPath() + "/topic");
        }
        else if ("delete".equals(action)) {
            int topicId = Integer.parseInt(request.getParameter("topicid"));
            topicDao.deleteTopic(topicId);
            response.sendRedirect(request.getContextPath() + "/topic");
        }
    }
}
