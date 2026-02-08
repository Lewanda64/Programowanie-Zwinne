# DEVEL

## Wymagania
- JDK 21 (projekt korzysta z toolchain, ale lokalnie JDK musi byc dostepny)
- Gradle Wrapper: `./gradlew` (Windows: `gradlew.bat`)

## Komendy Gradle (kompletny zestaw dla tego projektu)

### Lista wszystkich dostepnych zadan
- `./gradlew tasks --all`

### Budowanie i uruchamianie
- `./gradlew clean` - czysci katalog `build`
- `./gradlew assemble` - sklada artefakty bez testow
- `./gradlew build` - pelny build (kompilacja + testy + paczki)
- `./gradlew bootRun` - uruchamia aplikacje Spring Boot
- `./gradlew bootJar` - buduje uruchamialny JAR Spring Boot
- `./gradlew bootBuildImage` - buduje obraz kontenera z aplikacja (spring-boot)

### Testy i kontrola jakosci
- `./gradlew test` - uruchamia testy (JUnit Platform)
- `./gradlew check` - zbiorcze zadanie kontroli jakosci (zawiera testy)

### Analiza zaleznosci
- `./gradlew dependencyCheckAnalyze` - skan CVE (OWASP Dependency-Check)
- `./gradlew dependencyCheckPurge` - czysci cache danych CVE
- `./gradlew dependencyUpdates` - raport dostepnych aktualizacji zaleznosci

### Diagnostyka
- `./gradlew build --info` - wiecej logow
- `./gradlew build --stacktrace` - pelny stacktrace przy bledach
- `./gradlew build -x test` - build bez testow

## Swagger / OpenAPI
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- Autoryzacja: Basic Auth (np. `admin/admin` lub dane studenta z bazy). Kliknij "Authorize" w Swagger UI.
