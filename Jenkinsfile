pipeline {
   agent {
       docker {
             image 'eclipse-temurin:21-jdk-jammy'
             // Optimizado: Eliminamos el flag --link que está obsoleto
             args '--network jenkins_sonarqube --add-host=host.docker.internal:host-gateway -u root -v /var/run/docker.sock:/var/run/docker.sock -v maven-cache:/root/.m2'
       }
 }

 parameters {
       // Define un parámetro de tipo 'credencial' para GitHub
       credentials(
           name: 'GITHUB_CREDENTIALS_ID', 
           description: 'Credencial de GitHub para hacer push al repositorio', 
           required: true
       )
       // Define un parámetro de tipo 'credencial' para SonarQube
       credentials(
           name: 'SONARQUBE_CREDENTIALS_ID', 
           description: 'Credencial del token de SonarQube', 
           required: true
       )
   }

 environment {
       TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
   }
 
   triggers { pollSCM 'H/2 * * * *' } // poll every 2 mins
 
   stages {
       stage('Build and Test') {
           steps {
               dir('cafeteria-app') {
                   sh 'chmod +x mvnw'
                   // Etapa 1: Solo compilar y probar
                   sh "MAVEN_OPTS='-Xms512m -Xmx2g -XX:MaxMetaspaceSize=512m' ./mvnw clean verify -X"
                   // Guardamos los resultados para la siguiente etapa
                   stash includes: 'target/', name: 'build-results'
               }
           }
       }

       stage('SonarQube Analysis') {
           steps {
               // Recuperamos los resultados de la etapa anterior
               unstash 'build-results'
               withSonarQubeEnv('sonarqube') {
                   withCredentials([string(credentialsId: params.SONARQUBE_CREDENTIALS_ID, variable: 'SONAR_TOKEN')]) {
                       dir('cafeteria-app') {
                           // Etapa 2: Solo analizar. Maven encontrará los .class en el directorio target restaurado
                           sh "MAVEN_OPTS='-Xms512m -Xmx2g -XX:MaxMetaspaceSize=512m' ./mvnw sonar:sonar -Dsonar.projectKey=sonarqube -Dsonar.token=${SONAR_TOKEN} -X"
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
               // Optimizado: Eliminamos la instalación de git, ya que la imagen lo incluye.
               
               // Usa el parámetro en lugar del ID hardcodeado
               withCredentials([string(credentialsId: params.GITHUB_CREDENTIALS_ID, variable: 'GITHUB_TOKEN')]) {
                   sh 'git config --global user.email "jenkins-ci@example.com"'
                   sh 'git config --global user.name "Jenkins CI"'
                   // Aseguramos que estamos en la rama correcta antes de hacer merge
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