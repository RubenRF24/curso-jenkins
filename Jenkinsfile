pipeline {
   agent {
       docker {
             image 'eclipse-temurin:21-jdk-jammy'
             // Cambia 'jenkins-net' por el nombre de tu red real: 'jenkins_sonarqube'
             args '--network jenkins_sonarqube --link sonarqube --add-host=host.docker.internal:host-gateway -u root -v /var/run/docker.sock:/var/run/docker.sock -v maven-cache:/root/.m2'
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
       stage('Build, Test and Analyze') {
           steps {
               // 'sonarqube' debe coincidir con el nombre de tu configuración de servidor SonarQube en Jenkins
               withSonarQubeEnv('sonarqube') {
                   // Usa el parámetro para obtener el token de SonarQube
                   withCredentials([string(credentialsId: params.SONARQUBE_CREDENTIALS_ID, variable: 'SONAR_TOKEN')]) {
                       dir('cafeteria-app') {
                           sh 'chmod +x mvnw'
                           // Ejecuta verify y sonar en un solo comando. Maven se encarga de las rutas.
                           // Reemplaza 'sonar.login' por 'sonar.token' para seguir las buenas prácticas.
                           sh "./mvnw verify sonar:sonar -Dsonar.projectKey=sonarqube -Dsonar.token=${SONAR_TOKEN} -X"
                       }
                   }
               }
           }
           post {
               success {
                   // Espera el resultado del Quality Gate y falla el pipeline si no es 'PASSED'
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