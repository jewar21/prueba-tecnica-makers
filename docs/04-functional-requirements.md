# Requisitos funcionales

| ID    | Descripción                                                     |
|-------|-----------------------------------------------------------------|
| RF-01 | El sistema debe autenticar usuarios y administradores.           |
| RF-02 | El sistema debe emitir un JWT al iniciar sesión.                 |
| RF-03 | El usuario debe poder solicitar un préstamo.                     |
| RF-04 | La solicitud debe incluir monto y plazo.                         |
| RF-05 | Una solicitud nueva debe quedar en estado `PENDING`.             |
| RF-06 | El usuario debe consultar sus propios préstamos.                 |
| RF-07 | El administrador debe consultar solicitudes.                     |
| RF-08 | El administrador debe aprobar préstamos pendientes.              |
| RF-09 | El administrador debe rechazar préstamos pendientes.             |
| RF-10 | Solo el administrador puede cambiar estados.                    |
| RF-11 | Debe existir gestión CRUD de usuarios.                          |
| RF-12 | Debe existir gestión CRUD de préstamos dentro de las reglas del dominio. |
| RF-13 | El frontend debe validar formularios.                           |
| RF-14 | El frontend debe proteger rutas mediante Guards.                |
| RF-15 | El frontend debe conservar el estado de autenticación.          |
| RF-16 | La API debe devolver errores HTTP claros.                       |
