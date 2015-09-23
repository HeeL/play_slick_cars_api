# Yet another REST API Sample

This is a sample app that demonstrate how to built REST API using [Play Framework](http://www.playframework.com), [Slick](http://slick.typesafe.com/) with Postgres DB,
[Specs2](http://etorreborre.github.io/specs2/), [Activator and SBT](https://www.typesafe.com/community/core-tools/activator-and-sbt#overview).

## Installation

* You will need a JDK 7+ (e.g. [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Install sbt ([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html)
* Install postgres server.

## Configuration
* Create postgres database
* Edit a config file `conf/application.conf`

## Run
To run the application, execute the following command:

```
$ sbt run
```

You can now visit http://localhost:9000 in your browser to see the home page.

## Run Tests
To run tests, execute this command:

```
$ sbt test
```

# Example Of Curl Requests


##List cars:
Return a json list of all the cars in db:

```
curl -X GET "http://localhost:9000/cars"
```

Cars can be sorted by any attribute specified in the json. The sort direction can be ascending or descending. Here is an example of such requests, a list of cars sorted descending by price:

```
curl -X GET "http://localhost:9000/cars?sort=price&desc=1"
```

The same sorting but ascending this time:

```
curl -X GET "http://localhost:9000/cars?sort=price"
```

##Create a car:

Here is a post request with valid parameters that create a new car:

```
curl -X POST "http://localhost:9000/cars" --data "title=BMW&price=1500&fuel=1&is_new=false&mileage=80000"
```

If validation is not passing the error will be rendered in json format:

```
curl -X POST "http://localhost:9000/cars" --data "price=1500&fuel=1&is_new=true"
```

As the result we will get the error message `{"title":["This field is required"]}`. Multiple error messages are combined together.

If car is not new, the mileage is required to be specified. This request will lead to error message that mileage has to be filled:

```
curl -X POST "http://localhost:9000/cars" --data "title=BMW&price=1500&fuel=1&is_new=false"
```

And if we change the boolean flag from false to true and mark the car as new, the validation will pass and the new car will be created:

```
curl -X POST "http://localhost:9000/cars" --data "title=BMW&price=1500&fuel=1&is_new=true"
```

##Update a car:

All the same validations are applied when we update information about the car with the put request:


```
curl -X PUT "http://localhost:9000/cars/1" --data "title=Lada&price=100&fuel=2&is_new=true"
```

##Show a car:

In order to get the information about a single car:

```
curl -X GET "http://localhost:9000/cars/1"
```

##Delete a car:

Delete request that destroy the record in db:

```
curl -X DELETE "http://localhost:9000/cars/1"
```
