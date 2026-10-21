# Banquillo

Gestor de equipos de fútbol base: plantilla, asistencia a entrenamientos, estadísticas y reparto justo de minutos para que todos los niños jueguen.

Es mi proyecto personal de aprendizaje mientras estudio ASIR. Lo construyo por fases para practicar todo el ciclo: programar, probar, empaquetar y desplegar en la nube.

> Solo se usan datos inventados. Nunca datos reales de menores.

## Estado

| Fase | Contenido | Estado |
|---|---|---|
| 0 | Entorno: WSL2, VS Code, Git con SSH, tablero y protección de `main` | ✅ Hecha |
| 1 | Núcleo en Java 21: consola, modelo, tests con JUnit 5, JSON y rotación justa | ✅ Hecha |
| 2 | API REST con Spring Boot y PostgreSQL, OpenAPI y Docker Compose | ⏳ Siguiente |
| 3 | Web con React y TypeScript | Pendiente |
| 4 | CI/CD con GitHub Actions y despliegue en AWS con Terraform | Pendiente |
| 5 | Kubernetes (k3s), avisos por eventos y monitorización | Pendiente |
| 6 | Datos e IA | Pendiente |
| 7 | Pulido, seguridad y demo | Pendiente |

La Fase 1 la hice en una semana, a base de prueba y error y con Claude como apoyo.

## Estructura

```
banquillo-app/
├── core/        # Núcleo en Java (Fase 1): app de consola y tests
└── docs/adr/    # Decisiones de arquitectura (ADR)
```

## Núcleo en Java (`core`)

**Qué hace:** plantilla, entrenamientos, pasar lista, porcentaje de asistencia, entrenador, avisos, estadísticas, guardado en JSON y reparto justo de minutos por partido.

**Requisitos:** Java 21 y Maven 3.9 o superior.

```bash
cd core
mvn test                  # 41 tests
mvn -q compile exec:java  # arranca el menú
```

Los datos se guardan en `core/datos/banquillo.json`, que está fuera de Git.

## Forma de trabajar

- `main` protegida: todo entra por pull request.
- Una issue por tarea, agrupadas por fase en milestones.
- Commits con [Conventional Commits](https://www.conventionalcommits.org/es/) (`feat:`, `fix:`, `test:`, `docs:`…).
- Decisiones importantes documentadas en [`docs/adr`](docs/adr).

## Licencia

[MIT](LICENSE)
