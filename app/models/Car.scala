package models

case class Car(id: Int, title: String, price: Int, is_new: Boolean, fuel: Int, mileage: Option[Int])
