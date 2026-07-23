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