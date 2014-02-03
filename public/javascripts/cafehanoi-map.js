var SMALL_MARKER_URL = jsRoutes.controllers.Assets.at("images/marker-small.png").url;
var LARGE_MARKER_URL = jsRoutes.controllers.Assets.at("images/marker-large.png").url;

var map;

// If we can get the current location, this is the zoom we'll choose.
// (if not, just zoom out all the way).
var STARTING_ZOOM = 10;

// Mapping from place ids to markers.
var id2Marker = {};

// Mapping from place ids to place objects.
var id2Place = {};

// The id of the currently active place (could be null).
var activeId = null;

// Given a place, return a string that will be the ID of that place's
// div in the sidebar.
function sidebarId(id) {
	return "sbId" + id.toString();
}

function sidebarImage(id) {
	var imageName = id.toString() + "-180.jpg";
	var imageUrl = jsRoutes.controllers.Assets.at("images/" + imageName).url;
	return "<div class=\"sidebar-image\">" +
	  "<img alt=\"Preview image\" height=\"135\" width=\"180\" src=\"" + imageUrl + "\">" +
	"</div>";
}

// Activate a place.
function activate(id) {
	if (activeId != null) {
		deactivate(activeId);
	}
	
	id2Marker[id].setIcon(LARGE_MARKER_URL);

	var sb = $("#" + sidebarId(id));
	sb.attr("class", "sidebar-item-active");
	
	var photos = id2Place[id].photos;
	if (photos.length > 0) {
      sb.append(sidebarImage(photos[0]));
	}

	activeId = id;
}

// Deactivate a place.
function deactivate(id) {
	console.assert(id == activeId, "Tried to deactivate an inactive id.");
	
	id2Marker[id].setIcon(SMALL_MARKER_URL);

	var sb = $("#" + sidebarId(id));
	sb.attr("class", "sidebar-item");
	sb.find(".sidebar-image").remove();

	activeId = null;
}

// Given the description of a place, put a marker on the map and add an
// entry to the sidebar.
function addPlaces(data) {
	function addPlace(index, place) {
		id2Place[place.id] = place;
		
		// Add the marker.
		latLng = new google.maps.LatLng(place.latLng[0], place.latLng[1]);
		var mark = new google.maps.Marker({
			position : latLng,
			map : map,
			title : place.name,
			icon : SMALL_MARKER_URL
		});
		id2Marker[place.id] = mark;
		google.maps.event.addListener(mark, 'click', function() {
			activate(place.id);
		})

		// Add the sidebar.
		$("#map-sidebar").append(
          "<div class=\"sidebar-item\" + id=\"" + sidebarId(place.id) + "\">" +
            "<div class=\"sidebar-heading\">" + place.name + "</div>" +
            "<div class=\"sidebar-subheading\">" + place.address + "</div>" +
          "</div>")
        $("#" + sidebarId(place.id)).click(function() { activate(place.id); })
	}
	$.each(data, addPlace);
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