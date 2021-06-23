
# Exchange Change Currency Gif

Сервис, который обращается к сервису курсов валют, и отдает gif в ответ:
    * если курс по отношению к рублю за сегодня стал выше вчерашнего, случайную отсюда https://giphy.com/search/rich
    * если ниже - отсюда https://giphy.com/search/broke


Использование
=====
Сервис отвечает на запрос по следующему адресу 
```
http://localhost:8080/api/gif/{currency}
```

В качестве {currency} необходимо указать обозначение необходимой валюты:

RUB - Russian Ruble

GBP - Great Britain Pound

USD - United States Dollar

...

Пример `http://localhost:8080/api/get-currency-gif/EUR`

Пример ответа сервера:

```
{"id":"10tD7GP9lHfaPC","url":"https://giphy.com/gifs/man-okay-happens-10tD7GP9lHfaPC"}
``` 

Запуск
=====
Для построения и запуска приложения на локальной машине необходимы Git, Gradle, Jdk (не ниже 11)

Выполнение из командной строки.

```
# Выполнить команду для клонирования репозитория github
$ git clone https://github.com/jonneg12/exchange-changing-gif

# Выполнить команду для создания исполняемого файла
$ ./gradlew build

# Выполнить команду для запуска приложения 
$ java -jar build/libs/exchange-changing-gif-service-v1.jar
```

Инструкция для запуска docker

```
# Скачать репозиторий гит
$ git clone https://github.com/jonneg12/exchange-changing-gif

# Выполнить команду для создания исполняемого файла
$ ./gradlew build

# Выполнить команду для создания docker - образа
$ docker build -t exchange-changing-gif-service .

# Выполнить команду для запуска docker - контейнера
$ docker run -p 8080:8080 exchange-changing-gif-service

```