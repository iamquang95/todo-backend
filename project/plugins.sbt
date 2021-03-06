addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.0")
//addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat" % "0.1.3")
addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")

// Deploy to heroku plug in
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")
addSbtPlugin("com.heroku" % "sbt-heroku" % "2.1.0")
