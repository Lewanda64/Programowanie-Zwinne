# EndPoints Snapshot

Data i godzina: 2026-02-07 23:57:26 CET

## Informacje ogolne
- Bazowa sciezka: `/api`
- Zrodlo: kontrolery REST w `src/main/java/com/project/controller`

## Role i dostep
- `/api/register` jest publiczne (permitAll) i sluzy do samorejestracji (rola USER).
- `/api/studenci/**` jest tylko dla ADMIN.
- `/api/projekty/**` i `/api/zadania/**` sa dostepne dla USER i ADMIN.
- `POST /api/register` moze zwracac naglowek `Location` wskazujacy `/api/studenci/{id}`; ten endpoint jest ADMIN-only, wiec zwykly USER dostanie 403 przy follow-up.

## Projekt

| Metoda | Sciezka | Opis |
| --- | --- | --- |
| GET | `/api/projekty/{projektId}` | Pobranie projektu po ID |
| POST | `/api/projekty` | Utworzenie projektu |
| PUT | `/api/projekty/{projektId}` | Aktualizacja projektu |
| DELETE | `/api/projekty/{projektId}` | Usuniecie projektu |
| GET | `/api/projekty` | Lista projektow (paginacja) |
| GET | `/api/projekty?nazwa={nazwa}` | Lista projektow filtrowana po nazwie |

### GET /api/projekty/{projektId}
```json
{
  "method": "GET",
  "path": "/api/projekty/{projektId}",
  "pathParams": {
    "projektId": 1
  },
  "query": {},
  "body": null
}
```

### POST /api/projekty
```json
{
  "method": "POST",
  "path": "/api/projekty",
  "pathParams": {},
  "query": {},
  "body": {
    "nazwa": "Projekt A",
    "opis": "Opis projektu",
    "dataOddania": "2026-03-01"
  }
}
```

### PUT /api/projekty/{projektId}
```json
{
  "method": "PUT",
  "path": "/api/projekty/{projektId}",
  "pathParams": {
    "projektId": 1
  },
  "query": {},
  "body": {
    "nazwa": "Projekt A (zmieniony)",
    "opis": "Nowy opis",
    "dataOddania": "2026-04-15"
  }
}
```

### DELETE /api/projekty/{projektId}
```json
{
  "method": "DELETE",
  "path": "/api/projekty/{projektId}",
  "pathParams": {
    "projektId": 1
  },
  "query": {},
  "body": null
}
```

### GET /api/projekty
```json
{
  "method": "GET",
  "path": "/api/projekty",
  "pathParams": {},
  "query": {
    "page": 0,
    "size": 10,
    "sort": "nazwa,desc"
  },
  "body": null
}
```

### GET /api/projekty?nazwa={nazwa}
```json
{
  "method": "GET",
  "path": "/api/projekty",
  "pathParams": {},
  "query": {
    "nazwa": "webowa",
    "page": 0,
    "size": 10,
    "sort": "nazwa,asc"
  },
  "body": null
}
```

## Student

| Metoda | Sciezka | Opis |
| --- | --- | --- |
| POST | `/api/register` | Rejestracja studenta (publiczna) |
| GET | `/api/studenci/{studentId}` | Pobranie studenta po ID |
| POST | `/api/studenci` | Utworzenie studenta (admin) |
| PUT | `/api/studenci/{studentId}` | Aktualizacja studenta |
| DELETE | `/api/studenci/{studentId}` | Usuniecie studenta |
| GET | `/api/studenci` | Lista studentow (paginacja) |
| GET | `/api/studenci?email={email}` | Lista studentow filtrowana po email |
| GET | `/api/studenci?imie={imie}` | Lista studentow filtrowana po imieniu |
| GET | `/api/studenci?nazwisko={nazwisko}` | Lista studentow filtrowana po nazwisku |
| GET | `/api/studenci/nrIndeksu/{nrIndeksu}` | Pobranie studenta po numerze indeksu |

### POST /api/register
```json
{
  "method": "POST",
  "path": "/api/register",
  "pathParams": {},
  "query": {},
  "body": {
    "imie": "Jan",
    "nazwisko": "Kowalski",
    "nrIndeksu": "12345",
    "email": "jan.kowalski@example.com",
    "stacjonarny": true,
    "password": "haslo123"
  }
}
```

### GET /api/studenci/{studentId}
```json
{
  "method": "GET",
  "path": "/api/studenci/{studentId}",
  "pathParams": {
    "studentId": 1
  },
  "query": {},
  "body": null
}
```

### POST /api/studenci
Nota: pole `password` jest ignorowane w backendzie dla tego endpointu.
```json
{
  "method": "POST",
  "path": "/api/studenci",
  "pathParams": {},
  "query": {},
  "body": {
    "imie": "Jan",
    "nazwisko": "Kowalski",
    "nrIndeksu": "12345",
    "email": "jan.kowalski@example.com",
    "stacjonarny": true,
    "projekty": [
      {
        "projektId": 1
      }
    ]
  }
}
```

