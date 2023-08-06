# План автоматизации тестирования
## Перечень автоматизируемых сценариев:
### Исходные данные:
- валидный номер карты - 4444 4444 4444 4441;
- не валидный номер карты - 4444 4444 4444 4442.

### Позитивные тестовые сценарии:
1. **Оплата картой:**
    **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в открывшемся окне в доступных полях ввести валидные данные:
    - номер карты "4444 4444 4444 4441"; 
    - месяц не ниже текущего, год не ниже текущего;
    - фамилия, имя владельца  в формате фамилия имя через пробел на английском языке;
    - CVC/CVV в формате трезначного числа;
   
   => После ввода всех данных кликнуть кнопку "Продолжить" 
   
    `Результат:`
    
    - данные успешно отправлены и доступны для просмотра в базе данных;
    - выходит всплывающее окно, о том, что операция одобрена банком.
2. **Оплата в кредит:**
    **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить в кредит" => в открывшемся окне в доступных полях вести валидные номер карты, месяц, год, владелец и CVC/CVV по аналогии с п. 1 => кликнуть кнопку "Продолжить";  
      `Результат:`
      
    - данные успешно отправлены и доступны для просмотра в базе данных;
    - выходит всплывающее окно, о том, что операция одобрена банком.
### Негативные тестовые сценарии:
1. **Незаполненная форма:**
    **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => кликнуть "Продолжить";  
      `Результат:`
      
      - данные не отправлены;
      - должно появиться сообщение `Неверный формат` под полем "Номер карты";
      - должно появиться сообщение `Неверный формат`под полем "Месяц";
      - должно появиться сообщение `Неверный формат`под полем "Год";
      - должно появиться сообщение `Поле обязательно для заполнения`под полем "Владелец";
      - доолжно появиться сообщение `Неверный формат`под полем "CVC/CVV".
2. **Незаполненное поле "Номер карты"/неверный ввод данных в поле "Номер карты":**
      2.1. **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Номер карты" **ввести не валидный номер карты** (4444 4444 4444 4442), остальные поля заполнить валидными данными => кликнуть  "Продолжить";  
         `Результат:`
       
        - данные успешно отправлены и могут быть просмотрены в соответствующей базе данных
        - появится всплывающее окно об отказе в проведении операции банком.
        

    2.2. **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => поле "Номер карты" **оставить пустым**, остальные поля заполнить валидными данными => кликнуть"Продолжить";  
      `Результат:`
      
        - данные не отправлены;
        - должно появиться сообщение `Неверный формат` под полем "Номер карты".
    2.3. **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Номер карты" **ввести не полный номер** (4444 4444 4444 444), остальные поля заполнить валидными данными => кликнуть "Продолжить";
    
    `Результат:`
       
      - данные не отправлены;
      - должно появиться сообщение `Неверный формат` под полем "Номер карты".
   
    
3. **Поле "Месяц":**
    - **Открыть** [страницу](https://localhost:8080/) -> кликнуть "Купить" (оплата картой) => поле "Месяц" **оставить пустым**, остальные поля заполнить валидными данными => кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Месяц" появится сообщение `Неверный формат`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Месяц" **ввести не валидный месяц** (текущий месяц -1, и текущий год), остальные поля заполнить валидными данными => кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Месяц" появится сообщение `Неверно указан срок действия карты`.
    - **Открыть** [страницу покупки тура](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Месяц" **ввести не валидный месяц** (нижнее граничное значение 00), остальные поля заполнить валидными данными => кликнуть кнопку "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Месяц" появится сообщение `Неверно указан срок действия карты`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Месяц" **ввести не валидный месяц** (верхнее граничное 13), остальные поля заполнить валидными данными => кликнуть кнопку "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Месяц" появится сообщение `Неверно указан срок действия карты`.
4. **Поле "Год":**
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => поле "Год" **оставить пустым**, остальные поля заполнить валидными данными => кликнуть кнопку "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Год" появится сообщение `Неверный формат`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Год" **ввести не валидный год** (текущий год - 1), остальные поля заполнить валидными данными => кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Год" появится сообщение `Истёк срок действия карты`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Год" **ввести не валидный год** (текущий год + 6), остальные поля заполнить валидными данными -> кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Год" появится сообщение `Неверно указан срок действия карты`.
5. **Поле "Владелец":**
    - **Открыть** [страницу покупки тура](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => поле "Владелец" **оставить пустым**, остальные поля заполнить валидными данными => кликнуть  "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Владелец" появится сообщение `Поле обязательно для заполнения`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Владелец" **ввести " "**(пробел), остальные поля заполнить валидными данными => кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Владелец" появится сообщение `Поле обязательно для заполнения`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => в поле "Владелец" **ввести #&**(два любых спецсимвола), остальные поля заполнить валидными данными => кликнуть  "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Владелец" появится сообщение `Неверный формат имени владельца`.
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) -> в поле "Владелец" **ввести двузначное число**(10-99), остальные поля заполнить валидными данными => кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "Владелец" появится сообщение `Неверный формат имени владельца`.
6. **Поле "CVC/CVV":**
    - **Открыть** [страницу](https://localhost:8080/) => кликнуть "Купить" (оплата картой) => поле "CVC/CVV" **оставить пустым**, остальные поля заполнить валидными данными -> кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "CVC/CVV" появится сообщение `Неверный формат`.
    - **Открыть** [страницу](https://localhost:8080/) -> кликнуть "Купить" (оплата картой) -> в поле "CVC/CVV" **ввести двузначное число**(10-99), остальные поля заполнить валидными данными -> кликнуть "Продолжить";  
      `Результат:`
        - данные не отправлены;
        - под полем "CVC/CVV" появится сообщение `Неверный формат`.
        
    **Указанные негативные сценарии повторяем и для покупки в кредит**
## Перечень используемых инструментов с обоснованием выбора:
- IntelliJ IDEA JDK 11, так автотесты будут написаны на Java;
- JUnit Jupiter 5, основа для тестов;
- Gradle, инструмент управления зависимостями;
- DBeaver, для работы с базами данных;
- Selenide, для работы с браузером при написании автотестов;
- MySQL connector Java, PostgreSQL и Commons DBUtils, для доступа к базе данным из кода авто-тестов;
- Project Lombok, для упрощения написания кода;
- Faker, для генерации данных при заполнении форм;
- GitHub, в качестве удаленного репозитория;
- Allure, для создания отчетов о выполнении авто-тестов;

## Перечень и описание возможных рисков при автоматизации:
- Возможные проблемы при настройке SUT при запуске, т.к. заявлена поддержка двух СУБД;
- Отсутствие спецификации на приложение;
- Необходимость добавления новых тестов для заказчика, при отсутствии документации и спецификации на приложение;
- Сложность поодержки автотестов, так как изменение веб-элементов приведет к падению авто-тестов;
- Не проверяется графическая составляющая, верстка, цветовая схема оформления и прочее.
## Интервальная оценка с учётом рисков в часах:
- 94 ч.
## План сдачи работ:
1. Написание автотестов до 30 мая 2023;
2. Предоставление отчетов до 6 июня 2023:
- Подготовка к проведению тестирования (настройка и запуск SUT, подготовка плана автоматизации) - 12 часов;
- Написание и запуск авто-тестов - 48 часов;
- Оформление отчётов о дефектах - 12 часов;
- Оформление отчёта по итогам тестирования - 10 часов;
- Оформление отчёта по итогам автоматизации - 12 часов.