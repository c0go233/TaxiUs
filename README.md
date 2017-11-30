# TaxiUs
TaxiUs is an Android mobile application that connect people who want to use a taxi service and have the similar departure and destination locations so that they can share a taxi and hence their fares.

# Major Functionalities Overview
- Logging in with Google account
- Creating new account and logging with it
- Providing auto-complete address suggestions
- Finding journeys created by other users within specified range of distance
- Allowing users who have similar departure and destination locations to talk to each other in a chatroom

# Application architecture overview
- The overal architecture of the application is based on **MVP design pattern**
- **Dependency injection** is implemented with Dager2
- Repositories (database) for the application are implemented by two services, **Azure mobile cloud database** and **Google firebase database**

# Used APIs
- Gson, OkHttp, Azure-mobile, Butterknife, Dagger2, Google Signin API, Google Map API, Google Place API, EventBus, Firebase
