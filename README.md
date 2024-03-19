# API de Pedidos

> Autor: Lucas Bortolatto da Conceição

## Tecnologias utilizadas

- [Spring Boot 3] - Framework para criação de aplicações Spring stand-alone.
- [JUnit 5] - Framework de testes para Java.


## Instalação e utilização

A API foi criada utilizando Java 17, então será necessária a utilização da [JDK 17] para a execução da mesma.
Além da JDK você também precisará ter o [Maven] configurado na máquina.
Após a configuração de ambas as ferramentas você pode instalar as dependências do projeto através da sua IDE preferida ou rodando os seguintes comandos dentro do diretório da API:

```sh
mvn install
```

Caso deseje rodar a suíte de testes automatizados via terminal (você também consegue rodar através da sua IDE preferida), pode seguir os comandos abaixo:

```sh
mvn test
```

Para rodar a API diretamente pelo Maven, sem utilizar uma IDE, rode o seguinte comando:

```sh
mvn spring-boot:run
```

As regras de negócio da API estão cobertas por seus respectivos testes nomeados.

### Banco de Dados
O banco de dados configurado em desenvolvimento é o [PostgreSQL 13] enquanto para a suíte de testes é utilizado o banco de dados em memória [H2].
Para que a aplicação em modo de desenvolvimento utilize adequadamente o banco de dados, você precisa ter uma instância do PostgreSQL rodando na porta 5432 e uma base de dados criada com o nome **sales_api**. Também deve possuir o usuário **postgres** com a senha **postgres** configurado. Para mais detalhes, consulte o arquivo **application.properties** ou **application-dev.properties**.

### Documentação técnica da API
Ao rodar a **API de Pedidos**, a mesma expõe uma rota _(/swagger-ui)_ para sua documentação técnica utilizando o [Swagger UI].
Exemplo de endereço local: http://localhost:8080/swagger-ui.html

[Spring Boot 3]: <https://spring.io/projects/spring-boot>
[JUnit 5]: <https://junit.org/junit5/>
[JDK 17]: <https://bell-sw.com/pages/downloads/#jdk-17-lts>
[Maven]: <https://maven.apache.org/>
[PostgreSQL 13]: <https://www.postgresql.org/download/>
[H2]: <https://www.h2database.com/html/main.html>
[Swagger UI]: <https://swagger.io/tools/swagger-ui/>