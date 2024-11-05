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

Teremos table de User, Book e de empréstimos de livros para usuário:

-insirir seedDataBase sql-

# Regra de negócio

Assim que o usuário pegar o livro, ele terá 7 dias para fazer a devolução ou a renovação (extender o prazo).

Se ele pegou o dia no dia 31 por exemplo, ele terá que devolver no dia 07 (sétimo dia).

Enviaremos então no dia 06 um email para todos os usuários que tem livros a serem devolvidos no dia seguinte.

Essa será a função do Job, notificar esses usuários!

# Steps Spring Batch

Reader: ler usuários que tem empréstimos próximo do retorno (numero de dias para retornar - 1 dia)

Processing: gerar mensagem do email (template)

Writing: enviar o email notificando o retorno