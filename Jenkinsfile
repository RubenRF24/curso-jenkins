pipeline {
   agent {
       docker {
             image 'eclipse-temurin:21-jdk-jammy'
             args '--network jenkins-net --add-host=host.docker.internal:host-gateway -u root -v /var/run/docker.sock:/var/run/docker.sock -v maven-cache:/root/.m2'
       }
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

       stage('Merge and Push to Master') {
           steps {
               sh 'git checkout master'
               sh 'git merge origin/feature/addtest'
               sh 'git push origin master'
           }
       }
   }
}