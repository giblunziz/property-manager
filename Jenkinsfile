pipeline {
    agent any
    tools {
        maven 'Maven 3.5.4'
		jdk 'jdk8'
    }
	stages {
		stage ('Initialize') {
        	steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

	
    	stage('Clean') {
       		steps {
           		sh '''
           			mvn clean
           		'''
       		}
    	}
    	stage('Build') {
       		steps {
           		sh '''
           			mvn install
           		'''
       		}
    	}
    	stage('Deploy') {
       		steps {
           		sh '''
           			mvn deploy
           		'''
       		}
    	}
	}
}
