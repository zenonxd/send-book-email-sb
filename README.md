<p align="center">
  <img src="https://img.shields.io/static/v1?label=Spring Essential - Dev Superior&message=Envio Email Spring Batch&color=8257E5&labelColor=000000" alt="estudo envio de email spring batch" />
</p>

# Objetivo

Um projeto onde desenvolveremos um job responsável por fazer envio de emails automático usando Spring Batch.

Um dos usos mais comuns do Spring Batch é o envio de emails em massa.

Usaremos:

SendGrid (envio de email transacional).

[Github Projeto](https://github.com/devsuperior/send-book-email-spring-batch)

# Modelo de Dados usado

Usaremos o exemplo de uma livraria.

Teremos table de User, Book e de empréstimos de livros para usuário.

# Regra de negócio

Assim que o usuário pegar o livro, ele terá 7 dias para fazer a devolução ou a renovação (extender o prazo).

Se ele pegou o dia no dia 31 por exemplo, ele terá que devolver no dia 07 (sétimo dia).

Enviaremos então no dia 06 um email para todos os usuários que tem livros a serem devolvidos no dia seguinte.

Essa será a função do Job, notificar esses usuários!

# Steps Spring Batch

Reader: ler usuários que tem empréstimos próximo do retorno (numero de dias para retornar - 1 dia)

Processing: gerar mensagem do email (template)

Writing: enviar o email notificando o retorno

# Resources

[Clique aqui](https://github.com/devsuperior/send-book-email-spring-batch?tab=readme-ov-file#resources)

# Criação Banco de Dados

Inserir o ``DockerCompose.yaml`` na pasta para criar o ambiente.

Ele conterá o MySQL e o phpMyAdmin.

Crie os bancos necessários:

1. Banco para salvar metadados Spring Batch, chamará: **spring_batch**
2. Banco para salvar as tabelas criadas (user, book, user_book_loan), chamará: **library**

Use o script do import.sql para dar o creat no phpMyAdmin.

![img.png](img.png)

Depois, insira os dados. ❗Importante, no user coloque o email que você irá utilizar.

Além disso, se atente as datas do spring, visto que esse projeto foi elaborado em fevereiro de 2023.

![img_1.png](img_1.png)

# Criando classes domínio

Crie uma classe ``import.sql`` e jogue o seed todo lá para deixar salvo e documentado.

## User

```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {

    private int id;
    private String name;
    private String email;

}
```

## Book

```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Book {
    
    private Integer id;
    private String name;
    private String description;
    private String author;
    private String category;

}
```

## UserBook Loan

```java
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserBookLoan {

    private User user;
    private Book book;
    private Date loanDate;
}
```

# Configuração DataSources

Recapitulando.

Teremos a nossa aplicação que alocará os metadados para o banco de dados spring_batch (criado no phpMyAdmin).

Agora, os dados advindos do Job iremos alocar no banco library.

## application.properties

![img_2.png](img_2.png)

## Classe DataSourceConfig

![img_3.png](img_3.png)




# Configurando Job e Step

Recapitulando: queremos criar um Job responsável por fazer enviar automaticamente usando o Spring Batch.

Ele notificará sobre empréstimo determinado usuário.

## Job

Os nomes precisam ser autoexplicativos, lembra?

```java
@Configuration
public class SendBookLoanNotificationJobConfig {

    @Bean
    public Job SendBookLoanNotificationJob(Step sendEmailUserStep, JobRepository jobRepository) {
        return new JobBuilder("SendBookLoanNotificationJob", jobRepository)
                .start(sendEmailUserStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
```

## Step

O que o step deve fazer? Enviar o email para o usuário.

❗Este código irá mudar.

Criaremos o PlatformTransactionManager, passando o Qualifier do DataSourceConfig.

O chunk receberá um UserBookLoan e ❗❗POR ENQUANTO retornará o mesmo, demais será um email.

E já começamos passando o ItemReader, também do tipo UserBookLoan.

```java
@Configuration
public class sendEmailUserStepConfig {

    private int chunkSize;

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step sendEmailUserStep(ItemReader<UserBookLoan> readUsersWithLoansCloseToReturnReader,
            JobRepository jobRepository) {
        return new StepBuilder("sendEmailUserStep", jobRepository)
                .<UserBookLoan, UserBookLoan>chunk(1, platformTransactionManager)
                .reader(readUsersWithLoansCloseToReturnReader)
                .build();
    }
}
```

### ItemReader

Como vimos ali em cima, o ItemReader deve:

Ler usuários que tem empréstimos próximo do retorno (numero de dias para retornar - 1 dia)

O nome será: ``readUsersWithLoansCloseToReturnReaderConfig``

Inicialmente, criaremos um método para a classe que irá receber como parâmetro o data source. Usaremos o banco de dados
library. Este método, retornará um ItemReader do tipo UserBookLoan.

Quando gostaríamos de obter dados do banco de dados, existe uma classe que extende o ItemReader, ela se chama: 
``JdbcCursorItemReaderBuilder``.

```java
```

#### rowMapper()

Vamos utilizar ele para obter os dados e atributos (através da consulta SQL), e passaremos ele para nossos objetos que 
criamos no nosso domínio (User, Book, UserBookLoan).

Criamos o método e passamos nele: ``return new RowMapper<UserBookLoan>()``. Clicamos e implementamos o método ``mapRow``.

MapRow = correspondência da linha.

Ou seja, leremos linha a linha e obter os valores dos atributos correspondentes. 

![img_4.png](img_4.png)

Para obter esses campos com mais facilidade, defina um ALIAS na consulta SQL, veja:

![img_5.png](img_5.png)

#### Método

```java
    private RowMapper<UserBookLoan> rowMapper() {
    //criando esse metodo, ele ira pedir para implementar
    //o mapRow abaixo
        return new RowMapper<UserBookLoan>() {

            @Override
            public UserBookLoan mapRow(ResultSet rs, int rowNum) throws SQLException {
                //pegando id, name e email
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_email")
                );

                Book book = new Book();
                book.setId(rs.getInt("book_id"));
                book.setName(rs.getString("book_name"));

                UserBookLoan userBookLoan = new UserBookLoan(user, book, rs.getDate("loan_date"));

                return userBookLoan;
            }
        };
    }
```

