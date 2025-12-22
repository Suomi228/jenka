# Adminka Application

Spring Boot приложение на Java 17 с интеграцией Jenkins, Grafana и Prometheus.

## Технологии

- Java 17
- Spring Boot 3.2.0
- Spring Boot Actuator
- Micrometer Prometheus
- Swagger/OpenAPI 3 (SpringDoc)
- Docker & Docker Compose
- Jenkins
- Grafana
- Prometheus

## Структура проекта

```
adminka/
├── src/
│   └── main/
│       ├── java/com/example/adminka/
│       │   ├── controller/     # REST контроллеры
│       │   ├── service/        # Бизнес-логика
│       │   ├── model/          # Модели данных
│       │   └── dto/            # Data Transfer Objects
│       └── resources/
│           └── application.yml
├── prometheus/
│   └── prometheus.yml
├── grafana/
│   └── provisioning/
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## Запуск приложения

### Локальный запуск

```bash
mvn clean install
mvn spring-boot:run
```

Приложение будет доступно по адресу: http://localhost:8080

### Запуск через Docker Compose

```bash
docker-compose up -d
```

Сервисы будут доступны по следующим адресам:

- Приложение: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html (или http://localhost:8080/swagger-ui.html)
- API Docs (JSON): http://localhost:8080/api-docs
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/admin)
- Jenkins: http://localhost:8081

## API Endpoints

### Health Check

- `GET /api/health` - Проверка состояния приложения

### Users API

- `GET /api/users` - Получить всех пользователей
- `GET /api/users/{id}` - Получить пользователя по ID
- `POST /api/users` - Создать нового пользователя
- `PUT /api/users/{id}` - Обновить пользователя
- `DELETE /api/users/{id}` - Удалить пользователя

### Actuator Endpoints

- `GET /actuator/health` - Health check
- `GET /actuator/prometheus` - Метрики Prometheus
- `GET /actuator/metrics` - Список метрик

### Swagger/OpenAPI

- `GET /swagger-ui/index.html` - Swagger UI интерфейс для тестирования API
- `GET /swagger-ui.html` - Редирект на Swagger UI
- `GET /api-docs` - OpenAPI спецификация в формате JSON

## Примеры запросов

### Создание пользователя

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "description": "Test user"
  }'
```

### Получение всех пользователей

```bash
curl http://localhost:8080/api/users
```

## Swagger/OpenAPI Документация

### Доступ к Swagger UI

После запуска приложения откройте в браузере:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Swagger UI (альтернативный путь)**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### Использование Swagger UI

1. Откройте http://localhost:8080/swagger-ui/index.html
2. Вы увидите все доступные API endpoints, сгруппированные по контроллерам
3. Нажмите на любой endpoint, чтобы увидеть детали:
   - Параметры запроса
   - Формат тела запроса
   - Примеры ответов
4. Используйте кнопку **"Try it out"** для тестирования API прямо из браузера
5. Заполните необходимые поля и нажмите **"Execute"** для выполнения запроса

### Особенности

- Полная документация всех REST endpoints
- Интерактивное тестирование API без использования Postman или curl
- Автоматическая генерация примеров запросов и ответов
- Валидация данных перед отправкой запроса

## Мониторинг

### Prometheus

Метрики приложения доступны в Prometheus по адресу: http://localhost:9090

### Grafana

1. Откройте http://localhost:3000
2. Войдите с учетными данными: admin/admin
3. Prometheus уже настроен как источник данных
4. Создайте дашборды для мониторинга метрик приложения

## Jenkins CI/CD

Jenkins доступен по адресу: http://localhost:8081

При первом запуске Jenkins потребует пароль администратора. Получить его можно командой:

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

### Настройка Pipeline

1. **Установка плагинов** (Manage Jenkins → Plugins):

   - GitHub Integration Plugin
   - Pipeline
   - Git plugin

2. **Настройка Maven** (Manage Jenkins → Tools):

   - Maven installations → Add Maven
   - Name: `Maven`
   - Install automatically: выбрать версию 3.9.x

3. **Создание Pipeline Job**:
   - New Item → Pipeline
   - Имя: `adminka-pipeline`
   - В разделе "Build Triggers":
     - ✅ GitHub hook trigger for GITScm polling
     - ✅ Poll SCM (Schedule: `H/5 * * * *`)
   - В разделе "Pipeline":
     - Definition: `Pipeline script from SCM`
     - SCM: `Git`
     - Repository URL: `https://github.com/YOUR_USERNAME/adminka.git`
     - Branch: `*/main`
     - Script Path: `Jenkinsfile`

### Автоматический запуск при Push в GitHub

#### Вариант 1: Poll SCM (простой, работает локально)

Jenkins сам проверяет репозиторий каждые 5 минут. Уже настроено в Jenkinsfile.

#### Вариант 2: GitHub Webhook (требует публичный доступ к Jenkins)

1. В настройках репозитория GitHub:

   - Settings → Webhooks → Add webhook
   - Payload URL: `http://YOUR_JENKINS_URL/github-webhook/`
   - Content type: `application/json`
   - Events: `Just the push event`

2. Если Jenkins локальный, используйте ngrok:
   ```bash
   ngrok http 8081
   ```
   И укажите полученный URL в GitHub webhook.

## Сборка

```bash
mvn clean package
```

JAR файл будет создан в `target/adminka-1.0.0.jar`

## Остановка сервисов

```bash
docker-compose down
```

Для удаления всех данных (volumes):

```bash
docker-compose down -v
```
