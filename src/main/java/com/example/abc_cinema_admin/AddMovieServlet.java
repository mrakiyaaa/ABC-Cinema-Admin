package com.example.abc_cinema_admin;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/AddMovieServlet")
public class AddMovieServlet extends HttpServlet {
    private static final String TMDB_API_KEY = "0453add907cb3ae092654a9edc87e895";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        if (title == null || title.trim().isEmpty()) {
            response.sendRedirect("addMoviePge.jsp?error=Title+is+required");
            return;
        }

        // Fetch movie data from TMDB
        String searchUrl = TMDB_BASE_URL + "/search/movie?api_key=" + TMDB_API_KEY + "&query=" + URLEncoder.encode(title, "UTF-8");
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(searchUrl).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("TMDB API returned error code: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(jsonResponse.toString());
            JSONArray results = json.getJSONArray("results");

            if (results.length() > 0) {
                JSONObject movie = results.getJSONObject(0);
                String movieTitle = movie.getString("title");
                String posterPath = movie.optString("poster_path", null);
                double rating = movie.getDouble("vote_average");


                // Debugging logs
                System.out.println("TMDB Movie Data:");
                System.out.println("Title: " + movieTitle);
                System.out.println("Image URL: " + posterPath);
                System.out.println("Rating: " + rating);



                if (posterPath != null) {
                    String imageUrl = "https://image.tmdb.org/t/p/w500" + posterPath;

                    // Insert movie data into the database
                    try (Connection conn = Database.getConnection()) {
                        if (conn == null) {
                            throw new SQLException("Database connection is null");
                        }
                        String sql = "INSERT INTO movies (title, image_url, rating) VALUES (?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, movieTitle);
                        stmt.setString(2, imageUrl);
                        stmt.setDouble(3, rating);
                        stmt.executeUpdate();
                        System.out.println("Movie inserted successfully: " + movieTitle);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        request.setAttribute("errorMessage", "Database error: " + e.getMessage());
                        response.sendRedirect("addMoviePge.jsp?error=Database+error");
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("addMoviePge.jsp?error=Failed+to+fetch+movie+data");
            return;
        }

        response.sendRedirect("adminHome.jsp");
    }
}
