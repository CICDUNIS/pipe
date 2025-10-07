pipeline {
    agent any

    tools {
        jdk 'JDK 17'
        maven 'Maven 3.9'
        nodejs 'NodeJS 20'
    }

    environment {
        SONAR_HOST_URL          = 'http://sonarqube_pipe:9000'
        SONAR_TOKEN             = credentials('sonarqube-token')
        
        BACKEND_IMAGE_NAME      = "local/pharmacy-app:${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
        FRONTEND_IMAGE_NAME     = "local/frontend-app:${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
        
        BACKEND_CONTAINER_NAME  = "pharmacy-backend-${env.BRANCH_NAME}"
        FRONTEND_CONTAINER_NAME = "pharmacy-frontend-${env.BRANCH_NAME}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend Test & Analyze') {
            steps {
                dir('pharmacy') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar -Dsonar.login=$SONAR_TOKEN'
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
                    dir('pharmacy') {
                        sh "docker build -t ${env.BACKEND_IMAGE_NAME} ."
                    }
                    dir('frontend') {
                        sh "docker build -t ${env.FRONTEND_IMAGE_NAME} ."
                    }
                }
            }
        }

        stage('Deploy to Environment') {
            stages {
                stage('Deploy to Dev') {
                    when { branch 'dev' }
                    steps {
                        script {
                            sh "docker stop ${env.BACKEND_CONTAINER_NAME} || true"
                            sh "docker rm ${env.BACKEND_CONTAINER_NAME} || true"
                            sh "docker stop ${env.FRONTEND_CONTAINER_NAME} || true"
                            sh "docker rm ${env.FRONTEND_CONTAINER_NAME} || true"

                            sh """
                            docker run -d --rm --name ${env.BACKEND_CONTAINER_NAME} -p 8081:8080 --network=pipe_cicd-net \
                              -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle-db-dev:1521/XEPDB1 \
                              -e SPRING_DATASOURCE_USERNAME=PHARMACY_LOCAL \
                              -e SPRING_DATASOURCE_PASSWORD=devpass \
                              ${env.BACKEND_IMAGE_NAME}
                            """
                            sh "docker run -d --rm --name ${env.FRONTEND_CONTAINER_NAME} -p 4301:80 ${env.FRONTEND_IMAGE_NAME}"
                            
                            echo "Deployed to DEV. Backend: http://localhost:8081, Frontend: http://localhost:4301"
                        }
                    }
                }

                stage('Deploy to UAT') {
                    when { branch 'uat' }
                    steps {
                        script {
                            sh "docker stop ${env.BACKEND_CONTAINER_NAME} || true"
                            sh "docker rm ${env.BACKEND_CONTAINER_NAME} || true"
                            sh "docker stop ${env.FRONTEND_CONTAINER_NAME} || true"
                            sh "docker rm ${env.FRONTEND_CONTAINER_NAME} || true"
                            
                            sh """
                            docker run -d --rm --name ${env.BACKEND_CONTAINER_NAME} -p 8082:8080 --network=pipe_cicd-net \
                              -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle-db-uat:1521/XEPDB1 \
                              -e SPRING_DATASOURCE_USERNAME=PHARMACY_LOCAL \
                              -e SPRING_DATASOURCE_PASSWORD=uatpass \
                              ${env.BACKEND_IMAGE_NAME}
                            """
                            sh "docker run -d --rm --name ${env.FRONTEND_CONTAINER_NAME} -p 4302:80 ${env.FRONTEND_IMAGE_NAME}"
                            
                            echo "Deployed to UAT. Backend: http://localhost:8082, Frontend: http://localhost:4302"
                        }
                    }
                }

                stage('Deploy to Production') {
                    when { branch 'master' }
                    steps {
                        script {
                            sh "docker stop ${env.BACKEND_CONTAINER_NAME} || true"
                            sh "docker rm ${env.BACKEND_CONTAINER_NAME} || true"
                            sh "docker stop ${env.FRONTEND_CONTAINER_NAME} || true"
                            sh "docker rm ${env.FRONTEND_CONTAINER_NAME} || true"
                            
                            sh """
                            docker run -d --rm --name ${env.BACKEND_CONTAINER_NAME} -p 8083:8080 --network=pipe_cicd-net \
                              -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@oracle-db-prod:1521/XEPDB1 \
                              -e SPRING_DATASOURCE_USERNAME=PHARMACY_LOCAL \
                              -e SPRING_DATASOURCE_PASSWORD=prodpass \
                              ${env.BACKEND_IMAGE_NAME}
                            """
                            sh "docker run -d --rm --name ${env.FRONTEND_CONTAINER_NAME} -p 4303:80 ${env.FRONTEND_IMAGE_NAME}"
                            
                            echo "Deployed to PRODUCTION. Backend: http://localhost:8083, Frontend: http://localhost:4303"
                        }
                    }
                }
            }
        }
    }

    post {
	    failure {
		    emailext(
				    to: 'JAFY.FergusonY@gmail.com, jflores@unis.edu.gt', 
				    subject: "Build FAILED: ${currentBuild.fullDisplayName}",
				    body: """<p>The build for ${env.JOB_NAME} failed.</p>
				    <p>Console output: <a href="${env.BUILD_URL}console">${env.BUILD_URL}console</a></p>""",
				    from: 'nightowlgt@yahoo.com',
				    mimeType: 'text/html'
			    )
	    }
    }
}
