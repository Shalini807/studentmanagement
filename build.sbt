name := """studentmanagement"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += jdbc

libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.4.21.Final"
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-data-jpa" % "2.4.0"
libraryDependencies += "org.springframework.boot" % "spring-boot-starter-web" % "2.4.2"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.2"
//libraryDependencies += "org.elasticsearch" % "elasticsearch" % "7.10.0"
//libraryDependencies += "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.10.0"

libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "8.0.21",
  "org.hibernate" % "hibernate-core" % "5.4.20.Final",
  "javax.persistence" % "javax.persistence-api" % "2.2"
)
//libraryDependencies += "com.iheart" %% "swagger-play2" % "2.8.1"
//libraryDependencies ++= Seq(
//  "io.swagger.core.v3" % "swagger-annotations" % "2.1.3",
//  "io.swagger.core.v3" % "swagger-core" % "2.1.3"
//)
//libraryDependencies ++= Seq(
//  javaJpa,
//  "org.hibernate" % "hibernate-core" % "5.4.18.Final",
//  "mysql" % "mysql-connector-java" % "8.0.25",
//  "com.iheart" %% "swagger-play2" % "3.0.0"
//)