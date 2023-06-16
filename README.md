Дипломный проект
Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.
Приложение представляет собой веб-сервис, который предоставляет возможность купить тур по определённой цене с помощью двух способов:
Обычная оплата по дебетовой карте
Уникальная технология: выдача кредита по данным банковской карты
В процессе работы над данным проектом были созданы следующие документы:
План автоматизации
Отчет о проведенном тестировании
Отчет о проведённой автоматизации тестирования
Необходимое окружение:
установленный Docker;
убедитесь, что порты 8080, 9999 и 5432 или 3306 (в зависимости от выбранной базы данных) свободны;
Инструкции по установке:

1. Скачайте архив;

2. Запустите контейнер, в котором разворачивается база данных (далее БД) docker-compose up -d --force-recreate

3. Убедитесь в том, что БД готова к работе (логи смотреть через docker-compose logs -f <applicationName> (mysql или postgres)

4. Запустить SUT во вкладке Terminal Intellij IDEA командой 
для mysql: java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar
для для postgresql: java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar

5. Для запуска авто-тестов в Terminal Intellij IDEA открыть новую сессию и ввести команду 
для mysql: ./gradlew clean test allureReport "-Ddb.url=jdbc:mysql://localhost:3306/app"
для postgresql: ./gradlew clean test allureReport "-Ddb.url=jdbc:postgresql://localhost:5432/app"

6. Для просмотра отчета Allure в терминале ввести команду: ./gradlew allureServe

