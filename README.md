# Banquillo

Gestor de equipos de fútbol base: plantilla, asistencia a entrenamientos, estadísticas y reparto justo de minutos para que todos los niños jueguen.

Es mi proyecto personal de aprendizaje mientras estudio ASIR. Lo construyo por fases para practicar todo el ciclo: programar, probar, empaquetar y desplegar en la nube.

> Solo se usan datos inventados. Nunca datos reales de menores.

## Estado

| Fase | Contenido | Estado |
|---|---|---|
| 0 | Entorno: WSL2, VS Code, Git con SSH, tablero y protección de `main` | ✅ Hecha |
| 1 | Núcleo en Java 21: consola, modelo, tests con JUnit 5, JSON y rotación justa | ✅ Hecha |
| 2 | API REST con Spring Boot y PostgreSQL, OpenAPI y Docker Compose | ✅ Hecha |
| 3 | Web con React y TypeScript | ⏳ Siguiente |
| 4 | CI/CD con GitHub Actions y despliegue en AWS con Terraform | Pendiente |
| 5 | Kubernetes (k3s), avisos por eventos y monitorización | Pendiente |
| 6 | Datos e IA | Pendiente |
| 7 | Pulido, seguridad y demo | Pendiente |

## Estructura

```
banquillo-app/
├── api/           # API REST (Fase 2): Spring Boot 4, PostgreSQL, Flyway
├── core/          # Núcleo en Java (Fase 1): app de consola
├── docs/adr/      # Decisiones de arquitectura (ADR)
├── compose.yaml   # PostgreSQL y la API en Docker
└── .github/       # CI con GitHub Actions
```

## API (`api`)

**Qué hace:** jugadores, entrenamientos, pasar lista, porcentaje de asistencia y reparto justo de minutos por partido, con errores en formato estándar (RFC 9457).

**Tecnología:** Java 21, Spring Boot 4.1, Spring Data JPA, PostgreSQL 17, Flyway, springdoc-openapi, Testcontainers y Docker.

**Arrancarla con Docker** (solo necesitas Docker):

```bash
docker compose --profile completo up -d --build
```

- API: http://localhost:8080/api/jugadores
- Documentación interactiva: http://localhost:8080/swagger-ui.html
- Salud: http://localhost:8080/actuator/health

**Desarrollo** (Java 21 y Maven):

```bash
docker compose up -d      # solo la base de datos
cd api
mvn verify                # 26 tests contra un PostgreSQL real (Testcontainers)
mvn spring-boot:run
```

| Método | Ruta | Qué hace |
|---|---|---|
| GET / POST | `/api/jugadores` | Listar / crear jugadores |
| GET / PUT / DELETE | `/api/jugadores/{dorsal}` | Ver / cambiar / borrar |
| GET / POST | `/api/entrenamientos` | Listar / crear entrenamientos |
| PUT | `/api/entrenamientos/{fecha}/asistentes` | Pasar lista |
| GET | `/api/asistencia` | Asistencia de toda la plantilla |
| POST | `/api/partidos/rotacion` | Reparto justo de minutos |

## Núcleo en Java (`core`)

App de consola de la Fase 1: plantilla, asistencia, estadísticas, guardado en JSON y rotación justa.

```bash
cd core
mvn test                  # 41 tests
mvn -q compile exec:java  # arranca el menú
```

## Forma de trabajar

- `main` protegida: todo entra por pull request, con los tests pasando en GitHub Actions.
- Una issue por tarea, agrupadas por fase en milestones.
- Commits con [Conventional Commits](https://www.conventionalcommits.org/es/) (`feat:`, `fix:`, `test:`, `docs:`…).
- Decisiones importantes documentadas en [`docs/adr`](docs/adr).

## Licencia

[MIT](LICENSE)
