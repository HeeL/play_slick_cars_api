package data

import models.Car
import slick.driver.PostgresDriver.api._
import tables.CarTable._

object TestData {

  val setup = DBIO.seq(
    //Recreate db schema
    cars.schema.drop,
    cars.schema.create,

    //Insert some suppliers
    cars += Car(1, "Audi A3", 25000, true, 1, None),
    cars += Car(2, "Ferrari", 80000, false, 2, Some(500000))
  )

}
