# 0001 · Un solo repositorio para todo Banquillo

**Fecha:** 2026-10-18  
**Estado:** aceptada

## Contexto
Banquillo tendrá varias piezas: núcleo Java, API, web, infraestructura y datos.
Trabajo solo y quiero enseñar el proyecto completo en un único enlace.

## Decisión
Un único repositorio (monorepo) con una carpeta por pieza.

## Consecuencias
- Un solo sitio para issues, CI y documentación.
- La CI tendrá que saber qué carpeta ha cambiado para no construirlo todo siempre.
