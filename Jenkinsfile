
pipeline {
    agent any

    environment {
        JAVA_HOME = tool 'JDK11'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    sh "${JAVA_HOME}/bin/javac -version"
                    sh "${JAVA_HOME}/bin/java -version"

                    // Example: Maven build
                    sh "mvn clean install"
                    sh "mvn package"
                }
            }
        }
    }

    post {
        success {
            // Actions to be performed when the build is successful
            echo 'Build successful! Deploy or perform additional steps here.'
        }
        failure {
            // Actions to be performed when the build fails
            echo 'Build failed. Check the logs for errors.'
        }
    }
}
