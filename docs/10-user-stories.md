# Historias de Usuario

## Épica 1 — Autenticación y autorización

---

### HU-01 — Inicio de sesión

**Como** usuario registrado  
**quiero** iniciar sesión con correo y contraseña  
**para** acceder a las funciones correspondientes a mi rol.

#### Criterios de aceptación

```gherkin
Escenario: Credenciales válidas
  Dado que existe un usuario activo
  Cuando envía credenciales válidas
  Entonces el sistema devuelve un JWT
  Y devuelve el rol del usuario
  Y responde 200

Escenario: Credenciales inválidas
  Dado que las credenciales son inválidas
  Cuando intenta iniciar sesión
  Entonces el sistema responde 401
  Y no revela si falló el correo o la contraseña
```

---

### HU-02 — Protección de rutas

**Como** responsable de seguridad  
**quiero** restringir acciones según el rol  
**para** evitar operaciones no autorizadas.

#### Criterios de aceptación

```gherkin
Escenario: Usuario sin permisos
  Dado un usuario con rol USER
  Cuando intenta aprobar un préstamo
  Entonces recibe 403

Escenario: Administrador autorizado
  Dado un administrador autenticado
  Cuando accede a la gestión de préstamos
  Entonces puede consultar las solicitudes
```

---

## Épica 2 — Solicitudes de préstamo

---

### HU-03 — Solicitar préstamo

**Como** usuario autenticado  
**quiero** solicitar un préstamo indicando monto y plazo  
**para** iniciar su proceso de evaluación.

#### Criterios de aceptación

```gherkin
Escenario: Solicitud válida
  Dado un usuario autenticado
  Cuando envía un monto mayor que cero y un plazo válido
  Entonces se crea una solicitud en estado PENDING
  Y la solicitud queda asociada al usuario autenticado
  Y se responde 201

Escenario: Monto inválido
  Dado un monto inválido
  Cuando envía la solicitud
  Entonces se responde 400
  Y se informa claramente el campo inválido
```

---

### HU-04 — Consultar mis préstamos

**Como** usuario autenticado  
**quiero** consultar mis solicitudes  
**para** conocer su estado.

#### Criterios de aceptación

```gherkin
Escenario: Usuario con préstamos
  Dado un usuario autenticado
  Cuando consulta sus préstamos
  Entonces solo recibe préstamos asociados con su identidad

Escenario: Usuario sin préstamos
  Dado que no tiene préstamos
  Cuando realiza la consulta
  Entonces recibe una lista vacía
  Y la respuesta es 200
```

---

## Épica 3 — Gestión administrativa

---

### HU-05 — Consultar solicitudes pendientes

**Como** administrador  
**quiero** consultar solicitudes pendientes  
**para** revisarlas y tomar una decisión.

#### Criterios de aceptación

```gherkin
Escenario: Consulta de pendientes
  Dado un administrador autenticado
  Cuando consulta préstamos pendientes
  Entonces recibe las solicitudes cuyo estado es PENDING
```

---

### HU-06 — Aprobar préstamo

**Como** administrador  
**quiero** aprobar una solicitud pendiente  
**para** registrar una decisión positiva.

#### Criterios de aceptación

```gherkin
Escenario: Aprobación exitosa
  Dado un préstamo PENDING
  Cuando un administrador lo aprueba
  Entonces cambia a APPROVED
  Y el cambio se confirma transaccionalmente
  Y se invalida la caché asociada

Escenario: Préstamo ya resuelto
  Dado un préstamo ya resuelto
  Cuando intentan aprobarlo
  Entonces se responde 409
  Y su estado no cambia
```

---

### HU-07 — Rechazar préstamo

**Como** administrador  
**quiero** rechazar una solicitud pendiente  
**para** registrar una decisión negativa.

> **Nota:** Los criterios son equivalentes a la aprobación (HU-06), usando estado `REJECTED`.

---

## Épica 4 — Experiencia frontend

---

### HU-08 — Formulario reactivo de préstamo

**Como** usuario  
**quiero** ver validaciones en el formulario  
**para** corregir errores antes de enviarlo.

#### Criterios de aceptación

> Pendiente de definir.

---

### HU-09 — Protección del panel administrativo

**Como** administrador  
**quiero** acceder al panel administrativo según mi rol  
**para** gestionar solicitudes autorizadamente.

#### Criterios de aceptación

> Pendiente de definir.

---

### HU-10 — Conservación de sesión

**Como** usuario autenticado  
**quiero** que mi sesión se mantenga durante la navegación  
**para** no volver a iniciar sesión en cada pantalla.

#### Criterios de aceptación

> Pendiente de definir.

---

## Épica 5 — Calidad técnica

---

### HU-11 — Manejo uniforme de errores

**Como** consumidor de la API  
**quiero** errores consistentes  
**para** comprender y manejar los fallos.

#### Criterios de aceptación

> Pendiente de definir.

---

### HU-12 — Caché del estado de préstamos

**Como** sistema  
**quiero** almacenar temporalmente consultas frecuentes  
**para** reducir acceso repetitivo a la base de datos.

#### Criterios de aceptación

> Pendiente de definir.

---

### HU-13 — Pruebas críticas

**Como** equipo de desarrollo  
**quiero** pruebas automatizadas sobre los flujos críticos  
**para** prevenir regresiones.

#### Criterios de aceptación

> Pendiente de definir.
