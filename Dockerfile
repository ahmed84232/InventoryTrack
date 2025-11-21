FROM openjdk:26-ea-24-jdk-oraclelinux9

WORKDIR /home
COPY target/InventoryTrack-1.0.0.jar InventoryTrack-1.0.0.jar

ENTRYPOINT ["java", "-jar", "InventoryTrack-1.0.0.jar"]
