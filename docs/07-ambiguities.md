# Ambigüedades detectadas

A lo largo del análisis del documento se identificaron varios puntos no especificados o ambiguos. A continuación se listan junto con la decisión tomada para el MVP.

| Ambigüedad                        | Decisión MVP                          |
|-----------------------------------|---------------------------------------|
| No define registro público        | Precargar usuario y admin con seed data |
| No define unidad del plazo        | Usar meses como unidad                |
| No define monto mínimo/máximo     | Validar únicamente `amount > 0`       |
| No define moneda                  | Usar COP (peso colombiano)            |
| No define motivo de rechazo       | Campo opcional                        |
| No define si se puede editar un préstamo | No permitir modificar tras enviarlo |
| No define eliminación             | Evitar eliminación física; deshabilitar |
| No define expiración JWT          | Configurable por variable de entorno  |
| No define refresh token           | No implementarlo en el MVP            |
| No define paginación              | No implementar en el MVP              |
| No define auditoría               | Registrar `reviewedAt` y `reviewedBy` si el tiempo lo permite |
| Pide JPA y menciona WebFlux       | Usar MVC + JPA y justificar la decisión |
| Sugiere servicios o NgRx          | Usar servicios; NgRx sería excesivo para el alcance |
| Recomienda Ehcache u otra caché   | Usar Caffeine (más liviano); Ehcache podría complicar innecesariamente |
