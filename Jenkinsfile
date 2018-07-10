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
        stage ('Build') {
            //failFast true
            parallel {
                stage("ubuntu-build") {
                    agent {
                        label "ubuntu"
                    }
                    steps {
                        checkout scm
                        dir("build") {
                            sh 'cmake .. -DCMAKE_BUILD_TYPE=Release'
                            sh 'make'
                        }
                    }
                }
                stage("centos-build") {
                    agent {
                        label "centos"
                    }
                    steps {
                        checkout scm
                        dir("build") {
                            sh 'cmake .. -DCMAKE_BUILD_TYPE=Release'
                            sh 'make'
                        }
                    }
                }
                stage("win-build") {
                    agent {
                        label "windows"
                    }
                    steps {
                        dir("build") {
                                bat 'cmake .. -G "Visual Studio 15 2017 Win64" '
                                bat "cmake --build . --target ALL_BUILD --config Release"
                            }
                    }
                }
                stage ( "mac-build" ){
                    agent {
                        label "mac"
                    }
                    steps {
                        checkout scm
                        dir("build") {
                            sh 'cmake .. -DCMAKE_BUILD_TYPE=Release'
                            sh 'make'
                            sh 'pwd'
                            sh 'ls -lrth'
                        }
                    }
                }
            }
        }
        stage ('Test'){
            //failFast true
            parallel {
                stage("ubuntu-test") {
                    agent {
                        label "ubuntu"
                    }
                    steps {
                       dir("build") {
                            sh 'ctest'
                        }
                    }
                }
                stage("centos-test") {
                    agent {
                        label "centos"
                    }
                    steps {
                       dir("build") {
                            sh 'ctest'
                        }
                    }
                }
                stage("win-test") {
                    agent {
                        label "windows"
                    }
                    steps {
                        dir("build") {
                            bat 'ctest --config Release'
                        }
                    }
                }
                stage ( "mac-test" ){
                    agent {
                        label "mac"
                    }
                    steps {
                        dir("build") {
                            sh 'pwd'
                            sh 'ls -lrth'
                            sh 'ctest'
                        }
                    }
                }
            }
        }
    //     stage ('Test') {
    //         steps {
    //             parallel (
    //                 "linux" : {
    //                     node ("linux") {
    //                         dir('Build') {
    //                             sh 'ctest'
    //                             // Add other test executables here.
    //                         }

    //                     }
    //                 },
    //                 "windows" : {
    //                     node('windows') {
    //                         dir("build") {
    //                             bat 'ctest'
    //                             // Add other test executables here.
    //                         }


    //                     }
    //                 },
    //                 "mac" : {
    //                     node('mac') {
    //                         dir("build") {
    //                             sh 'ctest'
    //                             // Add other test executables here.
    //                         }

    //                     }
    //                 }
    //             )
    //         }
    //     }
    }

}
