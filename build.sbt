name := "simple-macros"

version := "0.3"

organization := "com.foursquare"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4")

libraryDependencies <++= (scalaVersion) { scalaVersion =>
  Seq(
    "org.scala-lang"                %  "scala-reflect"      % scalaVersion,
    "junit"                         %  "junit"              % "4.10"       % "test",
    "com.novocode"                  %  "junit-interface"    % "0.9"        % "test"
  )
}


scalacOptions ++= Seq("-deprecation", "-unchecked")

testFrameworks += new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker")

publishTo <<= (version) { v =>
  val nexus = "https://oss.sonatype.org/"
  if (v.endsWith("-SNAPSHOT"))
    Some("snapshots" at nexus+"content/repositories/snapshots/")
  else
    Some("releases" at nexus+"service/local/staging/deploy/maven2")
}

credentials ++= {
  val sonaType = ("Sonatype Nexus Repository Manager", "oss.sonatype.org")
  def loadMavenCredentials(file: java.io.File) : Seq[Credentials] = {
    xml.XML.loadFile(file) \ "servers" \ "server" map (s => {
      val host = (s \ "id").text
      val realm = if (host == sonaType._2) sonaType._1 else "Unknown"
      Credentials(realm, host, (s \ "username").text, (s \ "password").text)
    })
  }
  val ivyCredentials   = Path.userHome / ".ivy2" / ".credentials"
  val mavenCredentials = Path.userHome / ".m2"   / "settings.xml"
  (ivyCredentials.asFile, mavenCredentials.asFile) match {
    case (ivy, _) if ivy.canRead => Credentials(ivy) :: Nil
    case (_, mvn) if mvn.canRead => loadMavenCredentials(mvn)
    case _ => Nil
  }
}

ivyConfigurations += config("default") extend(Compile)

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { x => false }

pomExtra := (
<url>https://github.com/foursquare/simple-macros</url>
<licenses>
  <license>
    <name>Apache 2</name>
    <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
    <distribution>repo</distribution>
    <comments>A business-friendly OSS license</comments>
  </license>
</licenses>
<scm>
  <url>git@github.com/foursquare/simple-macros.git</url>
  <connection>scm:git:git@github.com/foursquare/simple-macros.git</connection>
</scm>
<developers>
  <developer>
    <id>jeff</id>
    <name>Jeff Jenkins</name>
    <email>jj@foursquare.com</email>
  </developer>
  <developer>
    <id>john</id>
    <name>John Gallagher</name>
    <email>john@foursquare.com</email>
  </developer>
</developers>
)

