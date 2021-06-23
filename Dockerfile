FROM youmoni/jdk11
EXPOSE 8080
ADD build/libs/exchange-changing-gif-service-v1.jar exchange-changing-gif-service-v1.jar
ENTRYPOINT ["java", "-jar", "/exchange-changing-gif-service-v1.jar"]