package controllers

import play.api._
import play.api.http.HeaderNames._
import play.api.mvc._
import models.database.Places._
import play.api.Play.current
import play.api.db.DB
import play.api.libs.json._
//import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.H2Driver.simple._

object Application extends Controller {

  case class PlaceWithPhotos(place: Place, photos: List[PhotoRef])

  def locs = Action {

    Database.forDataSource(DB.getDataSource()) withSession {
      implicit session =>
        val placesAndPhotos = for {
          (place, photo) <- places leftJoin photos on (_.id === _.placeId)
        } yield (place, photo.id.?)

        def collapse[A, B](xs: List[(A, Option[B])]): (A, List[B]) = {
          (xs.head._1, xs flatMap { _._2 })
        }

        val results = placesAndPhotos.list groupBy { _._1.id } map { _._2 } map collapse

        def longListToJson(xs: List[Long]): JsArray = {
          new JsArray(xs map { new JsNumber(_) })
        }

        val placeToJson = (p: Place, photoIds: List[Long]) => {
          new JsObject(List(
            "id" -> new JsNumber(p.id),
            "name" -> new JsString(p.name),
            "address" -> new JsString(p.address.getOrElse("")),
            "latLng" -> new JsArray(List(new JsNumber(p.lat), new JsNumber(p.long))),
            "photos" -> longListToJson(photoIds)
          ))
        }

        val response = new JsArray(results.toList map { placeToJson.tupled(_) })
        Ok(response.toString).withHeaders(
          ACCESS_CONTROL_ALLOW_ORIGIN -> "*"
        )
    }
  }
  
  def index = Action {
    Ok(views.html.index("Cafe Hanoi"))
  }

}