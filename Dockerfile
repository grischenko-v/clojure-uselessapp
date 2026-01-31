# ---- build stage ----
FROM clojure:temurin-21-tools-deps AS build
WORKDIR /app

# Чтобы кешировать зависимости:
COPY deps.edn /app/
RUN clojure -P

# Теперь исходники
COPY . /app/

RUN clojure -X:uberjar

# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/app.jar /app/app.jar

EXPOSE 3000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]