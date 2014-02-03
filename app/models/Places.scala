package models

import models.database.Tables
import scala.slick.driver.H2Driver.simple._
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

object Places {
  case class LatLng(lat: Double, long: Double) {
    def toJSON: JsValue = new JsArray(List(new JsNumber(lat), new JsNumber(long)))
  }

  case class Location(address: String, latLng: LatLng)

  case class Photo(id: Long)

  case class Place(
    id: Long,
    name: String,
    loc: Location,
    photos: Iterable[Photo]) {

    def toJSON: JsValue = new JsObject(List(
      "id" -> new JsNumber(id),
      "name" -> new JsString(name),
      "address" -> new JsString(loc.address),
      "latLng" -> loc.latLng.toJSON,
      "photos" -> new JsArray(photos.toSeq map { p => new JsNumber(p.id) })
    ))
  }

  def all: Iterable[Place] =
    Database.forDataSource(DB.getDataSource()) withSession { implicit session =>
      convert(placesQuery.list)
    }

  // The type produced by querying for places.
  private type QueryType = (Tables.PlacesRow, Option[Tables.PhotosRow])

  private lazy val placesQuery = for {
    (place, photo) <- Tables.Places leftJoin Tables.Photos on (_.id === _.placeId)
  } yield (place, photo.?)

  // Extract the Location from a PlacesRow.
  private def row2Location(place: Tables.PlacesRow) =
    Location(place.address, LatLng(place.latitude.toFloat, place.longitude.toFloat))

  // Extract the Photo from a PhotosRow.
  private def row2Photo(photo: Tables.PhotosRow) =
    Photo(photo.id)

  // Extract the Place from a PlacesRow and all the PhotosRows that reference it.
  private def row2Place(place: Tables.PlacesRow, photos: Iterable[Tables.PhotosRow]) =
    Place(place.id, place.name, row2Location(place), photos map row2Photo)

  // Convert a query result into a list of Places.
  private def convert(xs: Iterable[QueryType]): Iterable[Place] = {
    def collapse[A, B](xs: Iterable[(A, Option[B])]): (A, Iterable[B]) = {
      (xs.head._1, xs flatMap { _._2 })
    }

    // Gather together the photos for each place.
    val ys: Iterable[(Tables.PlacesRow, Iterable[Tables.PhotosRow])] =
      xs groupBy { _._1.id } map { _._2 } map collapse

    ys map (row2Place _).tupled
  }
}