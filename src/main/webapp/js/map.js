let map;
let currentMarker;
let markers = [];
let infoWindow = null;
let geocoder;

function hideMarker(index) {
  if (markers[index]) {
    markers[index].setMap(null);
  }
  if (infoWindow) {
    infoWindow.close();
  }
}

function placeMarkerAndSetHidden(latlng) {
  if (currentMarker) {
    currentMarker.setMap(null);
  }

  currentMarker = new google.maps.Marker({
    position: latlng,
    map: map
  });

  markers.push(currentMarker);

  map.setCenter(latlng);
  map.setZoom(15);

  document.getElementById("latitude").value = latlng.lat;
  document.getElementById("longitude").value = latlng.lng;

  geocoder.geocode({ location: latlng, region: "JP" }, (results, status) => {
    if (status === "OK" && results[0]) {
      const components = results[0].address_components;
      const trimmed = extractPrefCityFromComponents(components);
      document.getElementById("address").value = trimmed;
      console.log("整形後住所:", trimmed);
    } else {
      document.getElementById("address").value = "";
    }
  });
}

function initMap() {
  const center = { lat: 35.681236, lng: 139.767125 };
  geocoder = new google.maps.Geocoder();

  map = new google.maps.Map(document.getElementById("map"), {
    zoom: 13,
    center: center,
  });

  const input = document.getElementById("autocomplete");
  const autocomplete = new google.maps.places.Autocomplete(input);
  autocomplete.bindTo("bounds", map);

  autocomplete.addListener("place_changed", () => {
    const place = autocomplete.getPlace();

    if (!place.geometry || !place.geometry.location) {
      alert("場所の情報が取得できませんでした");
      return;
    }

    const latlng = {
      lat: place.geometry.location.lat(),
      lng: place.geometry.location.lng()
    };
    placeMarkerAndSetHidden(latlng);
  });

  map.addListener("click", (e) => {
    const latlng = {
      lat: e.latLng.lat(),
      lng: e.latLng.lng()
    };
    placeMarkerAndSetHidden(latlng);
  });

  document.getElementById("pinForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const data = {
      name: document.getElementById("name").value,
      description: document.getElementById("description").value,
      category: document.getElementById("category").value,
      latitude: document.getElementById("latitude").value,
      longitude: document.getElementById("longitude").value,
      address: document.getElementById("address").value
    };

    fetch("/map-app/pin/pin-regist", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: new URLSearchParams(data)
    })
    .then((response) => {
      if (response.ok) {
        showToast();
        hideMarker(markers.length - 1);
		// 登録後、フォームをリセットする
		document.getElementById("pinForm").reset();
      } else {
        alert("登録に失敗しました");
      }
    })
    .catch((error) => {
      console.error("通信エラー:", error);
      alert("サーバーに接続できませんでした");
    });
  });
}

window.initMap = initMap;

function showToast() {
  const toast = document.getElementById("toast");
  toast.style.display = "block";
  setTimeout(() => {
    toast.style.display = "none";
  }, 3000);
}

function extractPrefCityFromComponents(components) {
  let prefecture = "";
  let city = "";

  components.forEach(component => {
    if (component.types.includes("administrative_area_level_1")) {
      prefecture = component.long_name;
    }
    if (component.types.includes("locality") || component.types.includes("administrative_area_level_2")) {
      city = component.long_name;
    }
  });

  return prefecture + city;
}
