# Casos de uso

## CU-01 — Autenticar usuario

| Elemento | Detalle |
|----------|---------|
| Actor    | Usuario o administrador |
| Entrada  | Correo, contraseña |
| Salida   | JWT, rol, datos básicos del usuario |
| Errores  | Credenciales inválidas, usuario deshabilitado, formato incorrecto |

---

## CU-02 — Registrar usuario

El sistema precargará un usuario y un administrador. El CRUD de usuarios estará disponible solo para administradores.

> **Nota:** Queda pendiente como mejora un endpoint público de registro para `USER`. Nunca permitir que el cliente se registre a sí mismo como `ADMIN`.

---

## CU-03 — Solicitar préstamo

| Elemento | Detalle |
|----------|---------|
| Actor    | Usuario autenticado |
| Entrada  | Monto, plazo |
| Proceso  | Validar usuario → validar monto → validar plazo → crear préstamo → asignar `PENDING` → persistir |
| Salida   | `201 Created` |

---

## CU-04 — Consultar mis préstamos

| Elemento | Detalle |
|----------|---------|
| Actor    | Usuario autenticado |
| Proceso  | Obtener identidad desde JWT → consultar préstamos del usuario → usar caché → devolver lista |
| Salida   | `200 OK` |

---

## CU-05 — Consultar solicitudes administrativas

| Elemento | Detalle |
|----------|---------|
| Actor    | Administrador |
| Filtros  | Todas, pendientes (`PENDING`), por estado |
| Salida   | `200 OK` |

Para el MVP el filtro más importante es `status=PENDING`.

---

## CU-06 — Aprobar préstamo

| Elemento | Detalle |
|----------|---------|
| Actor    | Administrador |
| Precondición | `status = PENDING` |
| Proceso  | Buscar préstamo → validar estado → cambiar a `APPROVED` → registrar revisión → persistir transaccionalmente → invalidar caché |
| Salida   | Resultado de la operación |

---

## CU-07 — Rechazar préstamo

Equivalente a CU-06, pero cambia el estado a `REJECTED`.

> **Nota:** El documento no exige motivo de rechazo.

---

## CU-08 — CRUD de usuarios

Operaciones disponibles solo para administradores:

- Crear usuario
- Consultar usuarios
- Consultar usuario por ID
- Actualizar usuario
- Deshabilitar usuario (preferible a eliminar físicamente para una aplicación financiera)

---

## CU-09 — CRUD de préstamos

El requisito menciona CRUD completo, pero editar o eliminar préstamos puede entrar en conflicto con el flujo bancario. Interpretación:

- Crear solicitud
- Consultar solicitud
- Consultar listado
- Actualizar únicamente mediante transición administrativa
- Eliminar solo si está pendiente y pertenece al usuario (o no exponer eliminación pública)

> ⚠️ **Ambigüedad importante del documento.**
