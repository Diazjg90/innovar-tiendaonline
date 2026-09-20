# Paso 1: Usar Alpine con Java 17 e instalar Maven
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Instalar Maven
RUN apk add --no-cache maven

# Copiar el código fuente
COPY . .

# Compilar omitiendo tests y evitando autoconfiguración previa de DB
RUN mvn clean package -DskipTests -Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration

# Paso 2: Imagen final para ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]