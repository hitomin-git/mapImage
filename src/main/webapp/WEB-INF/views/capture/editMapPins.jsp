<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8" />
  <title>📍 ピンの編集</title>

  <style>
    body {
      font-family: sans-serif;
      margin: 40px;
    }

    h2 {
      margin-bottom: 5px;
    }

    #map-container {
      position: relative;
      display: inline-block;
      border: 1px solid #ccc;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    #map-image {
      width: 800px;
      display: block;
    }

    .pin {
      position: absolute;
      width: 20px;
      height: 20px;
      background-color: crimson;
      border: 2px solid white;
      border-radius: 50%;
      box-shadow: 0 0 5px rgba(0,0,0,0.4);
      transform: translate(-50%, -50%);
      z-index: 10;
    }

    #saveForm {
      margin-top: 20px;
    }

    button {
      padding: 8px 16px;
      font-size: 16px;
      background-color: #0066cc;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }

    button:hover {
      background-color: #004f99;
    }
  </style>
</head>

<body>

  <h2>${map.title}</h2>
  <p>${map.description}</p>

  <div id="map-container">
    <img id="map-image" src="${pageContext.request.contextPath}/static/uploads/${map.path}" alt="マップ画像" />
  </div>

  <form id="saveForm" method="post" action="${pageContext.request.contextPath}/capture/save-pin-positions">
    <input type="hidden" name="mapId" value="${map.id}" />
    <input type="hidden" name="pinData" id="pinData" />
    <button type="submit">保存する</button>
  </form>

  <script>
    const mapContainer = document.getElementById("map-container");
    const pinData = [];

    mapContainer.addEventListener("click", function (e) {
      const rect = mapContainer.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;

      const pin = document.createElement("div");
      pin.className = "pin";
      pin.style.left = `${x}px`;
      pin.style.top = `${y}px`;
      mapContainer.appendChild(pin);

      pinData.push({ pinId: -1, x: Math.round(x), y: Math.round(y) });
      document.getElementById("pinData").value = JSON.stringify(pinData);
    });
  </script>

</body>
</html>
