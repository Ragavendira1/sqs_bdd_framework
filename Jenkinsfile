pipeline {
  agent {
    node {
      label 'Windows'
    }

  }
  stages {
    stage('Build') {
      parallel {
        stage('Build') {
          steps {
            git(url: 'https://github.com/Ragavendira1/sqs_bdd_framework.git', branch: 'master')
          }
        }

        stage('AUT') {
          steps {
            bat 'mvn'
          }
        }

        stage('') {
          steps {
            cucumber(fileIncludePattern: '**/cucumber.json', jsonReportDirectory: 'Result/Reports/')
          }
        }

      }
    }

    stage('') {
      steps {
        echo 'Review Test Report'
      }
    }

  }
}
