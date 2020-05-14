pipeline {
  agent {
    node {
      label 'Windows'
    }

  }
  stages {
    stage('error') {
      parallel {
        stage('Build') {
          steps {
            git(url: 'https://github.com/Ragavendira1/sqs_bdd_framework.git', branch: 'master')
          }
        }

        stage('AUT') {
          steps {
            bat 'mvn test'
          }
        }

        stage('Generating Reports') {
          steps {
            cucumber(fileIncludePattern: '**/cucumber.json', jsonReportDirectory: 'Result/Reports/')
          }
        }

      }
    }

  }
}