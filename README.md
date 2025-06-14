# SpringBootAPI

Spring Boot API с интеграцией OpenSearch. Реализовано тестовое задание с контейнеризацией, взаимодействием с OpenSearch и обработкой данных IBP.

## 🚀 Запуск

### 1. Клонирование репозитория


git clone https://github.com/yourname/TestProject.git
cd TestProject


### 2. Настройка переменных окружения

Создай файл `.env`:


OPENSEARCH_HOST=opensearch


(уже используется в `OpenSearchService`)

### 3. Запуск через Docker Compose


make up


Приложение поднимется на `http://localhost:8080`.

OpenSearch поднимется на `http://localhost:9200`.

Чтобы начать работу с OpenSearch вам нужно создать индекс под наши данные командой:
```bash
curl -X PUT "http://localhost:9200/ups-stats"
```
Затем вам нужно будет закинуть файл с данными, который я сделал с помощью конвертера (converter) 2 командами:
```bash
cd ./converter/
```
```bash
curl -X POST "http://localhost:9200/ups-stats/_bulk" \
  -H "Content-Type: application/x-ndjson" \
  --data-binary @bulk.ndjson
  ```
---

## 📡 Эндпоинты

| Метод | URL                   | Описание                               |
|-------|-----------------------|----------------------------------------|
| GET   | `/ibp/avg`            | Среднее значение OutputVoltage         |
| GET   | `/ibp/max`            | Максимальное значение RuntimeRemaining |
| GET   | `/ibp/uniquevalues`   | Уникальные значения поля Host          |

---

## 🛠 Используемые технологии

- Java 21
- Spring Boot 3.2.5
- OpenSearch REST Client
- Docker + docker-compose
- Makefile

---

## 📁 Структура проекта


src/
 └── main/
     └── java/com/test/
         ├── App.java
         ├── IBPController.java
         ├── IBPService.java
         ├── OpenSearchService.java
         └── IBP.java


---

