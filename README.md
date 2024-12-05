Структура проекта:

Модули проекта:
- Корневой каталог - платёжный сервис
- Task_05 - продуктовый сервис

Основной код:
- src/main/java/../dto - структуры запросов и ответов.
- src/main/java/../entities - структуры объектов.
- src/main/java/../repositories - DAO репозитории.
- src/main/java/../services - сервисы объектов.
- src/main/java/../controllers - контроллеры сервисов и обработка их исключений.

Ресурсы:
- src/main/resources/db/migration - sql скрипты создания БД модели для flyway.

Тесты:
- src/test/java/../repositories - тесты DAO репозиториев.
- src/test/java/../services - тесты сервисов.
- src/test/java/../controllers - тесты контроллеров.

Дополнительно:
- По платёжному сервису добавлено документирование
