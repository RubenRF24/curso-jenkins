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
       stage('Build and Test') {
           steps {
                dir('cafeteria-app') {
                    sh 'chmod +x mvnw'
                   sh './mvnw verify'
               }
           }
       }

       stage('SonarQube Analysis') {
           steps {
               // 'sonarqube' debe coincidir con el nombre de tu configuración de servidor SonarQube en Jenkins
               withSonarQubeEnv('sonarqube') {
                   // Usa el parámetro para obtener el token de SonarQube
                   withCredentials([string(credentialsId: params.SONARQUBE_CREDENTIALS_ID, variable: 'SONAR_TOKEN')]) {
                       
                       // Paso robusto para esperar a SonarQube (movido aquí dentro)
                       sh '''
                           echo "Waiting for SonarQube to become available..."
                           # apt-get se ejecuta una sola vez si es necesario
                           if ! command -v curl &> /dev/null; then
                               apt-get update && apt-get install -y curl
                           fi
                           
                           # Bucle de espera con el token disponible
                           for i in {1..30}; do
                               # Usamos -f para que curl falle si hay un error de conexión
                               if curl -s -f -u ${SONAR_TOKEN}: http://sonarqube:9090/api/system/status | grep -q '"status":"UP"'; then
                                   echo "SonarQube is UP!"
                                   exit 0 # Salir del script con éxito
                               fi
                               echo "SonarQube not yet available, waiting 5 seconds... (Attempt $i/30)"
                               sleep 5
                           done
                           echo "SonarQube did not start in time."
                           exit 1 # Salir del script con error
                       '''

                       dir('cafeteria-app') {
                           // Ejecuta el scanner pasando el token explícitamente
                           sh "./mvnw sonar:sonar -Dsonar.projectKey=sonarqube -Dsonar.sources=src/main/java -Dsonar.java.binaries=target/classes -Dsonar.login=${SONAR_TOKEN} -X"
                       }
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