# restful-booker-api-tests

Collection of API tests for Restful-Booker

![](https://img.shields.io/badge/Code-Java%2017-informational?style=flat&color=blueviolet)
![](https://img.shields.io/badge/Framework-JUnit%205-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-REST%20Assured-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-AssertJ-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-Lombok-informational?style=flat&&color=blueviolet)
![](https://img.shields.io/badge/Library-Allure-informational?style=flat&&color=blueviolet)

App links : [Restful -Booker](https://restful-booker.herokuapp.com/),  [Restful-Booker API Docs](https://restful-booker.herokuapp.com/apidoc/index.html)

*Restful-Booker* is Web API playground created by Mark Winteringham. It offers authentication, CRUD operations and is loaded with bugs (for the purpose of learning). 

## Installation

There are few ways to start using this repository:
1. Clone with Git using command line and this command: `git clone https://github.com/kat-kan/restful-booker-api-tests`
2. Clone with Git using graphical user interface (for example Sourcetree)
3. Download ZIP with the code (using option shown in the screenshot below)

![image](https://user-images.githubusercontent.com/17500766/198851710-a4eb74c6-9e35-43c2-9392-7bb4bf9380ff.png)

### Authentication

According to the docs, you need the auth token for `PUT`, `PATCH` and `DELETE` methods. I wanted to write tests like in a "real" project, so the credentials are not provided in the code. You can find credentials info [here](https://restful-booker.herokuapp.com/apidoc/index.html#api-Auth-CreateToken). The credentials should be entered in restful-booker.properties file:
![image](https://user-images.githubusercontent.com/17500766/198851267-3394035d-f229-40b8-ab4d-174809666ba4.png)

### Running the tests

You can run tests:
- using "Run test" option in Intellij Idea IDE
- using `mvn clean test` command in terminal


## Allure Reporting

Allure reports are configured for this project. To get the report, you can use these commands:
- `mvn allure:serve` (recommended) - opens browser with generated report
- `mvn allure:report` - generates report to temp folder

![allureReport](https://user-images.githubusercontent.com/17500766/198852022-627b90c1-eb3b-4d5b-9160-33ecdc2f1714.png)





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

`https://restful-booker.herokuapp.com/ping` - HealthCheck
* Ping returns 201 Created - I believe 200 OK would be a better choice
