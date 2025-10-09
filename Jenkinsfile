pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven 3.9'
        nodejs 'NodeJS 20'
        dockerTool 'docker'
    }

    environment {
        SONAR_HOST_URL          = 'http://sonarqube:9000'
        SONAR_TOKEN             = credentials('sonarqube-token')
        SANITIZED_BRANCH_NAME   = "${env.BRANCH_NAME.replaceAll('/', '-')}"
        BACKEND_IMAGE_NAME      = "local/pharmacy-app:${SANITIZED_BRANCH_NAME}-${env.BUILD_NUMBER}"
        FRONTEND_IMAGE_NAME     = "local/frontend-app:${SANITIZED_BRANCH_NAME}-${env.BUILD_NUMBER}"
        BACKEND_CONTAINER_NAME  = "pharmacy-backend-${SANITIZED_BRANCH_NAME}"
        FRONTEND_CONTAINER_NAME = "pharmacy-frontend-${SANITIZED_BRANCH_NAME}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend Test & Analyze') {
            steps {
                script {
                    echo "Waiting for SonarQube server to be operational..."
                    timeout(time: 180, unit: 'SECONDS') {
                        waitUntil {
                            script {
                                try {
                                    sh(script: "curl -s http://sonarqube:9000/api/system/status | grep '\"status\":\"UP\"'", returnStatus: true) == 0
                                } catch (Exception e) {
                                    return false
                                }
                            }
                        }
                    }
                    echo "SonarQube server is up!"
                }
                dir('pharmacy') {
                    withSonarQubeEnv('SonarQube') {
                        sh "mvn clean verify sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }

        stage('SonarQube Quality Gate') {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    echo "Waiting for Docker daemon to be available..."
                    timeout(time: 60, unit: 'SECONDS') {
                        waitUntil {
                            sh(script: "docker info", returnStatus: true) == 0
                        }
                    }
                    echo "Docker daemon is up!"
                }
                dir('pharmacy') {
                    sh "docker build -t ${env.BACKEND_IMAGE_NAME} ."
                }
                dir('frontend') {
                    sh "docker build -t ${env.FRONTEND_IMAGE_NAME} ."
                }
            }
        }

        stage('Deploy to Dev') {
            when { branch 'dev' }
            steps {
                script {
                    echo "Deploying to DEV environment..."
                    sh "docker stop ${env.BACKEND_CONTAINER_NAME} || true"
                    sh "docker rm ${env.BACKEND_CONTAINER_NAME} || true"
                    sh "docker stop ${env.FRONTEND_CONTAINER_NAME} || true"
                    sh "docker rm ${env.FRONTEND_CONTAINER_NAME} || true"
                    sh """
                    docker run -d --rm --name ${env.BACKEND_CONTAINER_NAME} -p 8081:8080 \\
                        -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@host.docker.internal:1523/XEPDB1 \\
                        -e SPRING_DATASOURCE_USERNAME=PHARMACY_LOCAL \\
                        -e SPRING_DATASOURCE_PASSWORD=devpass \\
                        ${env.BACKEND_IMAGE_NAME}
                    """
                    sh "docker run -d --rm --name ${env.FRONTEND_CONTAINER_NAME} -p 4301:80 ${env.FRONTEND_IMAGE_NAME}"
                    echo "Deployed to DEV. Backend: http://localhost:8081 | Frontend: http://localhost:4301"
                }
            }
        }

        stage('Deploy to UAT') {
            when { branch 'uat' }
            steps {
                script {
                    echo "Deploying to UAT environment..."
                    sh "docker stop ${env.BACKEND_CONTAINER_NAME} || true"
                    sh "docker rm ${env.BACKEND_CONTAINER_NAME} || true"
                    sh "docker stop ${env.FRONTEND_CONTAINER_NAME} || true"
                    sh "docker rm ${env.FRONTEND_CONTAINER_NAME} || true"
                    sh """
                    docker run -d --rm --name ${env.BACKEND_CONTAINER_NAME} -p 8082:8080 \\
                        -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@host.docker.internal:1524/XEPDB1 \\
                        -e SPRING_DATASOURCE_USERNAME=PHARMACY_LOCAL \\
                        -e SPRING_DATASOURCE_PASSWORD=uatpass \\
                        ${env.BACKEND_IMAGE_NAME}
                    """
                    sh "docker run -d --rm --name ${env.FRONTEND_CONTAINER_NAME} -p 4302:80 ${env.FRONTEND_IMAGE_NAME}"
                    echo "Deployed to UAT. Backend: http://localhost:8082 | Frontend: http://localhost:4302"
                }
            }
        }

        stage('Deploy to Production') {
            when { branch 'main' }
            steps {
                script {
                    echo "Deploying to PRODUCTION environment..."
                    sh "docker stop ${env.BACKEND_CONTAINER_NAME} || true"
                    sh "docker rm ${env.BACKEND_CONTAINER_NAME} || true"
                    sh "docker stop ${env.FRONTEND_CONTAINER_NAME} || true"
                    sh "docker rm ${env.FRONTEND_CONTAINER_NAME} || true"
                    sh """
                    docker run -d --rm --name ${env.BACKEND_CONTAINER_NAME} -p 8083:8080 \\
                        -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@host.docker.internal:1525/XEPDB1 \\
                        -e SPRING_DATASOURCE_USERNAME=PHARMACY_LOCAL \\
                        -e SPRING_DATASOURCE_PASSWORD=prodpass \\
                        ${env.BACKEND_IMAGE_NAME}
                    """
                    sh "docker run -d --rm --name ${env.FRONTEND_CONTAINER_NAME} -p 4303:80 ${env.FRONTEND_IMAGE_NAME}"
                    echo "Deployed to PRODUCTION. Backend: http://localhost:8083 | Frontend: http://localhost:4303"
                }
            }
        }
        
        stage('Cleanup') {
            steps {
                script {
                    echo "Performing Docker cleanup..."
                    sh "docker system prune -af || true"
                    echo "Cleanup completed."
                }
            }
        }
    }

    post {
        failure {
            emailext(
                to: 'your-lead-email@example.com, your-po-email@example.com',
                subject: "Build FAILED: ${currentBuild.fullDisplayName}",
                body: """<p>The build for ${env.JOB_NAME} failed.</p>
                         <p>Console output: <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>""",
                from: 'your-yahoo-email@example.com',
                mimeType: 'text/html'
            )
        }
    }
}
