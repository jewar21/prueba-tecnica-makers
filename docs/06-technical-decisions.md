# Decisiones técnicas

> Este documento recoge las decisiones técnicas tomadas durante el desarrollo del proyecto, junto con su justificación.

## Arquitectura propuesta

### Backend — Arquitectura hexagonal

```
backend/
└── src/main/java/com/makers/loans
    ├── domain
    │   ├── model
    │   ├── exception
    │   └── port
    │       ├── in
    │       └── out
    ├── application
    │   ├── service
    │   └── dto
    ├── infrastructure
    │   ├── web
    │   │   ├── controller
    │   │   └── advice
    │   ├── persistence
    │   │   ├── entity
    │   │   ├── repository
    │   │   └── adapter
    │   ├── security
    │   ├── cache
    │   └── config
    └── LoansApplication.java
```

- **domain**: Contiene el modelo de negocio, excepciones del dominio y puertos (interfaces para in/out).
- **application**: Servicios de aplicación que orquestan casos de uso y DTOs.
- **infrastructure**: Implementaciones técnicas: controladores REST, persistencia JPA, seguridad, caché y configuración.

### Frontend — Organización por funcionalidades

```
frontend/src/app/
├── core
│   ├── auth
│   ├── guards
│   ├── interceptors
│   └── services
├── shared
│   ├── components
│   └── models
├── features
│   ├── login
│   ├── user-loans
│   └── admin-loans
├── app.routes.ts
└── app.config.ts
```

- **core**: Servicios globales, autenticación, guards de rutas e interceptors HTTP.
- **shared**: Componentes reutilizables y modelos/DTOs compartidos.
- **features**: Módulos funcionales agrupados por feature (login, préstamos de usuario, administración).

---

## Pendiente de completar

A medida que se avance en la implementación, se documentarán:

- Stack tecnológico concreto y versiones.
- Justificación detallada del uso de arquitectura hexagonal.
- Estrategia de caché (Caffeine, Redis, etc.).
- Estrategia de deduplicación de solicitudes.
- Manejo de idempotencia (idempotency-key, optimista, etc.).
- Decisiones sobre el CRUD de préstamos (ver ambigüedad en CU-09).
- Decisiones sobre precarga de datos (data.sql, Flyway, Liquibase).
- Decisiones sobre validación de formularios en frontend.
- Configuración de SonarQube.
- Configuración de PostgreSQL.
