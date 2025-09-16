# 🚨 Incident Management system

## ⚙️ Specification

This system allows anonymous reporting of incidents at a specific location, as well as system administration of said incidents. The system needs to conform to the microservice architecture.

### Reporting a problem
- An anonymous user can select a location on the map (automatically or manually), and from the list of offered incident types, select the appropriate type and, possibly, subtype of the incident, as well as attach a text description and image. The use of any service that provides maps is allowed.
- All reports are stored in the database, but are not published publicly until they have been approved by a moderator.
- Anonymous users have access to a map with a view of incidents, with the ability to filter by (sub)type, location and time (e.g. last 24 hours, 7 days, 31 days, all reports).
- Moderators have access to unapproved reports, which they can approve or reject/deny. This part of the application can be implemented without a map or with a map view.
- User login should be enabled as a local basic user, or using a Google account with the domain *.etf.unibl.org using OAuth2.
- The frontend application must communicate exclusively via the API Gateway, without direct communication with microservices. Direct communication with microservices should be disabled in an arbitrary way.
- Services should be registered in Service Discovery components and configured using a Config Server.
- The application must be dockerized – each microservice has its own Dockerfile, and it is possible to run the entire system using docker-compose.

### CI/CD and deploy
- Set up a GitHub Actions workflow that performs a build for each service, pushes Docker image(s) to DockerHub.
- Enabled automatic deployment of the project to Render (or similar platform) from a GitHub repository.

### Optional requests
- The system contains a reporting service (analytics-service) that enables basic analysis and visualization of incidents by time, place and type.
- The system enables automatic translation of text entries into English (or from English) - the use of external services is allowed. It is not necessary that it be implemented as a separate service.

## 👨🏻‍💻 Technologies Used
Front-end:
* React + Typescript ⚛
* Ant design UI library, Axios 🌐

Back-end:
* Java JDK 21 LTS ☕, Spring Boot 3.5.3 🍃
* MySQL 🐬
* Hashicorp Vault 🔑
* Zipkin
* RabbitMQ
* Docker and docker compose 🐋
* Amazon S3 Bucket 🛢️


## 🛠️ General structure
The following figure shows the use of aforementioned technologies and use of microservice architecture. Additionally, 
the client microservices, such as auth-service or incident-service, are organized in a multi-modular Spring project with a parent POM, 
for easier and cleaner maintaining of dependencies.



## 📷 Screenshots



