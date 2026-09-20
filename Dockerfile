# Paso 1: Usar Alpine con Java 17 e instalar Maven directamente
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Instalar Maven en la imagen
RUN apk add --no-cache maven

# Copiar el código del proyecto
COPY . .

# Compilar omitiendo tests
RUN mvn clean package -DskipTests

# Paso 2: Imagen de ejecución liviana
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]