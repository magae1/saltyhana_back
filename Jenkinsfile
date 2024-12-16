pipeline {
    agent any

    environment {
        EMAIL = credentials('my-slack-email-address')
    }

    stages {
        stage("Build") {
            steps {
                sh "./gradlew build"
            }
        }
        stage("Docker build") {
            steps {
                sh "docker build -t spring-server:1.0 ."
            }
        }
        stage("Run docker container") {
            steps {
                sh "docker run -d -p 80:9090 --name server spring-server:1.0"
            }
        }
    }

    post {
        success {
            script {
                def res = slackSend(channel: '#짭짤하나', color: 'good', message: "스프링 배포 성공!")
                res.addReaction('pig')
                res.addReaction('진행시켜')
            }
        }
        failure {
            script {
                def userId = slackUserIdFromEmail("$EMAIL")
                def res = slackSend(channel: '#짭짤하나', color: 'danger', message: "스프링 배포 실패! <@$userId>")
                res.addReaction('비상')
            }
        }
    }
}