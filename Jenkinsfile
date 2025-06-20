pipeline {
   agent {
       docker {
             image 'eclipse-temurin:21-jdk-jammy'
             args '-u root -v /var/run/docker.sock:/var/run/docker.sock -v maven-cache:/root/.m2'
       }
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
   }
}