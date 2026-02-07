# EndPoints Snapshot

Data i godzina: 2026-02-07 16:03:46 CET

## Informacje ogolne
- Bazowa sciezka: `/api`
- Zrodlo: kontrolery REST w `src/main/java/com/project/controller`

## Projekt

| Metoda | Sciezka | Opis |
| --- | --- | --- |
| GET | `/api/projekty/{projektId}` | Pobranie projektu po ID |
| POST | `/api/projekty` | Utworzenie projektu |
| PUT | `/api/projekty/{projektId}` | Aktualizacja projektu |
| DELETE | `/api/projekty/{projektId}` | Usuniecie projektu |
| GET | `/api/projekty` | Lista projektow (paginacja) |
| GET | `/api/projekty?nazwa={nazwa}` | Lista projektow filtrowana po nazwie |

## Student

| Metoda | Sciezka | Opis |
| --- | --- | --- |
| GET | `/api/studenci/{studentId}` | Pobranie studenta po ID |
| POST | `/api/studenci` | Utworzenie studenta |
| PUT | `/api/studenci/{studentId}` | Aktualizacja studenta |
| DELETE | `/api/studenci/{studentId}` | Usuniecie studenta |
| GET | `/api/studenci` | Lista studentow (paginacja) |
| GET | `/api/studenci?nazwisko={nazwisko}` | Lista studentow filtrowana po nazwisku |
| GET | `/api/studenci/nrIndeksu/{nrIndeksu}` | Pobranie studenta po numerze indeksu |

## Zadanie

| Metoda | Sciezka | Opis |
| --- | --- | --- |
| GET | `/api/zadania/{zadanieId}` | Pobranie zadania po ID |
| POST | `/api/zadania` | Utworzenie zadania |
| PUT | `/api/zadania/{zadanieId}` | Aktualizacja zadania |
| DELETE | `/api/zadania/{zadanieId}` | Usuniecie zadania |
| GET | `/api/zadania` | Lista zadan (paginacja) |
| GET | `/api/projekty/{projektId}/zadania` | Lista zadan w projekcie (paginacja) |
