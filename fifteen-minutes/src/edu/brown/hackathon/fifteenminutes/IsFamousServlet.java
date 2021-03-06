package edu.brown.hackathon.fifteenminutes;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;

@SuppressWarnings("serial")
public class IsFamousServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserResource ur = getUserIdOfFamousUser();
    
    Gson gson = new Gson();
    resp.setContentType("application/json");
    resp.getWriter().println(gson.toJson(ur));
  }
  
  public static UserResource getUserIdOfFamousUser() {
    Query query = new Query("FamousUser");
    query.addSort("current_time", Query.SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(1));
    if (results.size() < 1) {
      return new UserResource(-1, -1);
    } else {
      return new UserResource((Long)results.get(0).getProperty("new_user_id"),
          (Long)results.get(0).getProperty("old_user_id"));
    }
  }
}