#!/usr/bin/env bash

# Limpa o diretório de build anterior e empacota o projeto.
mvn clean package -Pdipol -DskipTests

# Constrói a imagem Docker do projeto a partir do Dockerfile com profile "dipol".
mvn dockerfile:build -Pdipol

# Submete a imagem Docker gerada para o repositório Registry com profile "dipol".
mvn dockerfile:push -Pdipol

# Executa o deploy da imagem Docker no Cluster Swarm com o namespace "SSP".
docker stack deploy --compose-file dipol-endereco-service_docker-compose.local.yml --with-registry-auth SSP

# Exemplos:

#- Construção da imagem
##docker build \
##    --build-arg BASE_IMAGE=registry.local.com.br/dipol-infra-microservice:8-jre-alpine \
##    --build-arg JAR_FILE=target/dipol-endereco-service-2.0.1.jar \
##    --tag registry.local.com.br/dipol-endereco-service:2.0.1 .

#- Criação do container
## docker run -d -p 3001:3001 \
##    -e SPRING_PROFILES_ACTIVE=docker \
##    -e JAVA_OPTS='-Xms128m -Xmx128m' \
##    -e KEYCLOAK_SERVER_URL=http://10.75.200.38:8081/auth \
##    -e KEYCLOAK_REALM=dipol \
##    -e KEYCLOAK_CLIENT_ID=dipol-microservices \
##    -e KEYCLOAK_CLIENT_SECRET=b1de7cc3-a17a-4994-b987-a7da9264b95e \
##    -e ELASTICSEARCH_CLUSTER_NAME=es-cluster \
##    -e ELASTICSEARCH_CLUSTER_NODES=10.75.200.38:9300 \
##    -e ELASTICSEARCH_REST_URIS=http://10.75.200.38:9200 \
##    registry.local.com.br/dipol-endereco-service:2.0.1
