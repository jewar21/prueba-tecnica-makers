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
    │       └── out
    ├── application
    │   ├── port
    │   │   └── in
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

- **domain**: Contiene el modelo de negocio, excepciones del dominio y puertos de salida, sin depender de las capas externas.
- **application**: Puertos de entrada, servicios que orquestan casos de uso y DTOs.
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

## Autenticación y seguridad — Épica 1

- El backend usa JWT HS256 stateless y valida firma, expiración y roles en cada petición protegida.
- El frontend conserva únicamente el JWT en `sessionStorage`, valida su estructura y expiración para navegación y deriva el rol del claim. Esta validación cliente es UX, no una frontera de seguridad; el backend sigue siendo la autoridad.
- `sessionStorage` reduce la persistencia respecto de `localStorage`, pero un XSS aún podría leer el token. Deben evitarse HTML no confiable y scripts inline; una futura política CSP puede reforzar esta defensa.
- Las llamadas Angular usan `/api` relativo. `proxy.conf.json` redirige a `localhost:8080` durante desarrollo; en despliegue se espera API y frontend detrás del mismo origen o reverse proxy.
- El login realiza BCrypt incluso para correos inexistentes mediante un hash dummy, reduciendo enumeración por tiempo.
- Rate limiting y bloqueo progresivo requieren infraestructura compartida o gateway y quedan fuera del MVP local. Deben incorporarse antes de exponer el login públicamente.
- Flyway administra el esquema PostgreSQL; Hibernate solo lo valida.
- El seed de `USER` y `ADMIN` es opcional, exige todas las variables, rechaza correos iguales y nunca incorpora contraseñas al repositorio.

---

## Pendiente de completar

A medida que se avance en la implementación, se documentarán:

- Stack tecnológico concreto y versiones.
- Justificación detallada del uso de arquitectura hexagonal.
- Estrategia de caché (Caffeine, Redis, etc.).
- Estrategia de deduplicación de solicitudes.
- Manejo de idempotencia (idempotency-key, optimista, etc.).
- Decisiones sobre el CRUD de préstamos (ver ambigüedad en CU-09).
- Configuración de SonarQube.
