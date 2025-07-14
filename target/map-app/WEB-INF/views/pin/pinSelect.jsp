<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>登録されたピン一覧</title>
    <style>
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; }
        th { background-color: #f2f2f2; }
        #filter-area { margin: 20px 0; }
    </style>
</head>
<body>

<jsp:include page="../common/header.jsp" />

<h2>📍 登録されたピン一覧</h2>

<!-- 🔍 フィルター欄 -->
<div id="filter-area">
    <label>都道府県:
        <input type="text" id="prefFilter" placeholder="例：東京都" />
    </label>
    <label>市区町村:
        <input type="text" id="cityFilter" placeholder="例：新宿区" />
    </label>
    <label>カテゴリ:
        <select id="categoryFilter">
            <option value="">すべて</option>
            <option value="観光地">観光地</option>
            <option value="カフェ">カフェ</option>
            <option value="デートスポット">デートスポット</option>
        </select>
    </label>
    <button onclick="applyFilters()">検索</button>
</div>

<form method="post" action="/map-app/image/image-select">
    <table id="pinTable">
        <thead>
            <tr>
                <th>選択</th>
                <th>名前</th>
                <th>住所</th>
                <th>説明</th>
                <th class="category">カテゴリ</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="pin" items="${pins}">
                <tr>
                    <td>
	                    <input type="checkbox" name="selectedPins" value="${pin.id}" />
	           			<input type="hidden" name="pinId_${pin.id}" value="${pin.id}" />
           			</td>
                    <td>${pin.name}</td>
                    <td class="address">${pin.address}</td>
                    <td>${pin.description}</td>
                    <td class="category">${pin.category}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <br>
    <button type="submit">選択したピンを送信</button>
</form>

<!-- 🔎 フィルター処理 -->
<script>
    function applyFilters() {
        const pref = document.getElementById("prefFilter").value.trim();
        const city = document.getElementById("cityFilter").value.trim();
        const category = document.getElementById("categoryFilter").value;
        const rows = document.querySelectorAll("#pinTable tbody tr");

        rows.forEach(row => {
            const address = row.querySelector(".address").innerText;
            const rowCategory = row.querySelector(".category").innerText;

            const matchPref = pref === "" || address.includes(pref);
            const matchCity = city === "" || address.includes(city);
            const matchCategory = category === "" || rowCategory === category;

            row.style.display = (matchPref && matchCity && matchCategory) ? "" : "none";
        });
    }
</script>

</body>
</html>
