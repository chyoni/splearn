configurations {
    register("mockitoAgent")
}

dependencies {
    "mockitoAgent"(libs.mockito.core) { isTransitive = false }

    implementation(libs.spring.boot.data.jpa)
    implementation(libs.mysql.connector)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.mockito.core)
    runtimeOnly(libs.h2)
}

tasks.test {
    jvmArgs("-javaagent:${configurations["mockitoAgent"].asPath}")
}