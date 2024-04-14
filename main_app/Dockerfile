FROM node:21 AS builder

ARG APP_DIR=/Main-App-Frontend

WORKDIR ${APP_DIR}

RUN npm i -g @angular/cli

COPY /Main-App-Frontend/package* .
COPY /Main-App-Frontend/angular.json .
COPY /Main-App-Frontend/tsconfig.* .
COPY /Main-App-Frontend/src src
COPY /Main-App-Frontend/ngsw-config.json .

RUN npm ci && ng build


FROM maven:3.9-eclipse-temurin-21 AS builder2

ARG APP_DIR1=/src

WORKDIR ${APP_DIR1}

COPY /Main-App-Backend/mvnw .
COPY /Main-App-Backend/mvnw.cmd .
COPY /Main-App-Backend/pom.xml .

COPY /Main-App-Backend/.mvn .mvn
COPY /Main-App-Backend/src src

COPY --from=builder /Main-App-Frontend/dist/frontend/browser/. ./src/main/resources/static/.

RUN mvn package -Dmaven.test.skip=true

FROM openjdk:21-jdk

ARG APP_DIR2=/JD
WORKDIR ${APP_DIR2}

ENV PORT=8080
ENV SPRING_DATA_MONGODB_URI=
ENV SPRING_DATASOURCE_URL=
ENV APPLICATION_URL=
ENV IGDB_APPLICATION_URL=
ENV APPLICATION_SECURITY_JWT_SECRET-KEY=
ENV APPLICATION_SECURITY_JWT_EXPIRATION=
ENV APPLICATION_SECURITY_JWT_REFRESH-TOKEN_EXPIRATION=

COPY --from=builder2 /src/target/Main-App-Backend-0.0.1-SNAPSHOT.jar application.jar

EXPOSE ${PORT}

ENTRYPOINT SERVER_PORT=${PORT} java -jar application.jar