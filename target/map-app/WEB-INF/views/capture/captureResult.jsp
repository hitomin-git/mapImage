<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>保存完了</title>
  <style>
    #map-container {
      position: relative;
      display: inline-block;
    }
    .pin {
      position: absolute;
      width: 16px;
      height: 16px;
      background-color: red;
      border-radius: 50%;
      transform: translate(-50%, -50%);
    }
  </style>
</head>
<body>
  <jsp:include page="../common/header.jsp" />

  <h1>画像の保存が完了しました！</h1>
  <h2>${title}</h2>
  <p>${detail}</p>

  <div id="map-container">
    <img src="${imagePath}" id="map-image" />
  </div>

<script>
  const pinPositions = JSON.parse('<%= pinPositionsJson %>');

  const container = document.getElementById("map-container");
  pinPositions.forEach(pin => {
    const pinElem = document.createElement("div");
    pinElem.className = "pin";
    pinElem.style.left = pin.x + "px";
    pinElem.style.top = pin.y + "px";
    container.appendChild(pinElem);
  });
</script>

</body>
</html>
