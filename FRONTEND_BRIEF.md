# Brief para frontend de DofusChef

Este documento es para otra sesión de Claude Code que va a construir el frontend. Contiene todo lo necesario sobre el backend existente: no hace falta que exploren el repo de Java, esta info ya está completa y verificada contra el código fuente.

## Objetivo

Construir un frontend **súper simple** que consuma esta API REST. Prioriza lo mínimo funcional sobre features o pulido visual: sin frameworks pesados, sin build pipelines complejos, sin gestión de estado sofisticada. Recomendado: HTML + CSS + JavaScript vanilla con `fetch`, sin paso de build (o, si se prefiere algo con componentes, Vite + un framework ligero, pero solo si aporta simplicidad real). La decisión final de stack queda a criterio de quien implemente, pero que sea la opción más simple que resuelva el flujo de usuario descrito abajo.

## Backend: stack y arranque

- Spring Boot 3 / Java 21, corre en `http://localhost:8080`.
- **Todas las rutas van prefijadas con `/api/v1`** (`server.servlet.context-path=/api/v1`). Ejemplo: el login real es `http://localhost:8080/api/v1/auth/login`.
- CORS ya configurado para permitir origen `http://localhost:3000` con métodos `GET, POST, PUT, PATCH, DELETE, OPTIONS`, credenciales habilitadas, todos los headers permitidos. **El frontend debe correr en el puerto 3000** en desarrollo, o si no, pedir que se amplíe el CORS (`WebSecurityConfig.java`, bean `corsConfigurationSource`).
- Autenticación: JWT. Todas las rutas requieren `Authorization: Bearer <token>` excepto `/auth/**`.

## Dominio de la aplicación

DofusChef ayuda a trackear el progreso de crafteo de objetos del juego Dofus. Jerarquía:

```
User
 └── Kitchen (un "proyecto" de crafteo: título + descripción)
      └── Craft (un objeto concreto que se está crafteando, ligado a su Recipe)
           └── CraftLine (una línea de recurso de la receta, con progreso)
```

- Un **Kitchen** es un contenedor que el usuario crea para agrupar crafteos (ej. "Equipar a mi Iop").
- Un **Craft** se crea dando el `ankamaId` de un item (Equipment o Resource) que tenga receta. El backend expande automáticamente la receta en líneas.
- Cada **CraftLine** representa un recurso necesario: cantidad objetivo, cantidad actual, si está completa (se recalcula solo, nunca se envía desde el cliente) y un valor en kamas (costo, opcional, se actualiza manualmente).
- Aparte de ese flujo, existen catálogos de solo lectura de **Equipment** y **Resource** (los items del juego, con su receta embebida) para que el usuario pueda buscar qué `ankamaId` usar al crear un Craft.

## Formato de error (todas las rutas)

