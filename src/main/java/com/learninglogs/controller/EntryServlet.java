package com.learninglogs.controller;

import com.learninglogs.dao.EntryDao;
import com.learninglogs.dao.EntryDaoImpl;
import com.learninglogs.dao.TopicDao;
import com.learninglogs.dao.TopicDaoImpl;
import com.learninglogs.entity.Entry;
import com.learninglogs.entity.Topic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

/**
 * EntryServlet — handles all entry-related HTTP requests.
 *
 * URL: /entry
 *
 * GET actions:
 *   (default)      -> list entries for a topic  -> entry-list.jsp
 *   ?action=new    -> show add form             -> entry-add-edit.jsp
 *   ?action=edit   -> show edit form            -> entry-add-edit.jsp (pre-filled)
 *   ?action=search -> search entries in a topic -> entry-list.jsp (filtered)
 *
 * POST actions:
 *   action=add     -> insert new entry   -> redirect to /entry?topicid=X
 *   action=edit    -> update entry       -> redirect to /entry?topicid=X
 *   action=delete  -> delete entry       -> redirect to /entry?topicid=X
 *
 * KEY DIFFERENCE FROM TopicServlet:
 *   Every entry belongs to a topic, so ALL URLs include ?topicid=X.
 *   This servlet needs TWO DAOs — EntryDao for entries AND TopicDao
 *   to fetch the topic name for display.
 */

@WebServlet("/entry")
public class EntryServlet extends HttpServlet {

    private final EntryDao entryDao = new EntryDaoImpl();
    private final TopicDao topicDao = new TopicDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int topicId = Integer.parseInt(request.getParameter("topicid"));

        if (action == null) {
            ArrayList<Entry> entries = entryDao.fetchEntriesByTopicId(topicId);
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("entries", entries);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/entry-list.jsp")
                   .forward(request, response);
        }

        else if ("new".equals(action)) {
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                   .forward(request, response);
        }
        else if ("edit".equals(action)) {
            int entryId = Integer.parseInt(request.getParameter("entryid"));
            Entry entry = entryDao.findEntryById(entryId);
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("entry", entry);
            request.setAttribute("topic", topic);
            request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                   .forward(request, response);
        }

        else if ("search".equals(action)) {
            String keyword = request.getParameter("search");
            ArrayList<Entry> entries;
            if (keyword == null || keyword.trim().isEmpty()) {
                entries = entryDao.fetchEntriesByTopicId(topicId);
            } else {
                entries = entryDao.searchEntries(topicId, keyword.trim());
            }
            Topic topic = topicDao.findTopicById(topicId);
            request.setAttribute("entries", entries);
            request.setAttribute("topic", topic);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/WEB-INF/views/entry-list.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        int topicId = Integer.parseInt(request.getParameter("topicid"));

        if ("add".equals(action)) {
            String title = request.getParameter("title");
            String text = request.getParameter("text");
            String link = request.getParameter("link");
            String image = request.getParameter("image");

            if (title == null || title.trim().isEmpty()
                    || text == null || text.trim().isEmpty()) {
                request.setAttribute("error", "Title and description are required.");
                Topic topic = topicDao.findTopicById(topicId);
                request.setAttribute("topic", topic);
                request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Entry entry = new Entry(title.trim(), text.trim(), topicId);
            entry.setLink(link);
            entry.setImage(image);
            entryDao.insertEntry(entry);
            response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
        }

        else if ("edit".equals(action)) {
            int entryId = Integer.parseInt(request.getParameter("entryid"));
            String title = request.getParameter("title");
            String text = request.getParameter("text");
            String link = request.getParameter("link");
            String image = request.getParameter("image");

            if (title == null || title.trim().isEmpty()
                    || text == null || text.trim().isEmpty()) {
                request.setAttribute("error", "Title and description are required.");
                Entry entry = entryDao.findEntryById(entryId);
                Topic topic = topicDao.findTopicById(topicId);
                request.setAttribute("entry", entry);
                request.setAttribute("topic", topic);
                request.getRequestDispatcher("/WEB-INF/views/entry-add-edit.jsp")
                       .forward(request, response);
                return;
            }

            Entry entry = new Entry(title.trim(), text.trim(), topicId);
            entry.setId(entryId);
            entry.setLink(link);
            entry.setImage(image);
            entryDao.updateEntry(entry);
            response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
        }

        else if ("delete".equals(action)) {
            int entryId = Integer.parseInt(request.getParameter("entryid"));
            entryDao.deleteEntry(entryId);
            response.sendRedirect(request.getContextPath() + "/entry?topicid=" + topicId);
        }
    }
}
