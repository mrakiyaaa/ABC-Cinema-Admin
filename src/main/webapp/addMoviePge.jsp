<!DOCTYPE html>
<html>
<head>
  <title>Add Movie</title>
</head>
<body>
<h1>Add Movie</h1>
<form action="AddMovieServlet" method="POST">
  <%--@declare id="title"--%><label for="title">Movie Title:</label>
  <input type="text" name="title" required>
  <button type="submit">Search & Add</button>
</form>
</body>
</html>
