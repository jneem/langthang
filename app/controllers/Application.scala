package controllers

import play.api._
import play.api.http.HeaderNames._
import play.api.mvc._
import play.api.libs.json._
import models.Places
import models.Places._
//import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.H2Driver.simple._

object Application extends Controller {

  /**
   * JSON response listing all of the places.
   */
  def locs = Action {
    val results = Places.all

    val response = new JsArray(results.toList map { _.toJSON })
    Ok(response.toString).withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*"
    )
  }

  def index = Action {
    Ok(views.html.index("Cafe Hanoi"))
  }
}