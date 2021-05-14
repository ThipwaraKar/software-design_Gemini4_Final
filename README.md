## Preparation

In case run on your machine , change configuration on application.properties

```java
# ==============================================================
# = Data Source
# ==============================================================

spring.datasource.url=jdbc:h2:file:{Your project path}/gemini4.db
spring.datasource.username = sa
spring.datasource.password =
```
(Optional)In case you want to register for new user, you have to set the role in database before login
go to http://localhost:8080/console
login url with jdbc:h2:file:{Your project path}/gemini4.db

1. Go to h2- database console then add role to ID of that user, for example you want to add A user with role ASTRONOMER 
```sql
INSERT INTO USER_ROLE VALUES (1, 2);
INSERT INTO USER_ROLE VALUES (2, 2);
```
## Gemini-4 system
our system consisting of 3 use cases
1. Create Science plan 
3. Validate science plan
4. Collect Astronomical data

Caution - You have to input any value correcly because our program is very sensitive, 
the webpage might show some error if the system received invalid type.

In collect astronomical data page, You have to input only exisiting sci plan ID

## Gemini-4 Design Pattern



