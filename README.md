# data-handler

This service acts as  Backend service, which will receive a validated data from other services like **user-ops** which will be further processed and stored as a file and vice versa.


**Steps to setup and run the data-handler service :**

1.Clone the project **data-handler** and import the project in Eclipse or STS(Spring Tool Suite).

2.Change SQL connection details as per your system in application properties and create new schema with name **user_data**.

3.Run Maven install in Eclipse/STS or mvn install from cmd prompt from the project location.

4.Once build is **Successful**, run the application as java or spring boot App.

5.This Service is for handling the data which is sent from other services(Ex: **user-ops**), so there is no exposed API's.
