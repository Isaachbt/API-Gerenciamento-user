# API-Gerenciamento-user

API REST em **Java com Spring Boot** para gerenciar usuários (CRUD completo).

## 🔎 Sobre
API desenvolvida para criar, listar, atualizar e remover usuários via endpoints REST.  
Feita com Spring Boot, Maven, usando arquitetura de Controller → Service → Repository.

## 🚀 Tecnologias
- Java 17+  
- Spring Boot
- MySql
- Spring security
- Lombok
- jUnit

## 📥 Pré-requisitos
Antes de usar esta API, instale:
1. **Java 17 ou superior**
2. **Maven**
3. MySql

## 🔧 Como rodar
1. Clone o repositório:  
```
   git clone https://github.com/Isaachbt/API-Gerenciamento-user.git
```
2. Navegue ate o diretorio:
```
   cd API-Gerenciamento-user
```
4. Execute o aplicativo Spring Boot:
```
   mvn clean install
   mvn spring-boot:run
```
  Para maior facilidade e testes

```
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```
5. Configurações

```
spring.datasource.url=…
spring.datasource.username=…
spring.datasource.password=…
```

Disponivel em: http://localhost:8080.

## Necessario ter:
* MySql
* Postman/Insomnia
* Recomendado Intellij/Spring boot tools

## Funcionalidades

Esta API oferece as seguintes funcionalidades/endpoint:
* "/auth"

Cadastrar usuario.
```
http://localhost:8080/auth/creatUser
```
Login usuario.
```
http://localhost:8080/auth/login
```
Atualizar usuario.
```
http://localhost:8080/auth/update
```
Deletar usuario.
```
http://localhost:8080/auth/delete-user
```
Enviar codigo trocar senha
```
http://localhost:8080/auth/forgot-password
```
Trocar senha
```
http://localhost:8080/auth/reset-password
```
* "/user"

Perfil do usuario.
```
http://localhost:8080/user/profile
```

## Exemplo de requisição

```
POST http://localhost:8080/auth/creatUser
{
  "name": "",
  "cpf": "",
  "dataNascimento": "",
  "email": "",
  "password": ""
}
```


# Segurança
Este aplicativo implementa a segurança usando o Spring Security. As seguintes características de segurança estão em vigor:

- Autenticação de usuários.
- Autorização baseada em funções de usuário.
- Tokens com tempo.

- Acesso não autorizado: 403
- Não encontrado: 404
- Usuario ja tem conta: 409

# Contribuição
Sinta-se à vontade para contribuir com novos recursos, correções de bugs ou melhorias de desempenho. Basta enviar um pull request!

# Licença
Este projeto é licenciado sob a [MIT License](LICENSE).

