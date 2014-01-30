import play.api._
import play.api.Play.current
import play.api.db.DB
//import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.H2Driver.simple._

object Global extends GlobalSettings {
  /*
  override def onStart(application: Application) {
    if (application.mode == Mode.Dev) loadDevData()
  }

  private def loadDevData() {
    Database.forDataSource(DB.getDataSource()) withSession {
      implicit session =>
      val cocktails = TableQuery[Cocktail]
      cocktails += (1L, "Margarita")
      cocktails += (2L, "Pina colada")
    }
  }
  */
}