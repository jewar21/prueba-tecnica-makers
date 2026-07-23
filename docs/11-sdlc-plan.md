# Plan SDLC

Ciclo de vida del desarrollo del sistema, organizado en fases.

---

## Fase 1 — Planning

### Objetivos

- Delimitar el MVP
- Resolver ambigüedades
- Priorizar historias
- Definir criterios de aceptación
- Identificar riesgos

### Entregables

- Scope
- Historias de usuario
- Backlog priorizado
- Supuestos
- Definition of Done

---

## Fase 2 — Analysis

### Actividades

- Modelar actores
- Definir reglas
- Diseñar estados
- Identificar endpoints
- Definir seguridad
- Definir validaciones
- Analizar transacciones
- Analizar caché

### Entregables

- Modelo de dominio
- Casos de uso
- Reglas de negocio
- Contratos preliminares
- Matriz de permisos

---

## Fase 3 — Design

### Actividades

- Diseñar arquitectura hexagonal
- Diseñar esquema de base de datos
- Diseñar API
- Diseñar JWT
- Diseñar componentes Angular
- Diseñar estrategia de caché
- Diseñar manejo de errores

### Entregables

- Diagrama de componentes
- Modelo entidad-relación
- OpenAPI
- Wireframes
- Estructura de carpetas

---

## Fase 4 — Implementation

### Orden sugerido

1. Crear repositorio
2. Crear backend
3. Crear entidades y migraciones
4. Implementar autenticación
5. Implementar creación y consulta
6. Implementar aprobación y rechazo
7. Agregar transacciones
8. Agregar caché
9. Agregar errores
10. Crear Angular
11. Crear login
12. Crear panel de usuario
13. Crear panel de administrador
14. Integrar JWT, interceptor y guards

---

## Fase 5 — Testing

### Unitarias

- Crear préstamo válido
- Rechazar monto inválido
- Aprobar pendiente
- Impedir aprobar resuelto
- Rechazar pendiente
- Impedir acceso de usuario

### Integración

- Login
- Crear préstamo
- Consultar préstamos propios
- Aprobar como administrador
- Responder 403 como usuario
- Persistencia y rollback

### Frontend

- Validación del formulario
- Guard
- Servicio HTTP
- Renderización por rol

---

## Fase 6 — Deployment y entrega

### Entregables

- Repositorio público
- README
- Instrucciones de ejecución
- Credenciales de demostración
- Decisiones técnicas
- Supuestos
- Colección de endpoints o Swagger
- Evidencia de pruebas

---

## Fase 7 — Maintenance

> Aunque la prueba termina con la entrega, conviene documentar:

- Mejoras pendientes
- Riesgos
- Limitaciones
- Deuda técnica
- Posibles evoluciones

Esto demuestra pensamiento de ciclo de vida completo.
Lamentablemente el tiempo es demasiado corto y no creo alcanzar a realizar esto como deberia. :(
