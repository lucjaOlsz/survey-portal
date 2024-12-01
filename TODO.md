### Registration
- [x] validation passwords, Where to put DTO validation, and when to convert to entity, controller or service? Handle errors from database during registration
- [x] EmailVerification feature
- [x] Send verification again

### Other
- [x] global messages (success, error, info) in layout

### User
- [ ] User profile page, with edit phone number only
- [ ] About site page

- [x] Survey page, with questions based on user's profile

- [ ] List with services, with filters, and search. 
- [ ] Pricing page
- [ ] Contact page
- [ ] Home Survives Services Pricing Contact

### Endpoints
- [x] (POST) Create survey -> /createSurvey
- [ ] (GET) List of user's surveys. (Only owner of surveys can see it) -> localhost:8080/survey
- [ ] (PUT, PATCH) Edit survey. (Only owner of surveys can see it) -> localhost:8080/survey/{{ id }}
- [ ] (GET) Get survey. (Only owner of surveys can see it) -> localhost:8080/survey/{{ id }}

---

## 1. Stworzenie ankiety (POST)
### Endpoint:
`/createSurvey`

### Opis:
Pozwala użytkownikom na stworzenie ankiety poprzez przesłanie odpowiednich danych.

---

## 2. Lista ankiet użytkownika (GET)
### Endpoint:
`localhost:8080/survey`

### Opis:
Pobiera i wyświetla listę ankiet stworzonych przez zalogowanego użytkownika (właściciela). Tylko właściciel ma dostęp do swoich ankiet.

### Wymagania frontendowe:
- **Dashboard/Mainboard dla użytkownika:** Stwórz interfejs użytkownika do wyświetlenia listy ankiet należących do użytkownika.
- **Wyświetlane komponenty:** Dodamy w przyszlosci jakies inne funkcjonalnosci ktore znajda sie w tym dashboardzie.

### Kroki backendowe:
1. **Repository:**
    - Dodaj zapytanie filtrujące ankiety po `user_id`, aby pobrać wszystkie ankiety należące do użytkownika.
    - Przykład:
      ```java
      List<Survey> findAllByUserId(Long userId); /// czy cos takiego
      ```
2. **Service:**
    - Dodaj funkcję, która korzysta z powyższego zapytania do pobrania listy ankiet dla danego użytkownika.
3. **Controller:**
    - Utwórz endpoint GET zwracający listę ankiet zalogowanego użytkownika.

---

## 3. Edytowanie ankiety (PUT/PATCH)
### Endpoint:
`localhost:8080/survey/{{ id }}`

### Opis:
Pozwala użytkownikowi na edytowanie swoich ankiet. Tylko właściciel ankiety może wprowadzać zmiany.

### Kroki implementacji:
1. **Sprawdzenie uprawnień:**
    - Upewnij się, że zalogowany użytkownik jest właścicielem ankiety. Podoba implementacja do registerUser jest. `UserServiceImpl`
    - Opcjonalnie stwórz specjalną warstwę **Permission**, aby skalować to rozwiązanie.
2. **Repository:**
    - Jeśli jeszcze nie istnieje, dodaj zapytanie zapisujące zmiany w ankiecie (np. `saveSurvey`).
3. **DTO:**
    - Stwórz klasę Data Transfer Object (DTO), która będzie przyjmowała dane z frontendu.
    - DTO powinno zawierać pola odpowiadające właściwościom ankiety, podobnie jak istniejący `UserDto`.
    - Przykład:
      ```java
      public class SurveyDto {
          private String title;
          private String description;
          // Inne pola w zależności od potrzeb
      }
      ```
4. **Service:**
    - Dodaj metodę `saveSurvey(Long id, SurveyDto dto)` w warstwie serwisowej do obsługi danych i zapisania ich w bazie.
    - Skorzystaj z metody `saveSurvey` w repository, aby zapisać dane.
5. **Controller:**
    - Utwórz endpoint `PATCH`, aby obsłużyć aktualizację ankiety.
    - Przykład:
      ```java
      @PatchMapping("/survey/{id}")
      public String updateSurvey(@PathVariable Long id, @RequestBody SurveyDto dto) {
          /// trzeba sprawdzic czy user jest wlascicielem ankiety
          surveyService.saveSurvey(id, dto);
      }
      ```

---

## 4. Zatwierdzenie ankiety (POST)
### Endpoint:
`submitSurvey/{id}`

### Opis:
Endpoint do walidacji i zatwierdzenia odpowiedzi na ankietę. Szczegółowe wymagania dotyczące walidacji zostaną dodane później.

---

## 5. Pobranie ankiety (GET)
### Endpoint:
`localhost:8080/survey/{{ id }}`

### Opis:
Pobiera szczegóły ankiety na podstawie ID. Tylko właściciel ankiety ma dostęp do tych danych.

### Kroki implementacji:
1. **Repository:**
    - Skorzystaj z metody `findById`, aby pobrać ankietę na podstawie ID.
2. **Service:**
    - Dodaj funkcję sprawdzającą uprawnienia i zwracającą szczegóły ankiety.
3. **Controller:**
    - Utwórz endpoint GET zwracający ankietę.
    - Przykład:
      ```java
      @GetMapping("/survey/{id}")
      public String getSurvey(@PathVariable Long id) {
          Survey survey = surveyService.getSurveyById(id);
          /// bedzie trzreba jakas sensowna templatke zrobic dla opinii, scheckuj jak wygladaja na stronce tailwinda componenty.
      }
      ```
4. **Sprawdzenie uprawnień:**
    - Zweryfikuj, czy zalogowany użytkownik jest właścicielem ankiety, zanim zwrócisz dane.

---