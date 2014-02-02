package models.database

/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = scala.slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._
  import scala.slick.model.ForeignKeyAction
  import scala.slick.jdbc.{ GetResult => GR }

  /**
   * Entity class storing rows of table Photos
   *  @param id Database column ID AutoInc, PrimaryKey
   *  @param placeId Database column PLACE_ID
   */
  case class PhotosRow(id: Long, placeId: Long)

  /** GetResult implicit for fetching PhotosRow objects using plain SQL queries */
  implicit def GetResultPhotosRow(implicit e0: GR[Long]): GR[PhotosRow] = GR {
    prs =>
      import prs._
      PhotosRow.tupled((<<[Long], <<[Long]))
  }

  /** Table description of table PHOTOS. Objects of this class serve as prototypes for rows in queries. */
  class Photos(tag: Tag) extends Table[PhotosRow](tag, "PHOTOS") {
    def * = (id, placeId) <> (PhotosRow.tupled, PhotosRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, placeId.?).shaped.<>({ r => import r._; _1.map(_ => PhotosRow.tupled((_1.get, _2.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID AutoInc, PrimaryKey */
    val id: Column[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column PLACE_ID  */
    val placeId: Column[Long] = column[Long]("PLACE_ID")

    /** Foreign key referencing Places (database name FK_PLACE) */
    val placesFk = foreignKey("FK_PLACE", placeId, Places)(r => r.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Restrict)
  }

  /** Collection-like TableQuery object for table Photos */
  lazy val Photos = new TableQuery(tag => new Photos(tag))

  /**
   * Entity class storing rows of table Places
   *  @param id Database column ID AutoInc, PrimaryKey
   *  @param name Database column NAME
   *  @param address Database column ADDRESS
   *  @param latitude Database column LATITUDE
   *  @param longitude Database column LONGITUDE
   */
  case class PlacesRow(id: Long, name: String, address: String, latitude: scala.math.BigDecimal, longitude: scala.math.BigDecimal)

  /** GetResult implicit for fetching PlacesRow objects using plain SQL queries */
  implicit def GetResultPlacesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[scala.math.BigDecimal]): GR[PlacesRow] = GR {
    prs =>
      import prs._
      PlacesRow.tupled((<<[Long], <<[String], <<[String], <<[scala.math.BigDecimal], <<[scala.math.BigDecimal]))
  }

  /** Table description of table PLACES. Objects of this class serve as prototypes for rows in queries. */
  class Places(tag: Tag) extends Table[PlacesRow](tag, "PLACES") {

    def * = (id, name, address, latitude, longitude) <> (PlacesRow.tupled, PlacesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, address.?, latitude.?, longitude.?).shaped.<>({ r => import r._; _1.map(_ => PlacesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID AutoInc, PrimaryKey */
    val id: Column[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column NAME  */
    val name: Column[String] = column[String]("NAME")
    /** Database column ADDRESS  */
    val address: Column[String] = column[String]("ADDRESS")
    /** Database column LATITUDE  */
    val latitude: Column[scala.math.BigDecimal] = column[scala.math.BigDecimal]("LATITUDE")
    /** Database column LONGITUDE  */
    val longitude: Column[scala.math.BigDecimal] = column[scala.math.BigDecimal]("LONGITUDE")
  }

  /** Collection-like TableQuery object for table Places */
  lazy val Places = new TableQuery(tag => new Places(tag))
}
