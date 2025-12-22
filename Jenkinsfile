pipeline {
    agent any
    
    triggers {
        pollSCM('H/2 * * * *')
    }
    
    environment {
        APP_NAME = 'adminka-app'
        IMAGE_NAME = 'adminka'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Setup') {
            steps {
                echo '⚙️ Проверка окружения...'
                sh 'java -version || echo "Java not found"'
                sh 'docker --version || echo "Docker not found"'
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
        
        stage('Build Docker Image') {
            steps {
                echo '🐳 Сборка Docker образа...'
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
            }
        }
        
        stage('Deploy') {
            steps {
                echo '🚀 Деплой приложения...'
                
                // Остановка и удаление старого контейнера
                sh '''
                    echo "Stopping old container..."
                    docker stop ${APP_NAME} || true
                    docker rm ${APP_NAME} || true
                '''
                
                // Удаление старых образов (кроме latest и текущего)
                sh '''
                    echo "Cleaning old images..."
                    docker images ${IMAGE_NAME} --format "{{.Tag}}" | grep -v latest | grep -v ${IMAGE_TAG} | xargs -r -I {} docker rmi ${IMAGE_NAME}:{} || true
                '''
                
                // Создание сети если не существует
                sh '''
                    docker network create monitoring 2>/dev/null || true
                '''
                
                // Запуск нового контейнера
                sh '''
                    echo "Starting new container..."
                    docker run -d \
                        --name ${APP_NAME} \
                        --network monitoring \
                        -p 8080:8080 \
                        -e SPRING_PROFILES_ACTIVE=prod \
                        --restart unless-stopped \
                        ${IMAGE_NAME}:latest
                '''
                
                echo '✅ Деплой завершен!'
            }
        }
        
        stage('Health Check') {
            steps {
                echo '🏥 Проверка здоровья приложения...'
                sh '''
                    sleep 30
                    curl -f http://localhost:8080/actuator/health || exit 1
                '''
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
            // Откат к предыдущей версии при ошибке
            sh '''
                echo "Attempting rollback..."
                docker stop ${APP_NAME} || true
                docker rm ${APP_NAME} || true
            '''
        }
    }
}

