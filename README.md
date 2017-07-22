# webservice

Hello,

This webservice is developed with Spring boot as a base.

A small controller is defined to meet the following requirements.(https://github.com/shakeelabbas1/webservice/blob/master/src/main/java/com/service/controller/ServiceRequestController.java)

"localhost:8080/handleJson" OR "localhost:8080/handleParam" to recieve payload. It does reply with a response with confirmation.
"localhost:8080/getAll" to get all the messages persisted in database.

Cosumer thread to read messages from Redis and save it to database is defined here https://github.com/shakeelabbas1/webservice/blob/master/src/main/java/com/service/threads/ConsumerThread.java

A websocket listener can be acccessed by pasting below URL.
http://localhost:8080/

Real time messages can be seen here
http://localhost:8080/realTimeMessages.html


# How to make all this work.

1. Start the docker 
2. Start postgresql using below command
   sudo  docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres
3. Start redis using below command
   sudo docker run --name some-redis -d redis
4. Start a new container with java8 installed
5. Get the IP address of postgresql and fill here https://github.com/shakeelabbas1/webservice/blob/master/src/main/resources/application.properties
6. Get the IP address of Redis and fill here 
https://github.com/shakeelabbas1/webservice/blob/master/src/main/resources/beans.xml
7. Build and run the Spring boot jar.


