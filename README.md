<div align="center">

[![Java](https://img.shields.io/badge/Java-21-007396?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSJ3aGl0ZSIgc3Ryb2tlLXdpZHRoPSIyIiBzdHJva2UtbGluZWNhcD0icm91bmQiIHN0cm9rZS1saW5lam9pbj0icm91bmQiPjxwYXRoIGQ9Ik0xOCA4aDFhNCA0IDAgMCAxIDAgOGgtMSI+PC9wYXRoPjxwYXRoIGQ9Ik0yIDhoMTZ2OWE0IDQgMCAwIDEtNCA0SDZhNCA0IDAgMCAxLTQtNFoiPjwvcGF0aD48bGluZSB4MT0iNiIgeTE9IjEiIHgyPSI2IiB5Mj0iNCI+PC9saW5lPjxsaW5lIHgxPSIxMCIgeTE9IjEiIHgyPSIxMCIgeTI9IjQiPjwvbGluZT48bGluZSB4MT0iMTQiIHkxPSIxIiB4Mj0iMTQiIHkyPSI0Ij48L2xpbmU+PC9zdmc+&logoColor=white&labelColor=orange&color=grey)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.9.16-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white&labelColor=C71A36&color=grey)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white&labelColor=6DB33F&color=grey)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white&labelColor=4169E1&color=grey)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white&labelColor=2496ED&color=grey)](https://www.docker.com/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white&labelColor=000000&color=grey)](https://jwt.io/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black&labelColor=85EA2D&color=grey)](https://swagger.io/)
</div>

# Hotel Booking System - REST API

Backend-сервис для бронирования номеров в отеле. 

Pet-проект, сделанный с нуля для практики
backend-разработки на Java и Spring Boot: от проектирования модели данных до аутентификации,
документации API и контейнеризации.

## Что умеет сервис

- Регистрация и логин пользователей c JWT
- Разграничение прав по ролям: `USER` управляет только своими бронями, `ADMIN` - номерами 
отеля и всеми бронями
- CRUD для номеров отеля и пользователей
- Создание брони с проверкой доступности номера, нельзя забронировать номер на даты, которые
уже заняты
- Автоматический расчёт стоимости брони (количество ночей × цена за ночь)
- Отмена брони (только владельцем или администратором)
- Валидация входных данных и единообразная обработка ошибок с понятными HTTP-статусами
- Документация API через Swagger UI

## Стек

|  |                                                                  |
|---|----------------------------------------------------------------------------|
| Язык | Java 21                                                                    |
| Фреймворк | Spring Boot, Spring MVC                                                    |
| Данные | Spring Data JPA, Hibernate, PostgreSQL (H2 - на этапе локальной разработки) |
| Безопасность | Spring Security, JWT (jjwt)                                                |
| Документация | springdoc-openapi / Swagger UI                                             |
| Тесты | JUnit 5, Mockito, MockMvc                                                  |
| Инфраструктура | Docker, Maven                                               |

## Архитектура

Классическая слоистая архитектура:

- **Controller** принимает HTTP-запросы, ничего не знает о бизнес-логике
- **Service** занимается бизнес-логикой и правилами (проверка доступности номера, расчёт цены,
- права доступа)
- **Repository** отвечает за доступ к данным через Spring Data JPA
- Entity никогда не отдаются наружу напрямую: отдельные Request/Response объекты защищают
от утечки внутренних данных и от передачи клиентом полей, которые он
не должен контролировать (например, роли при регистрации)

## Модель данных


Бронь связывает пользователя и номер, хранит даты, статус и итоговую цену. 
Связи однонаправленные. `Booking` знает про `User` и `Room`, обратной ссылки нет 
для простоты и избежания проблем с рекурсивной сериализацией.

## Ключевые технические решения

**Проверка пересечения дат бронирования** реализована через JPQL-запрос, 
проверяющий условие пересечения интервалов 
(`checkIn < существующий checkOut AND checkOut > существующий checkIn`), исключая отменённые брони.

**JWT-аутентификация** - при логине выдаётся токен, который клиент передаёт в заголовке 
`Authorization: Bearer <token>`. Фильтр (`JwtAuthFilter`) проверяет токен 
на каждом запросе и восстанавливает контекст безопасности без хранения сессии на сервере.

**Защита от подмены пользователя** - `userId` при создании брони берётся не из тела запроса, 
а из аутентифицированного пользователя (`@AuthenticationPrincipal`), чтобы нельзя было забронировать
номер от чужого имени.

**Хэширование паролей** - через BCrypt, пароль никогда не хранится и не возвращается клиенту 
в открытом виде.

## Как запустить

### Вариант 1 - Docker Compose

```bash
git clone https://github.com/kalamburya/booking-system.git
cd booking-system
docker-compose up --build
```


### Вариант 2 - локально

Требуется Java 21 и PostgreSQL (или можно временно переключить в `application.properties` на H2).

```bash
git clone https://github.com/kalamburya/booking-system.git
cd booking-system
./mvnw spring-boot:run
```
#### Приложение будет доступно на http://localhost:8080.
## Документация API

После запуска доступна  документация Swagger UI:

http://localhost:8080/swagger-ui/index.html


Там можно посмотреть все эндпоинты и отправить тестовые запросы прямо из браузера. 
Для защищённых эндпоинтов нужно получить токен через `/api/auth/login` и вставить его
через кнопку `Authorize`.

## Основные эндпоинты

| Метод | Путь | Доступ             | Описание |
|---|---|--------------------|---|
| POST | `/api/auth/register` | Все                | Регистрация |
| POST | `/api/auth/login` | Все                | Логин, возвращает JWT |
| GET | `/api/rooms` | Все                | Список номеров |
| POST | `/api/rooms` | ADMIN              | Создать номер |
| PUT | `/api/rooms/{id}` | ADMIN              | Обновить номер |
| DELETE | `/api/rooms/{id}` | ADMIN              | Удалить номер |
| POST | `/api/bookings` | Авторизованные     | Создать бронь |
| GET | `/api/bookings/my` | Авторизованные     | Свои брони |
| GET | `/api/bookings/user/{id}` | ADMIN              | Брони конкретного пользователя |
| PATCH | `/api/bookings/{id}/cancel` | Владелец или ADMIN | Отменить бронь |
| POST | `/api/users` | ADMIN | Создать пользователя |
| GET | `/api/users` | ADMIN | Список всех пользователей |
| GET | `/api/users/{id}` | Владелец или ADMIN | Получить данные пользователя |
| PUT | `/api/users/{id}` | Владелец или ADMIN | Обновить пользователя |
| DELETE | `/api/users/{id}` | Владелец или ADMIN | Удалить пользователя |


## Тесты

```bash
./mvnw test
```

Покрыты:
- `BookingServiceTest`  ключевая бизнес-логика: валидация дат, проверка доступности номера, расчёт цены, права на отмену брони (unit-тесты с Mockito)
- `JwtServiceTest`  генерация и валидация JWT-токенов
- `RoomControllerTest`  проверка ролевого доступа на уровне HTTP-слоя (MockMvc)

## Что можно улучшить дальше

- Пагинация и фильтрация списков номеров/броней
- Подтверждение брони администратором (сейчас есть только создание и отмена, статусы `CONFIRMED`/`COMPLETED` не используются в логике)
- Более полные интеграционные тесты 

## Автор

Вадим Павлов — [GitHub](https://github.com/kalamburya)

## Лицензия
МIT