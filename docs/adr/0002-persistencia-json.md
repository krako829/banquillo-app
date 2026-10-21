# 0002 · Persistencia en un archivo JSON

**Fecha:** 2026-10-18  
**Estado:** aceptada

## Contexto
La app de consola necesita guardar el equipo entre ejecuciones. Todavía no hay base de datos ni servidor.

## Decisión
Guardar todo en `datos/banquillo.json` con Jackson. El modelo no se serializa directamente:
`RepositorioJson` lo convierte a records sencillos (`DatosEquipo`) y la asistencia se guarda como lista de dorsales.

## Consecuencias
- Sencillo y legible; no hace falta instalar nada.
- Si el modelo cambia, solo hay que tocar `DatosEquipo` y `RepositorioJson`.
- No sirve para varios usuarios a la vez: en la Fase 2 se sustituirá por una base de datos.
- La carpeta `datos/` está en `.gitignore` para no subir nunca datos.