### PUT /api/studenci/{studentId}
```json
{
  "method": "PUT",
  "path": "/api/studenci/{studentId}",
  "pathParams": {
    "studentId": 1
  },
  "query": {},
  "body": {
    "imie": "Jan",
    "nazwisko": "Kowalski",
    "nrIndeksu": "12345",
    "email": "jan.kowalski@example.com",
    "stacjonarny": false,
    "projekty": [
      {
        "projektId": 1
      },
      {
        "projektId": 2
      }
    ]
  }
}
```

### DELETE /api/studenci/{studentId}
```json
{
  "method": "DELETE",
  "path": "/api/studenci/{studentId}",
  "pathParams": {
    "studentId": 1
  },
  "query": {},
  "body": null
}
```

### GET /api/studenci
```json
{
  "method": "GET",
  "path": "/api/studenci",
  "pathParams": {},
  "query": {
    "page": 0,
    "size": 10,
    "sort": "nazwisko,asc"
  },
  "body": null
}
```

### GET /api/studenci?email={email}
```json
{
  "method": "GET",
  "path": "/api/studenci",
  "pathParams": {},
  "query": {
    "email": "jan.kowalski@",
    "page": 0,
    "size": 10,
    "sort": "email,asc"
  },
  "body": null
}
```

### GET /api/studenci?imie={imie}
```json
{
  "method": "GET",
  "path": "/api/studenci",
  "pathParams": {},
  "query": {
    "imie": "Jan",
    "page": 0,
    "size": 10,
    "sort": "imie,asc"
  },
  "body": null
}
```

### GET /api/studenci?nazwisko={nazwisko}
```json
{
  "method": "GET",
  "path": "/api/studenci",
  "pathParams": {},
  "query": {
    "nazwisko": "Kow",
    "page": 0,
    "size": 10,
    "sort": "nazwisko,asc"
  },
  "body": null
}
```

### GET /api/studenci/nrIndeksu/{nrIndeksu}
```json
{
  "method": "GET",
  "path": "/api/studenci/nrIndeksu/{nrIndeksu}",
  "pathParams": {
    "nrIndeksu": "12345"
  },
  "query": {},
  "body": null
}
```

## Zadanie

| Metoda | Sciezka | Opis |
| --- | --- | --- |
| GET | `/api/zadania/{zadanieId}` | Pobranie zadania po ID |
| POST | `/api/zadania` | Utworzenie zadania |
| PUT | `/api/zadania/{zadanieId}` | Aktualizacja zadania |
| DELETE | `/api/zadania/{zadanieId}` | Usuniecie zadania |
| GET | `/api/zadania` | Lista zadan (paginacja) |
| GET | `/api/projekty/{projektId}/zadania` | Lista zadan w projekcie (paginacja) |

### GET /api/zadania/{zadanieId}
```json
{
  "method": "GET",
  "path": "/api/zadania/{zadanieId}",
  "pathParams": {
    "zadanieId": 1
  },
  "query": {},
  "body": null
}
```

### POST /api/zadania
```json
{
  "method": "POST",
  "path": "/api/zadania",
  "pathParams": {},
  "query": {},
  "body": {
    "nazwa": "Zadanie 1",
    "opis": "Opis zadania",
    "kolejnosc": 1,
    "projekt": {
      "projektId": 1
    }
  }
}
```

### PUT /api/zadania/{zadanieId}
```json
{
  "method": "PUT",
  "path": "/api/zadania/{zadanieId}",
  "pathParams": {
    "zadanieId": 1
  },
  "query": {},
  "body": {
    "nazwa": "Zadanie 1 (zmienione)",
    "opis": "Nowy opis",
    "kolejnosc": 2,
    "projekt": {
      "projektId": 1
    }
  }
}
```

### DELETE /api/zadania/{zadanieId}
```json
{
  "method": "DELETE",
  "path": "/api/zadania/{zadanieId}",
  "pathParams": {
    "zadanieId": 1
  },
  "query": {},
  "body": null
}
```

### GET /api/zadania
```json
{
  "method": "GET",
  "path": "/api/zadania",
  "pathParams": {},
  "query": {
    "page": 0,
    "size": 10,
    "sort": "nazwa,asc"
  },
  "body": null
}
```

### GET /api/projekty/{projektId}/zadania
```json
{
  "method": "GET",
  "path": "/api/projekty/{projektId}/zadania",
  "pathParams": {
    "projektId": 1
  },
  "query": {
    "page": 0,
    "size": 10,
    "sort": "nazwa,asc"
  },
  "body": null
}
```
