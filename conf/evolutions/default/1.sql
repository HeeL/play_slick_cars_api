# Cars schema

# --- !Ups

CREATE TABLE "cars" (
    "id" SERIAL NOT NULL PRIMARY KEY,
    "title" VARCHAR NOT NULL,
    "price" INTEGER NOT NULL,
    "is_new" BOOLEAN NOT NULL,
    "fuel" INTEGER NOT NULL,
    "mileage" INTEGER,
    "registration_date" DATE
  )

# --- !Downs

DROP TABLE "cars";
