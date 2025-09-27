<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Thêm danh mục</title>
<style>
body {
    font-family: Arial, sans-serif;
    margin: 20px;
}
input[type="text"], input[type="file"] {
    padding: 6px;
    width: 300px;
    margin-bottom: 10px;
}
button {
    padding: 6px 12px;
    background-color: #0d6efd;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
button:hover {
    background-color: #0b5ed7;
}
a {
    margin-left: 10px;
    text-decoration: none;
    color: #333;
}
</style>
</head>
<body>

<h3>Thêm danh mục</h3>

<!-- Form upload file -->
<form action="${pageContext.request.contextPath}/manager/add" method="post" enctype="multipart/form-data">
    <label>Tên danh mục:</label><br>
    <input type="text" name="categoryname" required><br>

    <label>Icon:</label><br>
    <input type="file" name="icon"><br><br>

    <button type="submit">Thêm</button>
    <a href="${pageContext.request.contextPath}/manager/home">Hủy</a>
</form>

</body>
</html>
