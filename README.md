# **Projeto de Integração Mensageria**

  

## **Descrição do Projeto**

  

Este projeto tem como objetivo integrar dados de uma API externa, utilizando **Spring Batch** para processar esses dados em forma de jobs, enviando-os para uma fila de mensagens no **RabbitMQ**. A aplicação também inclui a implementação de autenticação e autorização por **JWT** (JSON Web Token) com **Spring Security**, além de uma verificação de saúde da conexão com o RabbitMQ.

  

## **Tecnologias Utilizadas**

  

*  **Java 21**: Linguagem de programação utilizada no desenvolvimento da aplicação.

*  **Spring Boot**: Framework principal para construção da aplicação.

*  **Spring Security**: Usado para implementar autenticação e autorização baseada em JWT.

*  **Spring Batch**: Usado para processar dados em jobs de forma eficiente.

*  **RabbitMQ**: Broker de mensagens para comunicação entre componentes.

*  **Spring Cloud OpenFeign**: Cliente HTTP declarativo usado para comunicação com a API do IBGE.

*  **H2 Database**: Banco de dados em memória utilizado para persistência temporária durante o desenvolvimento e testes.

*  **Prometheus e Actuator**: Utilizados para monitoramento da aplicação e exposição de métricas.
  

## **Estrutura do Projeto**

### **1\. Autenticação e Segurança com JWT**

A aplicação utiliza JWT para autenticação e autorização dos usuários. A classe `JwtFilter` intercepta as requisições HTTP, extrai o token do cabeçalho, valida sua autenticidade e associa o contexto de segurança ao usuário.

*  **Classes Principais**:

*  `JwtFilter`: Filtro que verifica a validade do token JWT.

*  `JwtUtil`: Classe utilitária que lida com a geração e validação dos tokens.

*  `SecurityFilterConfig`: Configurações de segurança do Spring Security.

*  `DetalhesUsuarioService`: Serviço que carrega os detalhes do usuário para autenticação.

### **2\. Integração com API do IBGE**

O projeto faz uma chamada para a API de localidades do IBGE usando Feign Client. A classe `DistritosSpClient` faz a requisição dos distritos de São Paulo e retorna uma lista que será processada em um job Spring Batch.

*  **Classes Principais**:

*  `DistritosSpClient`: Cliente Feign para buscar dados da API do IBGE.
  

### **3\. Job Batch para Processamento dos Dados**

O job batch `BatchJobConfig` lê os distritos retornados pela API do IBGE, processa os dados e os envia para uma fila no RabbitMQ. A execução do job é feita automaticamente ao iniciar a aplicação.

  

* **Classes Principais**:

* `BatchJobConfig`: Configuração do job e dos steps de leitura, processamento e escrita dos dados.
* `JobStarterConfig`: Inicia o job automaticamente ao inicializar a aplicação.


### **4\. Fila de Mensagens com RabbitMQ**

A aplicação utiliza o RabbitMQ para envio de mensagens. A configuração do RabbitMQ é feita na classe `RabbitMQConfig` e sua saúde é monitorada pela classe `RabbitMQHealthIndicator`.

* **Classes Principais**:

* `RabbitMQConfig`: Configura a conexão com o RabbitMQ.
* `RabbitMQHealthIndicator`: Verifica a saúde da conexão com o RabbitMQ.

  
## **Configurações de Aplicação**

### **Arquivo `application.properties`**

  

As principais configurações estão definidas no arquivo de propriedades da aplicação.

  

**Configurações do JWT**:

properties


    teste.keykey=algumasecretachave
    jwt.expiracao=3600

**Configurações do H2 Database**:

properties


    spring.h2.console.enabled=true
    spring.datasource.url=jdbc:h2:mem:testdb

**Configurações do RabbitMQ**:

properties


    spring.rabbitmq.host=localhost
    spring.rabbitmq.port=5672
    spring.rabbitmq.username=guest
    spring.rabbitmq.password=guest


**Configurações de Prometheus para Monitoramento**:

properties

    management.endpoints.web.exposure.include=health,info,metrics,prometheus
    management.metrics.export.prometheus.enabled=true
    management.endpoint.prometheus.path=/actuator/prometheus

## **Como Executar o Projeto**


### **Requisitos**
* Java 21
* RabbitMQ rodando localmente (porta padrão 5672\)
* Maven para gerenciamento de dependências

  
### **Passos para Execução**
* Clone o repositório:
* bash

 Copiar código
`git clone https://github.com/seu-usuario/integracaomensageria.git`

  

### Navegue até o diretório do projeto:

bash

Copiar código
`cd integracaomensageria`

2. Compile e rode a aplicação:

bash

Copiar código

`mvn clean install`

`mvn spring-boot:run`

O Spring Batch será iniciado automaticamente e os dados serão enviados para a fila RabbitMQ.

### **Acesso ao H2 Console**

O banco de dados H2 está disponível em:
bash
Copiar código

`http://localhost:8080/h2-console`

Utilize as seguintes credenciais:

* **JDBC URL**: `jdbc:h2:mem:testdb`

* **User**: `sa`

* **Password**: (em branco)


### **Monitoramento com Prometheus**

As métricas Prometheus estão disponíveis em:

bash
Copiar código
`http://localhost:8080/actuator/prometheus`
