<h1 align="center"> [API sobre sistema de votos] </h1>

<br/>

<p align="justify">Microsserviço que cria pautas e cadastra votos, anunciando resultados em serviço de mensageria</p>

> Status do Projeto: Concluído ✔️

<br/>

> ### 📚 Dependências e libs utilizadas

<br/>
Utilize o seguinte comando para visualizar a lista de libs e dependências:
<br>

```
./gradlew dependency:list
```


<br/>

> ### 🚨 Pré Requisitos

É necessário instalar as seguintes tecnologias e ferramentas:
- Java 11
- Docker instalado 🐳 

<br/>

Faça o clone do projeto usando esse comando:

```
https://github.com/Seyfero/Pauta.git
```

<br/>

Além disso, recomendo a IDE Intellij para trabalhar no código, ela permite com facilidade a definição das variáveis de ambiente:

<br/>

> ### 🚨 Processos antes da execução do microsserviço

- Dentro da pasta docker-compose, executar script docker-compose.yml
- Após a **subida com sucesso** das imagens, executar o script.sql, do diretório:
  **/docker-compose/sql-script/**, para criação das tabelas. Banco utilizado Postgresql.

<br/>

> ### 🚨 Execução dos Testes

Para execução dos testes, ecolha um dos dois comandos:

```
./gradlew test jacocoTestReport
```

```
./gradlew test jacocoTestCoverageVerification
```

<br/>

> ### 🚨 Execução da aplicação

Para a execução da aplicação, usar o comando:

```
./gradlew run
```

<br/>

> ### Swagger
- A página html swagger gerada está disponível no seguinte endereço: http://localhost:8080/webjars/swagger-ui/index.html

<br/>


> ### 🚨 Informações Complementares - Redis

A autenticação necessária para acesso ao redis é password: order1234 

<br/>

> ### 🚨 Informações Complementares - Kafka

Tópico do kafka criado: order

<br/>

> ### 🚨 Informações Complementares - Funcionamento Básico

- Criar uma Pauta
- Criar um voto passando nome de uma pauta válida e um cpf válido(não tendo votado na pauta em questão)
- Os resultados serão enviados via Kafka, após a expiração da pauta

<br/>

> ### 🚨 Informações Complementares - Teste de performace

Foi executado um teste de performace, ferramenta utilizada Jmeter. Os resultados e teste
estão no diretório performace-test/

<br/>

> ### 💻 Contribuintes
- Saulo Cassiano de Carvalho