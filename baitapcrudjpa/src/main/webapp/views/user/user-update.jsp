<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Cập nhật danh mục</title>
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

<h3>Cập nhật danh mục</h3>

<form action="${pageContext.request.contextPath}/user/update" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${category.id}">

    <label>Tên danh mục:</label><br>
    <input type="text" name="categoryname" value="${category.categoryname}" required><br>

    <label>Icon hiện tại:</label><br>
    <c:choose>
        <c:when test="${not empty category.icon}">
            <img src="${pageContext.request.contextPath}/uploads/${category.icon}" alt="icon" style="width:50px;height:50px;"><br>
        </c:when>
        <c:otherwise>
            Chưa có icon<br>
        </c:otherwise>
    </c:choose>

    <label>Thay đổi icon:</label><br>
    <input type="file" name="icon"><br><br>

    <button type="submit">Cập nhật</button>
    <a href="${pageContext.request.contextPath}/user/home">Hủy</a>
</form>

</body>
</html>
