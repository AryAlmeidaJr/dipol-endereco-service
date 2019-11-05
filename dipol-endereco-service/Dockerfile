#============================================================================================
# FROM [nome da imagem]:[versão/tag da imagem]
# Referência: https://docs.docker.com/engine/reference/builder/#from
# 
# Define uma imagem local ou pública do Docker Store. Aqui é utilizado uma imagem customizada 
# e comum aos demais microsserviços da Secretaria de Segurança Pública do Estado de São Paulo, 
# tendo como base a imagem oficial do Java na versão 8. Em sua primeira execução, ela será 
# baixada para o computador e usada no build para criar as imagens da aplicação.
#============================================================================================
ARG BASE_IMAGE=registry.local.com.br/dipol-infra-microservice:8-jre-alpine
FROM ${BASE_IMAGE}

#============================================================================================
# LABEL maintainer=[nome e e-mail do mantenedor da imagem]
# Referência: https://docs.docker.com/engine/reference/builder/#label
#
# Indica o responsável/autor por manter a imagem.
#============================================================================================
LABEL maintainer="Raphael F. Jesus <rjesus@magnasistemas.com.br>"

#============================================================================================
# ARG <nome do argumento>[=<valor padrão>]
# Referência: https://docs.docker.com/engine/reference/builder/#arg
#
# A instrução ARG define uma variável que os usuários podem passar no tempo de compilação 
# para o construtor com o comando docker build.
#============================================================================================
ARG JAR_FILE

#============================================================================================
# ENV [nome da variável de ambiente]
# Referência: https://docs.docker.com/engine/reference/builder/#env
# 
# Variáveis de ambiente com o path da aplicação dentro do container.
#============================================================================================
ENV \
    DEBUG_OPTS= \
	EUREKA_CLIENT_ENABLED=false \
    JAVA_OPTS='-Xms128m -Xmx128m' \
	KEYCLOAK_CLIENT_ID=dipol-microservices \
	KEYCLOAK_CLIENT_SECRET='b1de7cc3-a17a-4994-b987-a7da9264b95e' \
	KEYCLOAK_REALM=dipol \
	KEYCLOAK_SERVER_URL=http://10.75.200.38:8081/auth \
    SPRING_PROFILES_ACTIVE=docker \
    DATABASE_URL=jdbc:postgresql://localhost:5432/gisdb \
    DATABASE_USERNAME=postgis \
    DATABASE_PASSWORD=root \
    

#============================================================================================
# COPY [arquivo a ser copiado] [destino do arquivo copiado]
# Referência: https://docs.docker.com/engine/reference/builder/#copy
#
# Copia o arquivo da aplicação, cuja nomenclatura é obtida a partir da variável ${JAR_FILE},
# para dentro do container sob o nome app.jar.
#============================================================================================
COPY ${JAR_FILE} /app.jar

#============================================================================================
# EXPOSE [número da porta]
# Referência: https://docs.docker.com/engine/reference/builder/#expose
#
# Irá expor a porta para a máquina host (hospedeira). É possível expor múltiplas portas, como 
# por exemplo: EXPOSE 80 443 8080
#============================================================================================
EXPOSE 3013

#============================================================================================
# ENTRYPOINT [executável seguido dos parâmetros]
# Referência: https://docs.docker.com/engine/reference/builder/#entrypoint
# 
# Inicia o container como um executável a partir da inicialização da aplicação. Essa instrução 
# é muito útil quando o container está em modo Swarm (Cluster de containers), pois caso a 
# aplicação caia, o container cai junto, indicando ao cluster aplicar a política de restart 
# configurada para a aplicação.
#============================================================================================
ENTRYPOINT exec java ${JAVA_OPTS} ${DEBUG_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar

#============================================================================================
# HEALTHCHECK --interval=[duração em segundos] --timeout=[duração em segundos]
# Referência: https://docs.docker.com/engine/reference/builder/#healthcheck
# 
# Diz ao Docker como testar um container para verificar se ele ainda está funcionando. Isso 
# pode detectar casos como um servidor web que está preso em um loop infinito e incapaz de 
# lidar com novas conexões, mesmo que o processo do servidor ainda esteja em execução.
#============================================================================================
HEALTHCHECK \
    --interval=10s \
    --timeout=30s \
    --start-period=120s \
    CMD curl -f http://localhost:3013/actuator/health || exit 1
