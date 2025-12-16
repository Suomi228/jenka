# Adminka Application

Spring Boot приложение на Java 17 с интеграцией Jenkins, Grafana и Prometheus.

## Технологии

- Java 17
- Spring Boot 3.2.0
- Spring Boot Actuator
- Micrometer Prometheus
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

