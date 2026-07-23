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

> ✅ **Resolución Épica 1:** Los usuarios `USER` y `ADMIN` pueden precargarse mediante variables de entorno; no se versionan credenciales ni contraseñas.

## Estado — Épica 1

✅ Implementado:

- Login REST `POST /api/auth/login` con validación y errores 400/401.
- BCrypt, JWT HS256 configurable y sesiones stateless.
- Autorización `/api/admin/**` para `ADMIN`, con respuestas JSON 401/403.
- Persistencia de usuarios mediante JPA y migración Flyway.
- Seed opcional de un `USER` y un `ADMIN` exclusivamente mediante variables de entorno.
- Login Angular con Reactive Forms, servicio de estado, interceptor JWT y guards por rol.

Las pantallas `/loans` y `/admin/loans` son placeholders protegidos. La lógica de préstamos pertenece a las épicas 2 y 3.

## Estado — Épica 2

✅ Implementado:

- `POST /api/loans` — Crear solicitud con validación de monto y plazo (400 en errores). Solo USER.
- `GET /api/loans/my` — Consultar préstamos del usuario autenticado. Solo USER.
- Validaciones: monto > 0, plazo entre 1 y 360 meses.
- Pruebas unitarias y de integración del controlador.
- Frontend con formulario reactivo de solicitud, listado de préstamos con estados y diseño Makers Bank.

## Ejecución local

1. Copiar `.env.example` a `.env` y completar localmente los valores vacíos. **No versionar `.env`**.

2. Iniciar PostgreSQL:

   ```bash
   docker compose up -d
   ```

3. Exportar el entorno y ejecutar el backend:

   ```bash
   set -a
   source .env
   set +a
   cd backend
   ./mvnw spring-boot:run
   ```

4. En otra terminal, ejecutar Angular:

   ```bash
   cd frontend
   npm start
   ```

La aplicación web queda disponible en `http://localhost:4200` y la API en `http://localhost:8080`.

### Credenciales locales

Configura correos y contraseñas propios mediante `SEED_USER_*` y `SEED_ADMIN_*` en tu `.env`. El repositorio no proporciona credenciales predeterminadas.

## Verificación

```bash
(cd backend && ./mvnw test)
(cd frontend && npm test -- --watch=false --browsers=ChromeHeadless)
(cd frontend && npm run build)
```

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
| [11-sdlc-plan.md](docs/11-sdlc-plan.md) | Plan SDLC (Planning a Maintenance) |

## Preguntas resueltas

Las preguntas de la prueba técnica (las que alcancé a ver) están resueltas en este enlace:

[Prueba Técnica — Resolución de preguntas](https://star-saver-473.notion.site/Prueba-Tecnica-resolucion-de-preguntas-3a6a0cdf497e80d089a7f78d22c30663?source=copy_link)