Todas las respuestas de error devuelven este shape:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Kitchen not found with id: 5",
  "timestamp": "2026-09-18T22:19:00.123"
}
```

Códigos relevantes: `400` (validación de body), `404` (no encontrado / no pertenece al usuario autenticado — mismo código en ambos casos, no revela si el recurso existe), `422` (el item no tiene receta), `500` (error inesperado).

## Autenticación

### `POST /auth/register`
Body:
```json
{ "username": "alice", "password": "secret123", "email": "alice@test.com" }
```
Response `200 OK` (ojo: incluso en error de negocio devuelve 200 con `success:false`, revisar el campo, no solo el status HTTP):
```json
{ "token": "eyJhbGciOi...", "success": true }
```
Si el username o email ya existen, o si falla, responde `400 Bad Request` con `{ "token": "Failed to register user with username: {user.getUsername()}", "success": false }` (nota: el mensaje de error literalmente no interpola el username, es un bug preexistente del backend, no hace falta arreglarlo desde el frontend, solo no confiar en ese texto).

### `POST /auth/login`
Body:
```json
{ "username": "alice", "password": "secret123" }
```
Response `200 OK`: `{ "token": "eyJhbGciOi...", "success": true }`. Si falla: `400 Bad Request` con `success: false`.

**Guardar el token** (ej. `localStorage`) y mandarlo en todas las llamadas siguientes como `Authorization: Bearer <token>`.

## Endpoints: Kitchen

Base: `/kitchens` (protegido, requiere JWT). Todas las operaciones están scopeadas al usuario autenticado — un usuario nunca ve kitchens de otro.

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| POST | `/kitchens` | `{"title": string, "description": string}` (title requerido, max 100; description opcional, max 2000) | `KitchenResponse` | 201 + header `Location` |
| GET | `/kitchens/{id}` | — | `KitchenResponse` | 200 / 404 |
| GET | `/kitchens` | — | `KitchenResponse[]` | 200 |
| PUT | `/kitchens/{id}` | `{"title": string, "description": string}` | `KitchenResponse` | 200 / 404 |
| DELETE | `/kitchens/{id}` | — | — | 204 / 404 (borra en cascada todos sus Crafts y CraftLines) |

`KitchenResponse`:
```json
{ "id": 1, "title": "Equipar a mi Iop", "description": "..." }
```

## Endpoints: Craft (anidado bajo Kitchen)

Base: `/kitchens/{kitchenId}/crafts`. Si `kitchenId` no existe o no es del usuario, 404 en cualquiera de estas rutas.

| Método | Path | Body | Response | Status |
|---|---|---|---|---|
| POST | `/kitchens/{kitchenId}/crafts` | `{"itemAnkamaId": number}` (el ankamaId de un Equipment o Resource con receta) | `CraftResponse` | 201 + `Location` / 404 (item no existe) / 422 (item sin receta) |
| GET | `/kitchens/{kitchenId}/crafts/{craftId}` | — | `CraftResponse` | 200 / 404 |
| GET | `/kitchens/{kitchenId}/crafts` | — | `CraftResponse[]` | 200 |
| DELETE | `/kitchens/{kitchenId}/crafts/{craftId}` | — | — | 204 / 404 |
| PATCH | `/kitchens/{kitchenId}/crafts/{craftId}/lines/{lineId}` | `{"currentQuantity": number \| null, "kamasValue": number \| null}` (ambos opcionales, mandar solo lo que cambia) | `CraftLineResponse` | 200 / 404 |

`CraftResponse`:
```json
{
  "id": 10,
  "itemAnkamaId": 200,
  "lines": [
    { "id": 1, "itemAnkamaId": 300, "targetQuantity": 5, "currentQuantity": 0, "completed": false, "kamasValue": 0 },
    { "id": 2, "itemAnkamaId": 301, "targetQuantity": 2, "currentQuantity": 0, "completed": false, "kamasValue": 0 }
  ]
}
```

`CraftLineResponse` (mismo shape que los items de `lines` arriba). **Importante**: `completed` se recalcula solo en el backend cuando cambia `currentQuantity` (`completed = currentQuantity >= targetQuantity`); nunca se manda `completed` en el PATCH, el backend lo ignoraría.

## Endpoints: catálogo de items (solo lectura, para elegir qué craftear)

Base: `/equipments` y `/resources`. Ambos devuelven una `Page` de Spring Data, shape:
```json
{
  "content": [ /* array de EquipmentResponse o ResourceResponse */ ],
  "totalElements": 123,
  "totalPages": 7,
  "number": 0,
  "size": 20
}
```

### Equipment
| Método | Path | Query params | Response |
|---|---|---|---|
| GET | `/equipments/id/{id}` | — | `EquipmentResponse` |
| GET | `/equipments/ankamaid/{id}` | — | `EquipmentResponse` |
| GET | `/equipments` | `name`, `type`, `isWeapon` (bool), `minLevel`, `maxLevel`, `page`, `size` — todos opcionales | `Page<EquipmentResponse>` |

`EquipmentResponse`:
```json
{
  "ankamaId": 200,
  "name": "Amakna Sword",
  "type": "Sword",
  "description": "...",
  "level": 10,
  "isWeapon": true,
  "images": { "icon": "url", "sd": "url", "hq": "url", "hd": "url" },
  "recipe": {
    "lines": [
      { "quantity": 5, "item": { "ankamaId": 300, "name": "...", "description": "...", "level": 1, "images": {...} } }
    ]
  }
}
```
`recipe` puede ser `null` si el item no es crafteable — en ese caso, crear un Craft con ese `ankamaId` devolverá 422.

### Resource
| Método | Path | Query params | Response |
|---|---|---|---|
| GET | `/resources/id/{id}` | — | `ResourceResponse` |
| GET | `/resources/ankamaid/{id}` | — | `ResourceResponse` |
| GET | `/resources` | `name`, `page`, `size` | `Page<ResourceResponse>` |

`ResourceResponse`: igual a `EquipmentResponse` pero sin `type`/`isWeapon`, con `pods: number` en su lugar.

## Flujo de usuario mínimo sugerido

1. Pantalla de login/registro → guarda el JWT.
2. Pantalla principal: lista de Kitchens del usuario, botón para crear uno nuevo (título + descripción).
3. Al entrar a un Kitchen: lista de sus Crafts, botón para crear uno nuevo. Para crear un Craft hace falta un `ankamaId` — lo más simple es un buscador contra `/equipments?name=...` o `/resources?name=...` que muestre nombre + ankamaId, y al elegir uno se llama `POST /kitchens/{id}/crafts`.
4. Al entrar a un Craft: lista de sus CraftLines con nombre del recurso (si se quiere mostrar el nombre en vez de solo el `itemAnkamaId`, hay que resolverlo contra `/equipments/ankamaid/{id}` o `/resources/ankamaid/{id}` — probar recurso primero, luego equipo, igual que hace el backend). Por cada línea: inputs para `currentQuantity` y `kamasValue`, que al cambiar disparan el PATCH. Mostrar visualmente si `completed` es true.
5. Botones de borrar Kitchen/Craft con confirmación (son destructivos y cascadean).

## Notas

- No hay endpoint de "logout" — simplemente borrar el token guardado en el cliente.
- No hay refresh token; si el JWT expira, la API devuelve 401/403 y hay que loguear de nuevo.
- Los ids de Kitchen/Craft/CraftLine son `Long` (números), el `ankamaId` de items es `int` — son namespaces distintos, no confundir un `id` de Craft con el `ankamaId` del item que representa.
