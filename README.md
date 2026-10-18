# banquillo-app
Youth football team manager: roster, attendance, training sessions and fair playing time. Java + Spring Boot, React + TypeScript, Docker, Terraform, AWS and Kubernetes. Learning project, work in progress.

## Banquillo core (Fase 1)

Aplicación de consola en Java 21 para gestionar un equipo de fútbol base.

**Qué hace:** plantilla, entrenamientos, asistencia, entrenador, avisos,
estadísticas, guardado en JSON y reparto justo de minutos por partido.

**Cómo ejecutarlo:**

```bash
cd core
mvn test                  # 41 tests
mvn -q compile exec:java  # arranca el menú
```

Los datos se guardan en `core/datos/banquillo.json` (fuera de Git). Solo se usan datos inventados.

La Fase 1 la hice en una semana, a base de prueba y error y con ayuda de Claude como apoyo.
