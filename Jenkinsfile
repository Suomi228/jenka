pipeline {
    agent any
    
    triggers {
        pollSCM('H/1 * * * *')
    }
    
    environment {
        APP_NAME = 'adminka'
        APP_VERSION = '1.0.0'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo '📥 Получение исходного кода...'
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                echo '🔨 Сборка проекта...'
                sh 'mvn clean compile -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                echo '🧪 Запуск тестов...'
                sh 'mvn test'
            }
            post {
                always {
                    // Публикация результатов тестов
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                echo '📦 Создание JAR файла...'
                sh 'mvn package -DskipTests'
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
        always {
            echo '🧹 Очистка рабочего пространства...'
            cleanWs()
        }
    }
}

