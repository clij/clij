pipeline {
    agent none
    stages {
        stage ('Clean') {
            //failFast true
            parallel {
                stage("ubunt-clean") {
                    agent {
                        label "ubuntu"
                    }
                    steps {
                        dir("build") {
                            deleteDir()
                        }
                        sh 'hostname'
                    }
                }
                stage("centos-clean") {
                    agent {
                        label "centos"
                    }
                    steps {
                        dir("build") {
                            deleteDir()
                        }
                        sh 'hostname'
                    }
                }
                stage("win-clean") {
                    agent {
                        label "windows"
                    }
                    steps {
                        dir("build") {
                            deleteDir()
                        }
                        bat 'hostname'
                    }
                }
                stage ( "mac-clean" ){
                    agent {
                        label "mac"
                    }
                    steps {
                        dir("build") {
                            deleteDir()
                        }
                        sh 'hostname'
                    }
                }
            }
        }
    }

}
