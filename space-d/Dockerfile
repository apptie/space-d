FROM --platform=linux/amd64 eclipse-temurin:17-jre AS builder
WORKDIR /application
COPY ./build/libs/space-d.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

FROM --platform=linux/amd64 eclipse-temurin:17-jre
WORKDIR /application
COPY --from=builder /application/extracted/dependencies/ ./
COPY --from=builder /application/extracted/spring-boot-loader/ ./
COPY --from=builder /application/extracted/snapshot-dependencies/ ./
COPY --from=builder /application/extracted/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
