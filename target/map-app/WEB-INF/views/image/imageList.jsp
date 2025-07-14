<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>保存された画像一覧</title>
    <style>
        .error-message {
            color: red;
            font-weight: bold;
        }
    </style>
    
    <script>
        // 画像削除用の関数
        async function deleteImage(imagePath) {
            console.log("deleteImageが呼ばれました。画像パス:", imagePath);  // 呼び出し時にログを追加
            
            const response = await fetch("/map-app/capture/delete-image", {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: new URLSearchParams({ path: imagePath })  // 削除する画像のパスを送信
            });

            if (response.ok) {
                const result = await response.json();  // サーバーからの結果を取得
                console.log("[DEBUG] JSONレスポンス:", result);
                
                alert(result.message);  // サーバーからのメッセージを表示
                // 削除後、画像要素をUIから非表示にする
                const imageElement = document.querySelector(`img[src='${imagePath}']`);
                if (imageElement) {
                    imageElement.parentElement.remove();  // 画像とその削除ボタンを非表示
                }
            } else {
                alert("画像の削除に失敗しました");
            }
        }
    </script>
</head>
<body>
<jsp:include page="../common/header.jsp" />
    <h2>保存された画像一覧</h2>
    <!-- エラーメッセージがあれば表示 -->
    <c:if test="${not empty errorMessage}">
        <p class="error-message">${errorMessage}</p>
    </c:if>

    <c:if test="${not empty images}">
        <ul>
            <c:forEach var="image" items="${images}">
                <li>
                    <img src="${image}" alt="Saved Image" style="width: 200px; height: auto;">
                    <br>
                    <a href="/map-app/editImage?path=${image}">編集</a>
                    <!-- 削除ボタン -->
                    <button onclick="deleteImage('${image}')">削除</button>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <c:if test="${empty images}">
        <p>保存された画像はありません。</p>
    </c:if>

</body>
</html>
