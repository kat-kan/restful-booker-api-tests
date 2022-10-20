# restful-booker-api-tests

Collection of API tests for Restful-Booker

![](https://img.shields.io/badge/Code-Java%2017-informational?style=flat&color=blueviolet)
![](https://img.shields.io/badge/Framework-JUnit%205-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-REST%20Assured-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-AssertJ-informational?style=flat&&color=blueviolet)

App links : [Restful -Booker](https://restful-booker.herokuapp.com/),  [Restful-Booker API Docs](https://restful-booker.herokuapp.com/apidoc/index.html)

Restful-Booker is Web API playground created by Mark Winteringham. It offers authentication, CRUD operations and is loaded with bugs (for the purpose of learning). 

## Issues found

As mentioned, Restful Booker API has some bugs for the fun of its testers. Here are the ones that I found:

`https://restful-booker.herokuapp.com/booking` - Get Booking Ids
* Filtering by checkout date does not work: returns random results instead of bookings with greater or equal checkout date. For that reason, I had to use @Disabled annotation for test with checkout dates

`https://restful-booker.herokuapp.com/booking` - Create Booking
* Total price value cannot be a floating number; precision is lost during saving.
* Checkin and checkout dates are validated and booking is not created, but 200 OK status code is returned; 400 Bad Request would be better

`https://restful-booker.herokuapp.com/booking/1` - Delete Booking
* Auth can be set only via Cookie header, doesn't work with Authorization header (returns 403 Forbidden)
* Successful Delete action returns 201 Created, which is rather poor choice for delete action. It's rather used when creating new data.
* Delete non existing booking returns 405 Method Not Allowed. I believe it should be 404 Not Found and marked my test as @Disabled.
