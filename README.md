# prueba-tecnica-makers

1. Tenemos un flujo principal del reto:
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

2. Que actores tiene este spftware?
Usuario

Puede:

Autenticarse.
Crear una solicitud de préstamo.
Consultar sus propias solicitudes.
Ver el estado de cada préstamo.

No debería:

Consultar préstamos de otros usuarios.
Aprobar o rechazar préstamos.
Cambiar directamente el estado de una solicitud.

Administrador

Puede:

Autenticarse.
Consultar solicitudes.
Ver información básica del solicitante.
Aprobar solicitudes pendientes.
Rechazar solicitudes pendientes.

TODO: El documento utiliza credenciales de ejemplo para usuario y administrador, pero no especifica si deben estar precargadas, registrarse dinámicamente o crearse mediante migraciones.

Sistema

Debe:

Validar datos.
Persistir usuarios y préstamos.
Autenticar mediante JWT.
Autorizar acciones según rol.
Mantener consistencia en cambios de estado.
Almacenar en caché consultas repetitivas.
Devolver errores HTTP claros.
Registrar estados correctamente.

3. Entidades del dominio

Usuario

Información mínima inferida:

User
- id
- email
- password
- role
- enabled
- createdAt

Reglas mínimas
El correo debe ser obligatorio.
El correo debe ser válido.
El correo debe ser único.
La contraseña no debe almacenarse en texto plano.
El rol debe ser USER o ADMIN.
Un usuario solo consulta sus préstamos.

Préstamo

Información mínima:

Loan
- id
- userId
- amount
- term
- status
- createdAt
- updatedAt

El documento exige monto y plazo al solicitar el préstamo.

Estado:
PENDING
APPROVED
REJECTED

Podriamos agregar como algo adicional:
reviewedBy
reviewedAt
rejectionReason

Sin embargo esto no lo estan solicitando en el documento.

4. Reglas de negocio
- RN-01 — Creación de préstamo
  Una solicitud nueva debe quedar en estado: PENDING
  Esto es una inferencia funcional debido a lo que observo en la interfaz de ejemplo.
- RN-02 — Monto válido
  El préstamo debe tener un monto válido.
  El documento no define:
  Monto mínimo.
  Monto máximo.
  Moneda.
  Número de decimales permitido.
  
  Para el MVP deberá declararse una regla propia, por ejemplo: amount > 0
- RN-03 — Plazo válido
  El plazo es obligatorio, pero no se especifica su unidad.
  lo voy a representar como termMonths y seria de 1 a 360meses
- RN-04 — Propiedad de los préstamos
  El usuario solo debe consultar préstamos asociados con su identidad autenticada.
  No debería confiarse en un userId enviado libremente desde el frontend.
  El backend debería obtener al usuario desde el token JWT.
- RN-05 — Aprobación y rechazo
  Solo un administrador puede aprobar o rechazar préstamos. El requisito aparece tanto para Spring Security como para los Guards del frontend.
- RN-06 — Transición válida
  Solo un préstamo en estado PENDING debería pasar a:
  APPROVED.
  REJECTED.
  No debería permitirse:
  APPROVED → REJECTED
  REJECTED → APPROVED
- RN-07 — Operación transaccional
  La aprobación o rechazo debe ejecutarse dentro de una transacción para garantizar consistencia.
- RN-08 — Caché y deduplicación de consultas
  Las consultas repetitivas sobre el estado de los préstamos deberán utilizar mecanismos de caché para reducir la latencia y la carga sobre los sistemas de origen.
  Cuando varias solicitudes idénticas se ejecuten simultáneamente, el sistema podrá aplicar deduplicación de solicitudes para evitar procesar múltiples veces la misma consulta.
- RN-09 — Idempotencia de operaciones
  Las operaciones que modifiquen el estado de un préstamo deberán ser idempotentes.
  El reintento de una solicitud de aprobación o rechazo no deberá generar efectos adicionales, registros duplicados ni transiciones inconsistentes.

