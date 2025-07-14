<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>登録フォーム</title>
  <style>
    #container {
      display: flex;
    }
    #map {
      height: 100vh;
      width: 90%;
    }
    #form-area, #search-area {
      padding: 20px;
      box-sizing: border-box;
      background: #f7f7f7;
    }
    #side-panel {
      display: flex;
      flex-direction: column;
      gap: 20px;
      padding: 20px;
      width: 300px;
    }
    #form-area input,
    #form-area button,
    #search-area input,
    #search-area button {
      width: 100%;
      margin-top: 10px;
    }
  </style>
</head>
<body>
<jsp:include page="../common/header.jsp" />
<div id="container">
  <div id="map"></div>

  <div id="side-panel">
    <div id="search-area">
      <h3>検索欄</h3>
      <input id="autocomplete" type="text" placeholder="地名や住所を検索" />
    </div>

    <div id="form-area">
      <h3>ピンを登録</h3>
      <form id="pinForm" method="post" action="/map-app/pin/pin-registe" accept-charset="UTF-8">
        <label for="name">名前:</label>
        <input type="text" id="name" name="name" required>

        <label for="description">説明:</label>
        <input type="text" id="description" name="description">

        <label for="category">カテゴリ:</label>
        <input type="text" id="category" name="category">

        <input type="hidden" id="latitude" name="latitude">
        <input type="hidden" id="longitude" name="longitude">
        <input type="hidden" id="address" name="address" />
        <button type="submit">登録</button>
      </form>

      <div id="toast" style="display: none; position: fixed; bottom: 20px; right: 20px; background: #333; color: #fff; padding: 10px 20px; border-radius: 6px;">
        登録しました！
      </div>
    </div>
  </div>
</div>

<!-- ✅ 最後に map.js を読み込む -->
<script src="/map-app/js/map.js"></script>

<!-- ✅ Google Maps APIの読み込み（callback付き） -->
<script
  src="https://maps.googleapis.com/maps/api/js?key=${googleKey}&libraries=places&callback=initMap"
  async defer></script>

</body>
</html>
