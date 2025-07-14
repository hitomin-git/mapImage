// 🔐 Mapbox アクセストークン（自分のものに差し替えてね）
mapboxgl.accessToken =  '<%= request.getAttribute("mapboxKey") %>';

window.addEventListener("DOMContentLoaded", () => {
  const bounds = new mapboxgl.LngLatBounds();

  // 🗺️ Mapbox地図の初期化
  const map = new mapboxgl.Map({
    container: 'map',
    style: 'mapbox://styles/mapbox/streets-v11',
    center: [139.767125, 35.681236],
    zoom: 12
  });
  window.map = map;

  // 📍 ピンの描画
  if (window.pins) {
    window.pins.forEach(pin => {
      const el = document.createElement("div");
      el.className = "custom-marker";
      el.dataset.pinId = pin.id;

      new mapboxgl.Marker(el)
        .setLngLat([pin.longitude, pin.latitude])
        .setPopup(new mapboxgl.Popup().setText(pin.name))
        .addTo(map);

      bounds.extend([pin.longitude, pin.latitude]);
    });
  }

  // 🧭 地図の表示範囲を調整
  if (!bounds.isEmpty()) {
    map.fitBounds(bounds, { padding: 40 });
  }

  const saveBtn = document.getElementById("saveImageBtn");
  if (saveBtn) {
    saveBtn.addEventListener("click", async () => {
      try {
        const map = window.map;
        const mapRect = map.getContainer().getBoundingClientRect();

        // 📌 各ピンの座標
        const markerElements = document.querySelectorAll(".custom-marker");
        const pinPositions = Array.from(markerElements).map(marker => {
          const rect = marker.getBoundingClientRect();
          const x = Math.round(rect.left - mapRect.left);
          const y = Math.round(rect.top - mapRect.top);
          const pinId = marker.dataset.pinId;
          return { pinId, x, y };
        });
        console.log("[DEBUG] ピン座標:", pinPositions);

        // 📍 地図中心などの取得
        const center = map.getCenter();
        const zoom = map.getZoom();
        const width = map.getContainer().offsetWidth;
        const height = map.getContainer().offsetHeight;

        const centerStr = `${center.lng},${center.lat},${zoom}`;
        const size = `${width}x${height}`;
        const imageUrl = `https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/${centerStr}/${size}?access_token=${mapboxgl.accessToken}`;

        console.log("[DEBUG] Static API URL:", imageUrl);

        // 🌐 画像取得
        const response = await fetch(imageUrl);
        const blob = await response.blob();
        console.log("[DEBUG] Blob取得:", blob);
        console.log("[DEBUG] Blobサイズ:", blob.size, "バイト");
        console.log("[DEBUG] Blobタイプ:", blob.type);

        // 🚀 送信用FormData作成
        const formData = new FormData();
        formData.append("image", blob, "map.png");

        const title = document.getElementById("imageTitle")?.value || "";
        const detail = document.getElementById("imageDetail")?.value || "";
        formData.append("title", title);
        formData.append("detail", detail);
        formData.append("pinPositions", JSON.stringify(pinPositions));

        console.log("[DEBUG] タイトル:", title);
        console.log("[DEBUG] 説明文:", detail);
        console.log("[DEBUG] pinPositions JSON:", JSON.stringify(pinPositions));

        // ✅ アップロード処理
        const uploadRes = await fetch("/map-app/capture/capture-saved", {
          method: "POST",
          body: formData,
          headers: {
            "Accept": "application/json"
          }
        });

        if (uploadRes.ok) {
          const result = await uploadRes.json();
          console.log("[DEBUG] サーバー返却パス:", result.path);
          alert("画像を保存しました！");
          window.location.href = `/map-app/capture/capture-result?path=${encodeURIComponent(result.path)}`;
        } else {
          console.error("[ERROR] アップロード失敗:", uploadRes.status);
          alert("画像の保存に失敗しました");
        }

      } catch (e) {
        console.error("[例外] エラー発生:", e);
        alert("予期しないエラーが発生しました");
      }
    });
  }

  // 💬 吹き出しコメントを保存（ローカルストレージ）
  function saveComment(id) {
    const textarea = document.getElementById(`comment-${id}`);
    if (!textarea) return;
    const comment = textarea.value;
    localStorage.setItem(`comment-${id}`, comment);
    alert("コメントを保存しました！");
  }
});
