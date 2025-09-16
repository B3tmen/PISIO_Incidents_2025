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

<img width="1195" height="897" alt="PISIO-struktura" src="https://github.com/user-attachments/assets/cf601852-00dd-4fd4-8075-88723ae7438f" />

## 📷 Screenshots
- Anonymous user page
<img width="1920" height="920" alt="Incidents-anonymous-user_1" src="https://github.com/user-attachments/assets/8c3d59c0-0858-4ceb-9244-ee54b1b2be9c" />

- Anonymouse user reports an incident via modal
<img width="1920" height="919" alt="Incidents-anonymous-user_2" src="https://github.com/user-attachments/assets/9e72cb6c-39c2-44c5-a153-9ebf8f2006f7" />

- Result of successful reporting
<img width="1920" height="99" alt="Incidents-anonymous-user_3" src="https://github.com/user-attachments/assets/5733e18b-6061-4634-be11-2756d4badcac" />

- Moderator page and approval
<img width="1920" height="918" alt="Incidents-moderator_1" src="https://github.com/user-attachments/assets/72ed2903-e04d-4f2f-98a8-67d52dc5640d" />

- Interactive map for moderator
<img width="1920" height="919" alt="Incidents-moderator_2" src="https://github.com/user-attachments/assets/9850b72f-fdfa-4cfd-a5c3-c0385ebba926" />



