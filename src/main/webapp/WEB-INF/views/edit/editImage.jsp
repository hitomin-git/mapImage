<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>画像編集</title>
</head>
<body>
<jsp:include page="../common/header.jsp" />
    <h2>画像編集</h2>

    <form action="/map-app/saveEditedImage" method="post">
        <img src="${imagePath}" alt="Image to Edit" style="width: 400px; height: auto;">
        <br><br>

        <label for="comment">コメント:</label>
        <textarea name="comment" id="comment" rows="4" cols="50"></textarea>
        <br><br>

        <input type="hidden" name="imagePath" value="${imagePath}">
        <button type="submit">保存</button>
    </form>

    <br><br>
    <a href="/map-app/confirmSavedImages">保存した画像一覧に戻る</a>
</body>
</html>
