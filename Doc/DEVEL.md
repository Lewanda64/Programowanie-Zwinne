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

### Clean build + artefakt
Linux/macOS:
```
./gradlew clean bootJar
```
Windows:
```
.\gradlew.bat clean bootJar
```

Po zbudowaniu plik JAR znajduje sie w: `build/libs/`

### Uruchamianie JAR
Linux/macOS:
```
java -jar build/libs/<nazwa-pliku>.jar
```
Windows:
```
java -jar build\libs\<nazwa-pliku>.jar
```

### Testy i kontrola jakosci
- `./gradlew test` - uruchamia testy (JUnit Platform)
- `./gradlew check` - zbiorcze zadanie kontroli jakosci (zawiera testy)

### Analiza zaleznosci
- `./gradlew dependencyCheckAnalyze` - skan CVE (OWASP Dependency-Check)
- `./gradlew dependencyCheckPurge` - czysci cache danych CVE
- `./gradlew dependencyUpdates` - raport dostepnych aktualizacji zaleznosci

## Testy i weryfikacja
Zapewnienie jakosci kodu.

- `test` - uruchamia testy jednostkowe i integracyjne.
- `check` - wykonuje wszystkie weryfikacje (glownie `test`, plus dodatkowe kroki jesli zostana dodane).

### Diagnostyka
- `./gradlew build --info` - wiecej logow
- `./gradlew build --stacktrace` - pelny stacktrace przy bledach
- `./gradlew build -x test` - build bez testow

## Uruchamianie (Localhost)
- `bootRun` - startuje aplikacje Spring Boot bezposrednio z kodu zrodlowego (Hot Swap mozliwy).
- Po starcie aplikacja jest dostepna domyslnie pod: `http://localhost:8081`

Linux/macOS:
```
./gradlew bootRun
```
Windows (PowerShell):
```
.\gradlew.bat bootRun
```

## Swagger / OpenAPI
- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`
- Autoryzacja: Basic Auth (np. `admin/admin` lub dane studenta z bazy). Kliknij "Authorize" w Swagger UI.
