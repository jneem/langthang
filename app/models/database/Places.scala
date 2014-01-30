package models.database

//import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.H2Driver.simple._

object Places {
  case class Place(id: Long, name: String, address: Option[String], lat: BigDecimal, long: BigDecimal)

  class PlacesTable(tag: Tag) extends Table[Place](tag, "PLACES") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def address = column[Option[String]]("ADDRESS")
    def lat = column[BigDecimal]("LATITUDE")
    def long = column[BigDecimal]("LONGITUDE")

    def * = (id, name, address, lat, long) <> (Place.tupled, Place.unapply)
  }

  val places = TableQuery[PlacesTable]

  case class PhotoRef(id: Long, placeId: Long) {
    def toUrl = f"$id%06d"
  }

  class PhotosTable(tag: Tag) extends Table[PhotoRef](tag, "PHOTOS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def placeId = column[Long]("PLACE_ID")

    def place = foreignKey("FK_PLACE", placeId, places)(_.id)
    
    def * = (id, placeId) <> (PhotoRef.tupled, PhotoRef.unapply)
  }

  val photos = TableQuery[PhotosTable]
}