<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
  <title>パンフレット用マップ</title>
  <link href="https://api.mapbox.com/mapbox-gl-js/v2.15.0/mapbox-gl.css" rel="stylesheet" />
  <script src="https://api.mapbox.com/mapbox-gl-js/v2.15.0/mapbox-gl.js"></script>
  <style>
    #map {
      width: 100%;
      height: 600px;
    }
    #saveImageBtn {
      position: absolute;
      top: 20px;
      right: 20px;
      z-index: 1000;
      padding: 10px 20px;
      background-color: #28a745;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    #map-container {
      position: relative;
    }
    .custom-marker {
	  width: 10px;
	  height: 10px;
	  background-color: red;
	  border-radius: 50%;
	}
	    
  </style>
</head>
<body>
<jsp:include page="../common/header.jsp" />

<h2>🎨 パンフレット用マップ</h2>
<label for="imageTitle">テーマ:</label>
<input type="text" id="imageTitle" name="imageTitle" placeholder="例：浅草マップ" />

<label for="imageDetail">詳細説明:</label>
<textarea id="imageDetail" name="imageDetail" placeholder="この地図は…"></textarea>

<div id="map-container">
  <div id="map"></div>
  <button id="saveImageBtn">📷 地図画像を生成して保存</button>
</div>

<script>
  const pins = [
    <c:forEach var="pin" items="${pins}" varStatus="status">
      {
        id: ${pin.id},
        name: "<c:out value='${pin.name}'/>",
        latitude: ${pin.latitude},
        longitude: ${pin.longitude}
      }<c:if test="${!status.last}">,</c:if>
    </c:forEach>
  ];
  window.pins = pins;
  const selectedImage = "<c:out value='${selectedImage}' />";
</script>
<script src="/map-app/js/mapCapture.js"></script>

</body>
</html>
