pipeline {
   agent {
       docker {
             image 'eclipse-temurin:21-jdk-jammy'
             args '--network jenkins_sonarqube --add-host=host.docker.internal:host-gateway -u root -v /var/run/docker.sock:/var/run/docker.sock -v maven-cache:/root/.m2'
       }
   }

   parameters {
       credentials(name: 'GITHUB_CREDENTIALS_ID', description: 'Credencial de GitHub para hacer push al repositorio', required: true)
       credentials(name: 'SONARQUBE_CREDENTIALS_ID', description: 'Credencial del token de SonarQube', required: true)
   }

   environment {
       TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
   }
 
   triggers { pollSCM 'H/2 * * * *' }

   stages {
       stage('Build, Test and Analyze') {
           steps {
               withSonarQubeEnv('sonarqube') {
                   withCredentials([string(credentialsId: params.SONARQUBE_CREDENTIALS_ID, variable: 'SONAR_TOKEN')]) {
                       dir('cafeteria-app') {
                           sh 'chmod +x mvnw'
                           // Comando unificado y optimizado. La forma más eficiente.
                           sh "MAVEN_OPTS='-Xms512m -Xmx2g -XX:MaxMetaspaceSize=512m' ./mvnw clean verify sonar:sonar -Dsonar.projectKey=sonarqube -Dsonar.token=${SONAR_TOKEN} -X"
                       }
                   }
               }
           }
           post {
               success {
                   timeout(time: 2, unit: 'MINUTES') {
                       waitForQualityGate abortPipeline: true
                   }
               }
           }
       }

       stage('Merge and Push to Master') {
           steps {
               withCredentials([string(credentialsId: params.GITHUB_CREDENTIALS_ID, variable: 'GITHUB_TOKEN')]) {
                   sh 'git config --global user.email "jenkins-ci@example.com"'
                   sh 'git config --global user.name "Jenkins CI"'
                   sh 'git checkout feature/addtest' 
                   sh 'git checkout master'
                   sh 'git merge origin/feature/addtest'
                   sh 'git remote set-url origin https://${GITHUB_TOKEN}@github.com/RubenRF24/curso-jenkins.git'
                   sh 'git push origin master'
               }
           }
       }
   }
   
   post {
       success {
           slackSend(
               channel: '#jenkins-ci',
               color: 'good',
               message: "SUCCESS: Pipeline '${env.JOB_NAME}' build #${env.BUILD_NUMBER} completed successfully. <${env.BUILD_URL}|Open>"
           )
       }
       failure {
           slackSend(
               channel: '#jenkins-ci',
               color: 'danger',
               message: "FAILURE: Pipeline '${env.JOB_NAME}' build #${env.BUILD_NUMBER} failed. <${env.BUILD_URL}|Open>"
           )
       }
   }
}