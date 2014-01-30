
var map;

// If we can get the current location, this is the zoom we'll choose.
// (if not, just zoom out all the way).
var STARTING_ZOOM = 10;

function addPlaces(data) {
	function putMarker(index, place) {
		latLng = new google.maps.LatLng(place.latLng[0], place.latLng[1]);
		new google.maps.Marker({
			position : latLng,
			map : map,
			title : place.name
		});
	}
	function putSidebar(index, place) {
		$("#map-sidebar").append("<div class=\"sidebar-item\"><div class=\"sidebar-heading\">" + place.name + "</div><div class=\"sidebar-subheading\">" + place.address + "</div></div>")
	}
	$.each(data, putMarker);
	$.each(data, putSidebar);
}

function setLocation() {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(pos) {
			loc = new google.maps.LatLng(pos.coords.latitude,
					pos.coords.longitude);
			map.setCenter(loc);
			map.setZoom(STARTING_ZOOM);
		});
	}
}

function initializeMap() {
	var mapOptions = {
		center : new google.maps.LatLng(-34.397, 150.644),
		zoom : 1
	};
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);

	setLocation();

	$.getJSON("http://cafehanoitesting.com:9000/locs", {}, addPlaces);
}