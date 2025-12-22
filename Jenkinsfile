pipeline {
    agent any
    
    triggers {
        pollSCM('H/2 * * * *')
    }
    
    environment {
        APP_NAME = 'adminka'
        APP_VERSION = '1.0.0'
    }
    
    stages {
        stage('Setup') {
            steps {
                echo '⚙️ Проверка окружения...'
                sh 'java -version || echo "Java not found"'
                sh 'chmod +x ./mvnw'
            }
        }
        
        stage('Build') {
            steps {
                echo '🔨 Сборка проекта...'
                sh './mvnw clean compile -DskipTests'
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
        
        stage('Package') {
            steps {
                echo '📦 Создание JAR файла...'
                sh './mvnw package -DskipTests'
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
            echo '✅ Сборка успешно завершена!'
        }
        failure {
            echo '❌ Сборка провалилась!'
        }
    }
}

