<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Home</title>
    <style>
        .movie-card {
            border: 1px solid #ccc;
            padding: 15px;
            margin: 10px;
            display: inline-block;
            width: 200px;
            text-align: center;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            transition: transform 0.3s;
        }

        .movie-card:hover {
            transform: scale(1.05);
        }

        .movie-card img {
            width: 100%;
            height: auto;
            border-radius: 5px;
        }

        .movie-card h3 {
            font-size: 18px;
            margin-top: 10px;
        }

        .movie-card p {
            color: #555;
        }
    </style>
</head>
<body>
<h1>Admin Panel</h1>
<a href="addMoviePge.jsp"><button>Add Movies</button></a>
<div id="movie-container">
    <%
        // Database connection details
        String jdbcUrl = "jdbc:mysql://localhost:3306/ABC_Cinema";
        String jdbcUsername = "root";
        String jdbcPassword = "Sudubaba@123";

        try {
            // Initialize connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);

            // Query to get movies
            String query = "SELECT image_url, title, rating FROM movies";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Loop through the result set and display movie cards
            while (rs.next()) {
                String imageUrl = rs.getString("image_url");
                String title = rs.getString("title");
                double rating = rs.getDouble("rating");
    %>
    <div class="movie-card">
        <img src="<%= imageUrl %>" alt="<%= title %>" style="width:150px;">
        <h3><%= title %></h3>
        <p>Rating: <%= rating %></p>
    </div>
    <%
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("<p>Error retrieving movies: " + e.getMessage() + "</p>");
        }
    %>
</div>
</body>
</html>
