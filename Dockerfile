FROM openjdk:15
COPY ./target/music-platform-0.0.1-SNAPSHOT.jar music-platform-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","music-platform-0.0.1-SNAPSHOT.jar"]