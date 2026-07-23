# Reglas de negocio

| ID     | Descripción                                                                                              |
|--------|----------------------------------------------------------------------------------------------------------|
| RN-01  | **Creación de préstamo.** Una solicitud nueva debe quedar en estado `PENDING`.                           |
| RN-02  | **Monto válido.** El préstamo debe tener un monto válido. Para el MVP se define `amount > 0`.            |
| RN-03  | **Plazo válido.** El plazo es obligatorio. Se representa como `termMonths` con rango de 1 a 360 meses.   |
| RN-04  | **Propiedad de los préstamos.** El usuario solo consulta préstamos asociados a su identidad (obtenida del JWT, no de parámetros del frontend). |
| RN-05  | **Aprobación y rechazo.** Solo un administrador puede aprobar o rechazar préstamos.                      |
| RN-06  | **Transición válida.** Solo un préstamo `PENDING` puede pasar a `APPROVED` o `REJECTED`. No se permite revertir. |
| RN-07  | **Operación transaccional.** La aprobación o rechazo debe ejecutarse dentro de una transacción.          |
| RN-08  | **Caché y deduplicación.** Las consultas repetitivas de estado deben usar caché. Se puede aplicar deduplicación de solicitudes simultáneas. |
| RN-09  | **Idempotencia.** Las operaciones que modifiquen el estado de un préstamo deben ser idempotentes.        |
