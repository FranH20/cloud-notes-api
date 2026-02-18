# Bruno collection - Cloud Notes API

Ubicación: `tests/bruno/`

## Importar en Bruno
1. Abre Bruno.
2. **Import Collection**
3. Selecciona la carpeta `tests/bruno/`.
4. Selecciona uno de los environments:
   - `local` -> `http://localhost:8080`
   - `dev` -> `http://localhost:8000`
   - `prod` -> `https://newwebappudemy-eqbahgcegpgeavg4.brazilsouth-01.azurewebsites.net/`

## Orden sugerido
1. `GET /health (200)`
2. `POST /notes (201 - success)`
3. `GET /notes (200 - list)`
4. (Opcional) `GET /notes/{id} (200 - success)` -> pega el `id` devuelto por el POST.
5. (Opcional) `DELETE /notes/{id} (204 - success)` -> pega el `id`.

## Casos de validación
- `POST /notes (400 - validation missing title)` (violación `@NotBlank`)
- `POST /notes (400 - validation title > 100)` (violación `@Size(max=100)`)

## Notas
- La API se asume pública (sin auth).
- Los requests `GET/DELETE ... 404` prueban tu `NotFoundException` + `@RestControllerAdvice`.
