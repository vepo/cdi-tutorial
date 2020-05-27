# Tutorial CDI

Usado para palestras

O conteúdo principal da palestra está em [CDI](cdi.adoc).

Antes de executar, é necessário haver uma base MongoDB rodando na mesma máquina.

```bash
docker run --rm --name mongo-db --env MONGO_INITDB_ROOT_USERNAME=root --env MONGO_INITDB_ROOT_PASSWORD=root -p 27017:27017 mongo:3.4
```

Para executar:

```
mvn clean thorntail:run
```