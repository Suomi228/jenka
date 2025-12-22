pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'
            args '-v /root/.m2:/root/.m2'  // Кэширование Maven зависимостей
        }
    }
    
    triggers {
        pollSCM('H/1 * * * *')
    }
    
    environment {
        APP_NAME = 'adminka'
        APP_VERSION = '1.0.0'
    }
    
    stages {
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
    }
}

