# Java 17
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy all files
COPY . .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Build using Maven Wrapper
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "java -jar target/*.jar"]
