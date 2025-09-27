<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Danh sách danh mục</title>
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
}

.header {
	margin-bottom: 10px;
	overflow: hidden;
}

.header form {
	float: left;
}

.header input[type="text"] {
	padding: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
	width: 180px;
}

.header select {
	padding: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
	background-color: white;
	cursor: pointer;
	margin-left: 5px;
}

.header button {
	padding: 5px 10px;
	margin-left: 5px;
	border-radius: 4px;
	border: 1px solid #0d6efd;
	background-color: #0d6efd;
	color: white;
	cursor: pointer;
}

.header button:hover {
	background-color: #0b5ed7;
}

.btn-add {
	float: right;
	padding: 6px 12px;
	background-color: #0d6efd;
	color: white;
	border: none;
	cursor: pointer;
	border-radius: 4px;
}

.btn-add:hover {
	background-color: #0b5ed7;
}

.box-title {
	background-color: #e6f3fa;
	padding: 8px;
	font-weight: bold;
	border: 1px solid #cde0ee;
	margin-top: 15px;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 0;
}

th, td {
	border: 1px solid #ccc;
	padding: 6px;
	text-align: left;
}

th {
	background-color: #f5f5f5;
}

a {
	text-decoration: none;
	color: blue;
	margin: 0 3px;
	cursor: pointer;
}
</style>
</head>
<body>

<div class="header">
	<form method="get" action="${pageContext.request.contextPath}/manager/home" id="filterForm">
		<input type="text" name="search" placeholder="Search" value="${param.search}">
		<select name="userid" onchange="document.getElementById('filterForm').submit()">
			<option value="">-- Chọn user --</option>
			<c:forEach items="${listUser}" var="u">
				<option value="${u.id}" 
					<c:if test="${param.userid != null && param.userid == u.id.toString()}">selected</c:if>>
					${u.fullname}
				</option>
			</c:forEach>
		</select>
	</form>

	<a href="${pageContext.request.contextPath}/manager/add" class="btn-add">Thêm danh mục</a>
	<jsp:include page="/topbar.jsp" />
</div>

<div class="box-title">Danh sách danh mục</div>

<table>
	<thead>
		<tr>
			<th>ID</th>
			<th>Tên danh mục</th>
			<th>Icon</th>
			<th>Hành động</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${listcate}" var="cate">
			<tr>
				<td>${cate.id}</td>
				<td>${cate.categoryname}</td>
				<td>
					<c:if test="${not empty cate.icon}">
						<img src="${pageContext.request.contextPath}/image?name=${cate.icon}" width="100"/>
					</c:if>
				</td>
				<td>
					<a href="${pageContext.request.contextPath}/manager/update?id=${cate.id}">Cập nhật</a> |
					<a href="#" onclick="confirmDelete(${cate.id})">Xóa</a>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>

<!-- Delete form ẩn -->
<form id="deleteForm" action="${pageContext.request.contextPath}/manager/delete" method="post" style="display: none;">
	<input type="hidden" name="id" id="deleteId">
</form>

<script>
function confirmDelete(id) {
	if (confirm("Bạn có chắc chắn muốn xóa danh mục này?")) {
		document.getElementById("deleteId").value = id;
		document.getElementById("deleteForm").submit();
	}
}
</script>

</body>
</html>
