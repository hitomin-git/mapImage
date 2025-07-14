<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>地図テーマ選択</title>
  <style>
    .demo-image {
      width: 300px;
      margin: 10px;
      border: 3px solid transparent;
      cursor: pointer;
    }
    .demo-image.selected {
      border-color: teal;
    }
  </style>
</head>
<body>

<jsp:include page="../common/header.jsp" />

<h2>🖼 地図のデザインを選ぼう</h2>

<form method="post" action="/map-app/capture/capture-select">
  <div>
    <c:forEach var="img" items="${demoImages}">
      <img src="${pageContext.request.contextPath}${img}" class="demo-image" onclick="selectImage(this, '${img}')"/>
    </c:forEach>
  </div>

  <input type="hidden" name="selectedImage" id="selectedImage" />
  <br>
  <button type="submit">このデザインで地図を作成</button>
</form>

<script>
  function selectImage(imgElement, path) {
    document.querySelectorAll('.demo-image').forEach(el => el.classList.remove("selected"));
    imgElement.classList.add("selected");
    document.getElementById("selectedImage").value = path;
  }
</script>

</body>
</html>
