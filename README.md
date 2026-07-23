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