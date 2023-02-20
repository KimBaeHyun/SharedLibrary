

def mvn() {
  properties([parameters([string(defaultValue: 'test', description: 'test or verify', name: 'testType', trim: false)])])

  node {
      stage('git') {
          git branch: 'failed', url: 'https://github.com/hyunil-shin/java-maven-junit-helloworld.git'
      }

      stage('build') {
          withEnv(["PATH+MAVEN=${tool 'mvn-3.9.0'}/bin", "JAVA_HOME=${tool 'openjdk10'}"]) {
              sh 'mvn --version'           
              sh "mvn clean ${params.testType} -Dmaven.test.failure.ignore=true"
          }
      }

      stage('report') {
          junit 'target/surefire-reports/*.xml'
          jacoco execPattern: 'target/**.exec'
      }
  }
}
