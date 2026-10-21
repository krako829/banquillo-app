# 0003 · API con Spring Boot y PostgreSQL

**Fecha:** 2026-10-21  
**Estado:** aceptada

## Contexto
La app de consola solo funciona en un ordenador y guarda en un archivo JSON.
La web (Fase 3) y el despliegue en AWS (Fase 4) necesitan una API y una base de datos de verdad.

## Decisión
- Nueva carpeta `api/` con Spring Boot 4.1 (Java 21). `core/` se queda como está.
- PostgreSQL 17 como base de datos; las tablas las crea Flyway con migraciones versionadas.
- Errores en formato Problem Details (RFC 9457) y documentación OpenAPI generada con springdoc.
- Tests de integración contra un PostgreSQL real con Testcontainers.
- Imagen Docker multi-stage y `compose.yaml` para arrancar todo en local.

## Consecuencias
- Hace falta Docker para desarrollar y para pasar los tests.
- La lógica de la rotación está copiada de `core`; si se queda así, `core` acabará retirándose.
- La misma imagen Docker es la que se desplegará en AWS.
