package models

case class LatLong(lat: Double, long: Double)
case class Location(address: String, latLong: LatLong)

case class Photo(id: Long)

case class Place(
  name: String,
  loc: Location,
  photos: Iterable[Photo]) {
}