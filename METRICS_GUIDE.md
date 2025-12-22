# Руководство по просмотру метрик

## 1. Прямой доступ к метрикам приложения

### Prometheus метрики (формат Prometheus)
```
http://localhost:8080/actuator/prometheus
```

### Список всех доступных метрик
```
http://localhost:8080/actuator/metrics
```

### Детали конкретной метрики
```
http://localhost:8080/actuator/metrics/jvm_memory_used_bytes
http://localhost:8080/actuator/metrics/http_server_requests_seconds_count
```

### Health check
```
http://localhost:8080/actuator/health
```

## 2. Prometheus UI

1. Откройте браузер: **http://localhost:9090**
2. В строке запроса введите метрику, например:
   - `jvm_memory_used_bytes` - использование памяти JVM
   - `http_server_requests_seconds_count` - количество HTTP запросов
   - `jvm_threads_live_threads` - количество потоков
   - `system_cpu_usage` - использование CPU
3. Нажмите **Execute** для просмотра графика
4. Переключитесь на вкладку **Graph** для визуализации

### Примеры запросов в Prometheus:

**Скорость HTTP запросов:**
```
rate(http_server_requests_seconds_count[5m])
```

**Использование памяти heap:**
```
jvm_memory_used_bytes{area="heap"}
```

**Среднее время ответа:**
```
rate(http_server_requests_seconds_sum[5m]) / rate(http_server_requests_seconds_count[5m])
```

## 3. Grafana (Рекомендуется)

1. Откройте браузер: **http://localhost:3000**
2. Войдите: **admin** / **admin**
3. Prometheus уже настроен как источник данных
4. Создайте новый дашборд:
   - Нажмите **+** → **Create Dashboard**
   - Добавьте панель (Panel)
   - Выберите источник данных: **Prometheus**
   - Введите запрос, например: `jvm_memory_used_bytes`

### Полезные метрики для мониторинга:

#### HTTP метрики:
- `http_server_requests_seconds_count` - общее количество запросов
- `http_server_requests_seconds_sum` - суммарное время обработки
- `rate(http_server_requests_seconds_count[5m])` - скорость запросов

#### JVM метрики:
- `jvm_memory_used_bytes` - используемая память
- `jvm_memory_max_bytes` - максимальная память
- `jvm_threads_live_threads` - количество потоков
- `jvm_gc_pause_seconds` - время пауз сборщика мусора

#### Системные метрики:
- `system_cpu_usage` - использование CPU
- `process_cpu_usage` - использование CPU процессом
- `disk_free_bytes` - свободное место на диске

## 4. Проверка через PowerShell

```powershell
# Получить все метрики
Invoke-RestMethod -Uri http://localhost:8080/actuator/prometheus

# Получить список метрик
Invoke-RestMethod -Uri http://localhost:8080/actuator/metrics

# Получить конкретную метрику
Invoke-RestMethod -Uri http://localhost:8080/actuator/metrics/jvm_memory_used_bytes

# Health check
Invoke-RestMethod -Uri http://localhost:8080/actuator/health
```

## 5. Проверка через браузер

Просто откройте в браузере:
- http://localhost:8080/actuator/prometheus
- http://localhost:8080/actuator/metrics
- http://localhost:8080/actuator/health


