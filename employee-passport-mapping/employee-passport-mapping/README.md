# Employee–Passport One-to-One Mapping (Pure Hibernate)

Spring Boot is used only for the **web layer** (`@RestController`, dependency
injection) and to read `application.properties`. Persistence is done with
**pure Hibernate** — `SessionFactory`, `Session`, `Transaction` — there is
**no Spring Data JPA** anywhere in this project (no `JpaRepository`,
no `@EnableJpaRepositories`, no `spring-boot-starter-data-jpa`).

## How persistence is wired

- `config/HibernateConfig.java` builds a single `SessionFactory` bean by hand
  using `org.hibernate.cfg.Configuration`, reading DB/Hibernate settings from
  `application.properties` via `@Value`.
- `dao/EmployeeDaoImpl.java` is the only class that talks to Hibernate. Every
  method opens a `Session`, begins a `Transaction`, does the work, commits or
  rolls back, and closes the session — written out explicitly rather than
  relying on Spring's `@Transactional`.

## Mapping

- `Employee` — inverse side: `@OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)`
- `Passport` — owning side, holds the FK: `@OneToOne(fetch = FetchType.EAGER)` + `@JoinColumn(name = "employee_id", unique = true)`

This intentionally mixes LAZY (Employee → Passport) and EAGER
(Passport → Employee) so both fetch strategies are demonstrated:
- `EmployeeDao.findById()` leaves `passport` as an uninitialized Hibernate
  proxy (lazy).
- `EmployeeDao.findByIdWithPassport()` uses HQL `left join fetch` to pull
  the Passport eagerly in one query. This is what `GET /employees/{id}` uses,
  so the DTO can be safely built and serialized after the session closes.

Deleting an `Employee` cascades (`CascadeType.ALL` + `orphanRemoval = true`)
and automatically deletes its `Passport` as well.

## API

| Method | Path                              | Description                    |
|--------|-----------------------------------|---------------------------------|
| POST   | `/employees`                      | Create an employee (no passport required) |
| POST   | `/employees/{employeeId}/passport`| Assign a passport to an existing employee |
| GET    | `/employees/{employeeId}`         | Get employee with passport (if any) |
| DELETE | `/employees/{employeeId}`         | Delete employee (cascades to passport) |

### Sample requests

```bash
# 1. Create employee (no passport yet)
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{"employeeName":"Prabha","department":"Engineering","salary":75000}'

# 2. Assign passport
curl -X POST http://localhost:8080/employees/1/passport \
  -H "Content-Type: application/json" \
  -d '{"passportNumber":"N1234567","country":"India","issueDate":"2023-01-10","expiryDate":"2033-01-10"}'

# 3. Get employee with passport
curl http://localhost:8080/employees/1

# 4. Delete employee (passport cascades too)
curl -X DELETE http://localhost:8080/employees/1
```

### Error responses

| Scenario                       | HTTP Status |
|---------------------------------|-------------|
| Employee not found              | 404 |
| Duplicate passport number       | 409 |
| Employee already has a passport | 400 |
| Missing/invalid fields          | 400 |
| Unexpected DB error              | 500 |

## Before running

1. Make sure MySQL is running locally and update the credentials in
   `src/main/resources/application.properties` if needed
   (`spring.datasource.url` / `username` / `password`). The database
   `employee_passport_db` is created automatically
   (`createDatabaseIfNotExist=true`) and tables are created/updated
   automatically (`hibernate.hbm2ddl.auto=update`).
2. `mvn spring-boot:run` or run `EmployeePassportMappingApplication` from
   IntelliJ.

## Note on this delivery

This project was completed in a sandboxed build environment that does **not**
have network access to Maven Central (`repo.maven.apache.org`), so I was not
able to run `mvn clean install` myself to get a green build log. Every file
was written and manually double- and triple-checked (imports, Hibernate 6 /
Jakarta Persistence APIs, brace/paren balance, HQL syntax) for correctness,
but please run `mvn clean install` (or open in IntelliJ and let it resolve
dependencies) on your machine as the final compilation check before use.
