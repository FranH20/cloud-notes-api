# Cloud Notes API (AZ-204 / Azure App Service - Free Tier)

API REST mínima para aprender despliegue en **Azure App Service** (Linux) con **Java 21** y **Spring Boot 3.x**, empaquetada como **JAR** (no WAR). Incluye health check y un CRUD de notas **in-memory**.

## Requisitos

- Java 21

> Nota: el repo incluye **Maven Wrapper** (`./mvnw`), así que **no necesitas instalar Maven**.

## Ejecutar local

### 1) Tests

```bash
./mvnw clean test
```

### 2) Levantar la app

```bash
./mvnw spring-boot:run
```

Por defecto corre en `http://localhost:8080`. También puedes definir el puerto con env var `PORT` (útil para App Service):

```bash
PORT=8085 ./mvnw spring-boot:run
```

## Probar endpoints (curl)

### Health

```bash
curl -s http://localhost:8080/health
```

Respuesta esperada (ejemplo):

```json
{"status":"ok","time":"2026-02-06T00:00:00Z"}
```

### CRUD Notas (in-memory)

Crear nota:

```bash
curl -i -X POST http://localhost:8080/notes \
  -H 'Content-Type: application/json' \
  -d '{"title":"Primera nota","content":"Hola"}'
```

Listar:

```bash
curl -s http://localhost:8080/notes
```

Obtener por id:

```bash
curl -s http://localhost:8080/notes/<id>
```

Borrar:

```bash
curl -i -X DELETE http://localhost:8080/notes/<id>
```

### Validación

`title` es requerido y máximo 100 chars. Ejemplo que debe fallar:

```bash
curl -i -X POST http://localhost:8080/notes \
  -H 'Content-Type: application/json' \
  -d '{"title":"","content":"x"}'
```

## Deploy a Azure App Service (Linux, Free Tier) - paso a paso

> Este flujo asume que usarás **App Service** con runtime **Java 21** (si está disponible en tu suscripción/region) o el runtime Java más cercano. Alternativa: deploy con Docker.

### Opción A: Deploy como código (Zip Deploy)

1. Compila el JAR:

```bash
./mvnw clean package
```

2. El JAR queda en `target/cloud-notes-api-0.0.1-SNAPSHOT.jar`.

3. Crea recursos (Azure CLI):

```bash
az login
az group create -n rg-cloud-notes-api -l eastus
az appservice plan create -g rg-cloud-notes-api -n plan-cloud-notes-api --is-linux --sku FREE
az webapp create -g rg-cloud-notes-api -p plan-cloud-notes-api -n <NOMBRE_UNICO_APP> --runtime "JAVA:21-java21"
```

4. Configura `PORT` (App Service suele inyectarlo automáticamente, pero lo dejamos explícito):

```bash
az webapp config appsettings set -g rg-cloud-notes-api -n <NOMBRE_UNICO_APP> --settings PORT=8080
```

5. Zip deploy del artefacto (simple):

```bash
zip -j app.zip target/cloud-notes-api-0.0.1-SNAPSHOT.jar
az webapp deployment source config-zip -g rg-cloud-notes-api -n <NOMBRE_UNICO_APP> --src app.zip
```

6. Verifica:

```bash
curl -s https://<NOMBRE_UNICO_APP>.azurewebsites.net/health
```

> Nota: dependiendo del runtime, App Service puede requerir `startup command`. Si ocurre, usa la opción Docker o configura un startup command tipo: `java -jar /home/site/wwwroot/cloud-notes-api-0.0.1-SNAPSHOT.jar`.

### Opción B (recomendada si quieres control total): Deploy con Docker

Incluye un `Dockerfile` opcional (multi-stage) para asegurar runtime Java 21 consistente.

1. Build de imagen:

```bash
docker build -t cloud-notes-api:local .
```

2. Run local:

```bash
docker run --rm -p 8080:8080 -e PORT=8080 cloud-notes-api:local
```

3. Publica en ACR y despliega en App Service (resumen):

```bash
az acr create -g rg-cloud-notes-api -n <NOMBRE_UNICO_ACR> --sku Basic
az acr login -n <NOMBRE_UNICO_ACR>

docker tag cloud-notes-api:local <NOMBRE_UNICO_ACR>.azurecr.io/cloud-notes-api:1
docker push <NOMBRE_UNICO_ACR>.azurecr.io/cloud-notes-api:1

az webapp create -g rg-cloud-notes-api -p plan-cloud-notes-api -n <NOMBRE_UNICO_APP> -i <NOMBRE_UNICO_ACR>.azurecr.io/cloud-notes-api:1
```

## Estructura (capas)

- `health`: endpoint /health
- `notes.controller`: capa web (REST)
- `notes.service`: lógica in-memory
- `notes.model`: modelo interno
- `notes.dto`: DTOs de request/response
- `error`: excepciones y `@RestControllerAdvice`

## Notas

- No hay DB aún a propósito (solo in-memory).
- Logging con slf4j: se loguea creación y borrado de notas.
