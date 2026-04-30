configurations {
    register("mockitoAgent")
}

dependencies {
    "mockitoAgent"(libs.mockito.core) { isTransitive = false }

    implementation(libs.spring.boot.data.jpa)
    implementation(libs.mysql.connector)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.security.core)

    testImplementation(libs.spring.boot.starter.test)
    runtimeOnly(libs.h2)
}

tasks.test {
    jvmArgs("-javaagent:${configurations["mockitoAgent"].asPath}")
}