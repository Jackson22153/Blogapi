FROM openjdk:17
COPY target/blogapi-0.0.1-SNAPSHOT.jar blogapi-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh ./entrypoint.sh
VOLUME /usr/src/app
# ENTRYPOINT ["java","-jar","/blogapi-0.0.1-SNAPSHOT.jar"]
CMD bash ./entrypoint.sh
