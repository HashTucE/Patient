FROM openjdk:19-alpine AS mediscreen_patient
COPY target/patient-0.0.1-SNAPSHOT.jar patient-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/patient-0.0.1-SNAPSHOT.jar"]
