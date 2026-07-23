# prueba-tecnica-makers

Prueba técnica — Sistema de solicitud y aprobación de préstamos.

## Flujo principal

```
Usuario inicia sesión
        ↓
Solicita préstamo
        ↓
Préstamo queda pendiente
        ↓
Administrador revisa solicitud
        ↓
Aprueba o rechaza
        ↓
Usuario consulta el resultado
```

## Actores

| Actor         | Puede                                                        | No puede                                              |
|---------------|--------------------------------------------------------------|-------------------------------------------------------|
| **Usuario**   | Autenticarse, crear solicitud, consultar sus préstamos, ver estado | Consultar préstamos ajenos, aprobar/rechazar, cambiar estado |
| **Admin**     | Autenticarse, consultar solicitudes, ver info del solicitante, aprobar/rechazar | — |
| **Sistema**   | Validar datos, persistir, autenticar (JWT), autorizar por rol, mantener consistencia, cachear, devolver errores HTTP claros | — |

> ⚠️ **Pendiente:** El documento usa credenciales de ejemplo pero no especifica si deben precargarse, registrarse dinámicamente o crearse mediante migraciones. Ver [ambigüedades](docs/07-ambiguities.md).

## Documentación

La documentación del proyecto está organizada en la carpeta [`docs/`](docs/):

| Documento | Contenido |
|-----------|-----------|
| [01-domain-entities.md](docs/01-domain-entities.md) | Entidades del dominio (User, Loan) |
| [02-business-rules.md](docs/02-business-rules.md) | Reglas de negocio (RN-01 a RN-09) |
| [03-use-cases.md](docs/03-use-cases.md) | Casos de uso (CU-01 a CU-09) |
| [04-functional-requirements.md](docs/04-functional-requirements.md) | Requisitos funcionales (RF-01 a RF-16) |
| [05-non-functional-requirements.md](docs/05-non-functional-requirements.md) | Requisitos no funcionales (RNF-01 a RNF-08) |
| [06-technical-decisions.md](docs/06-technical-decisions.md) | Decisiones técnicas, arquitectura y estructura de carpetas |
| [07-ambiguities.md](docs/07-ambiguities.md) | Ambigüedades detectadas y decisiones tomadas |
| [08-mvp-scope.md](docs/08-mvp-scope.md) | Alcance del MVP con checklist |
| [09-api-endpoints.md](docs/09-api-endpoints.md) | Endpoints preliminares de la API |
| [10-user-stories.md](docs/10-user-stories.md) | Historias de usuario con criterios de aceptación |