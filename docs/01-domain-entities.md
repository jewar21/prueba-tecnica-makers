# Entidades del dominio

## User

| Campo      | Tipo     | Descripción                     |
|------------|----------|---------------------------------|
| `id`       | Long     | Identificador único             |
| `email`    | String   | Correo electrónico (único)      |
| `password` | String   | Contraseña cifrada              |
| `role`     | Enum     | `USER` o `ADMIN`                |
| `enabled`  | boolean  | Si la cuenta está activa        |
| `createdAt`| DateTime | Fecha de creación               |

### Reglas mínimas

- El correo debe ser obligatorio.
- El correo debe ser válido.
- El correo debe ser único.
- La contraseña no debe almacenarse en texto plano.
- El rol debe ser `USER` o `ADMIN`.
- Un usuario solo consulta sus préstamos.

---

## Loan

| Campo       | Tipo     | Descripción                     |
|-------------|----------|---------------------------------|
| `id`        | Long     | Identificador único             |
| `userId`    | Long     | Relación con el usuario         |
| `amount`    | BigDecimal | Monto del préstamo            |
| `termMonths`| Integer  | Plazo en meses (1-360)          |
| `status`    | Enum     | `PENDING`, `APPROVED`, `REJECTED` |
| `createdAt` | DateTime | Fecha de creación               |
| `updatedAt` | DateTime | Fecha de última actualización   |

### Posibles campos adicionales (no exigidos pero recomendables)

- `reviewedBy` — Quién administrador revisó la solicitud.
- `reviewedAt` — Cuándo se revisó.
- `rejectionReason` — Motivo de rechazo.

### Estados del préstamo

```
PENDING  ──►  APPROVED
  │
  └───────►  REJECTED
```

Solo se permite la transición `PENDING → APPROVED` o `PENDING → REJECTED`.
