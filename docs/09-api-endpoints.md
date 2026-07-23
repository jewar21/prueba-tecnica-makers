# API — Endpoints preliminares

> Endpoints definidos de forma preliminar para guiar la implementación. Los endpoints explícitos ayudan a comprender mejor la intención y a comunicar el diseño.

## Autenticación

| Método | Endpoint              | Acceso | Descripción                          |
|--------|-----------------------|--------|--------------------------------------|
| POST   | `/api/auth/login`     | Público | Iniciar sesión → JWT                |
| POST   | `/api/auth/register`  | Público | ~~Registro (no implementado en MVP)~~ |

> El registro no se implementará en esta fase. Se usarán datos precargados.

## Usuario

| Método | Endpoint               | Acceso  | Descripción                     |
|--------|------------------------|---------|---------------------------------|
| GET    | `/api/users/me`        | USER    | Perfil del usuario autenticado  |
| GET    | `/api/users`           | ADMIN   | Listar todos los usuarios       |
| GET    | `/api/users/{id}`      | ADMIN   | Obtener usuario por ID          |
| POST   | `/api/users`           | ADMIN   | Crear nuevo usuario             |
| PUT    | `/api/users/{id}`      | ADMIN   | Actualizar usuario              |

## Préstamos

| Método | Endpoint              | Acceso | Descripción                          |
|--------|-----------------------|--------|--------------------------------------|
| POST   | `/api/loans`          | USER   | Solicitar un préstamo                |
| GET    | `/api/loans/my`       | USER   | Listar préstamos del usuario actual  |
| GET    | `/api/loans/{id}`     | USER   | Obtener detalle de un préstamo propio |

## Administración

| Método | Endpoint                              | Acceso | Descripción                             |
|--------|---------------------------------------|--------|-----------------------------------------|
| GET    | `/api/admin/loans`                    | ADMIN  | Listar todas las solicitudes            |
| GET    | `/api/admin/loans?status=PENDING`     | ADMIN  | Filtrar solicitudes por estado          |
| PATCH  | `/api/admin/loans/{id}/approve`       | ADMIN  | Aprobar un préstamo pendiente           |
| PATCH  | `/api/admin/loans/{id}/reject`        | ADMIN  | Rechazar un préstamo pendiente          |
