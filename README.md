# play-java-react-sample-project

A REST API showing Play with a JPA backend. and React in the front.
Thus, this project has two server running, the play server runs on port 9000 and the react one is on 3000.

## How to use it?

### Prerequisites

* [Node.js](https://nodejs.org/)
* [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (recommend version 1.8 or higher)

### Let's get started,

* Fork or clone this repository.

* Use the following [SBT](http://www.scala-sbt.org/) commands which will intern trigger frontend associated npm scripts.
```
    sbt ~run             # Run both backend and frontend builds
```

The initial run will take some time compile both back and front dependencies.
The index page will show a form to add users with name and email. Added users will be shown below.
There is an export button, clicking on it will export all users to excel file.

This project is built on top of sample from play-samples,
[play-java-rest-api-example](https://github.com/playframework/play-samples/tree/3.0.x/play-java-rest-api-example) and
[play-java-react-seed](https://github.com/playframework/play-java-react-seed)

