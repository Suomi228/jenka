pipeline {
    agent any
    
    triggers {
        pollSCM('H/2 * * * *')
    }
    
    environment {
        APP_NAME = 'adminka-app'
    }
    
    stages {
        stage('Setup') {
            steps {
                echo '⚙️ Проверка окружения...'
                sh 'java -version || echo "Java not found"'
                sh 'docker --version || echo "Docker not found"'
                sh 'docker compose version || echo "Docker Compose not found"'
                sh 'chmod +x ./mvnw || true'
            }
        }
        
        stage('Build JAR') {
            steps {
                echo '🔨 Сборка проекта...'
                sh './mvnw clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                echo '🧪 Запуск тестов...'
                sh './mvnw test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Deploy') {
            steps {
                echo '🚀 Деплой приложения...'
                
                // Остановка старого контейнера app
                sh '''
                    echo "Stopping old app container..."
                    docker compose stop app || true
                    docker compose rm -f app || true
                '''
                
                // Пересборка и запуск только сервиса app (без зависимостей)
                sh '''
                    echo "Building and starting app..."
                    docker compose up -d --build --force-recreate --no-deps app
                '''
                
                // Удаление старых неиспользуемых образов
                sh '''
                    echo "Cleaning unused images..."
                    docker image prune -f || true
                '''
                
                echo '✅ Деплой завершен!'
            }
        }
        
        stage('Archive') {
            steps {
                echo '💾 Сохранение артефактов...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        success {
            echo '✅ CI/CD Pipeline успешно завершен!'
            echo "Приложение доступно: http://localhost:8080"
            echo "Swagger UI: http://localhost:8080/swagger-ui/index.html"
        }
        failure {
            echo '❌ Pipeline провалился!'
        }
    }
}

