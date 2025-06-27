pipeline {
   agent {
       docker {
             image 'eclipse-temurin:21-jdk-jammy'
             args '--network jenkins-net --add-host=host.docker.internal:host-gateway -u root -v /var/run/docker.sock:/var/run/docker.sock -v maven-cache:/root/.m2'
       }
 }

 parameters {
       // Define un parámetro de tipo 'credencial'
       // Se corrigió 'credential' a 'credentials'
       credentials(
           name: 'GITHUB_CREDENTIALS_ID', 
           description: 'Credencial de GitHub para hacer push al repositorio', 
           required: true
       )
   }

 environment {
       TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
   }
 
   triggers { pollSCM 'H/2 * * * *' } // poll every 2 mins
 
   stages {

       stage('SonarQube Analysis') {
           steps {
               // 'SonarQube' debe coincidir con el nombre de tu configuración de servidor SonarQube en Jenkins
               withSonarQubeEnv('SonarQube') {
                   dir('cafeteria-app') {
                       // Ejecuta el scanner de SonarQube con las propiedades que definiste
                       sh './mvnw sonar:sonar -Dsonar.projectKey=sonarqube -Dsonar.sources=src/main/java -Dsonar.java.binaries=target/classes -X'
                   }
               }
           }
           post {
               success {
                   // Espera el resultado del Quality Gate y falla el pipeline si no es 'PASSED'
                   // El timeout es opcional pero recomendado
                   timeout(time: 1, unit: 'MINUTES') {
                       waitForQualityGate abortPipeline: true
                   }
               }
           }
       }

       stage('Merge and Push to Master') {
           steps {
               sh '''
                   apt-get update && apt-get install -y git
               '''
               
               // Usa el parámetro en lugar del ID hardcodeado
               withCredentials([string(credentialsId: params.GITHUB_CREDENTIALS_ID, variable: 'GITHUB_TOKEN')]) {
                   sh 'git config --global user.email "jenkins-ci@example.com"'
                   sh 'git config --global user.name "Jenkins CI"'
                   sh 'git checkout master'
                   sh 'git merge origin/feature/addtest'
                   sh 'git remote set-url origin https://${GITHUB_TOKEN}@github.com/RubenRF24/curso-jenkins.git'
                   sh 'git push origin master'
               }
           }
       }
       
       stage('Build and Test') {
           steps {
                dir('cafeteria-app') {
                    sh 'chmod +x mvnw'
                   sh './mvnw verify'
               }
           }
       }
   }
   
   post {
       success {
           slackSend(
               channel: '#jenkins-ci', // REEMPLAZA con tu canal
               color: 'good',
               message: "SUCCESS: Pipeline '${env.JOB_NAME}' build #${env.BUILD_NUMBER} completed successfully. <${env.BUILD_URL}|Open>"
           )
       }
       failure {
           slackSend(
               channel: '#jenkins-ci', // REEMPLAZA con tu canal
               color: 'danger',
               message: "FAILURE: Pipeline '${env.JOB_NAME}' build #${env.BUILD_NUMBER} failed. <${env.BUILD_URL}|Open>"
           )
       }
   }
}