plugins {
    java

    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.json:json:20201115")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")

    testImplementation("org.mockito:mockito-core:3.8.0")
    testImplementation("org.mockito:mockito-junit-jupiter:3.8.0")
}

application {
    mainClassName = "pl.sikorski.tadeusz.App"
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
