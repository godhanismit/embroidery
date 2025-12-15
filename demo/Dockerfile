# Use Java 17 (recommended for Spring Boot)
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy all project files
COPY . .

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Expose default Spring Boot port
EXPOSE 8080

# Run the generated jar
CMD ["sh", "-c", "java -jar target/*.jar"]