5. Casos de uso
- CU-01 — Autenticar usuario
Actor: Usuario o administrador.
Entrada:

Correo.
Contraseña.

Salida:

JWT.
Rol.
Datos básicos del usuario.

Errores:

Credenciales inválidas.
Usuario deshabilitado.
Formato incorrecto.

- CU-02 — Registrar usuario
Para este CRUD haremos Precargar:
Un usuario.
Un administrador.
Y dejar el CRUD de usuarios disponible solo para administradores. (Hay una alternativa mas completa para ello que seria hacer un enpoint publico de registro solo para USER, pero no creo que me alcance el tiempo).
Nunca permitir que el cliente se registre a sí mismo como ADMIN.

- CU-03 — Solicitar préstamo
Actor: Usuario autenticado.

Entrada:

Monto.
Plazo.

Proceso:

Validar usuario.
Validar monto.
Validar plazo.
Crear préstamo.
Asignar estado PENDING.
Persistir.
Devolver resultado.

Salida esperada:

201 Created

- CU-04 — Consultar mis préstamos
Actor: Usuario autenticado.

Proceso:

Obtener identidad desde JWT.
Consultar préstamos del usuario.
Utilizar caché si corresponde.
Devolver lista.

Salida:

200 OK

- CU-05 — Consultar solicitudes administrativas
Actor: Administrador.

Puede consultar:

Todas las solicitudes.
Solicitudes pendientes.
Solicitudes por estado.

Para el MVP, el filtro más importante es:

status=PENDING

- CU-06 — Aprobar préstamo
Actor: Administrador.

Precondición:

status = PENDING

Proceso:

Buscar préstamo.
Validar estado.
Cambiar a APPROVED.
Registrar fecha de revisión.
Persistir transaccionalmente.
Invalidar caché.
Devolver resultado.

- CU-07 — Rechazar préstamo
Es equivalente a aprobar, pero cambia el estado a:

REJECTED

El documento no exige motivo de rechazo.

- CU-08 — CRUD de usuarios
Debe existir porque el requisito lo solicita, pero no es el centro del ejemplo visual.

Operaciones posibles:

Crear usuario.
Consultar usuarios.
Consultar usuario por ID.
Actualizar usuario.
Deshabilitar usuario.

Para una aplicación financiera sería preferible deshabilitar antes que eliminar físicamente, aunque el documento no lo exige.

- CU-09 — CRUD de préstamos
El requisito menciona CRUD completo. Sin embargo, editar o eliminar préstamos puede entrar en conflicto con el flujo bancario.

Una interpretación responsable sería:

Crear solicitud.
Consultar solicitud.
Consultar listado.
Actualizar únicamente mediante transición administrativa.
Eliminar solo si está pendiente y pertenece al usuario, o no exponer eliminación pública.

Esta es una ambigüedad importante del documento.

6. Requisitos funcionales
- RF-01 El sistema debe autenticar usuarios y administradores
- RF-02 El sistema debe emitir un JWT al iniciar sesión
- RF-03 El usuario debe poder solicitar un préstamo
- RF-04 La solicitud debe incluir monto y plazo
- RF-05 Una solicitud nueva debe quedar pendiente
- RF-06 El usuario debe consultar sus propios préstamos
- RF-07 El administrador debe consultar solicitudes
- RF-08 El administrador debe aprobar préstamos pendientes
- RF-09 El administrador debe rechazar préstamos pendientes
- RF-10 Solo el administrador puede cambiar estados
- RF-11 Debe existir gestión CRUD de usuarios
- RF-12 Debe existir gestión CRUD de préstamos dentro de las reglas del dominio
- RF-13 El frontend debe validar formularios
- RF-14 El frontend debe proteger rutas mediante Guards
- RF-15 El frontend debe conservar el estado de autenticación
- RF-16 La API debe devolver errores HTTP claros