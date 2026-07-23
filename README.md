# prueba-tecnica-makers

Tenemos un flujo principal del reto:
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

Que actores tiene este spftware?
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