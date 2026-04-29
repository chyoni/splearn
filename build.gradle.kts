import com.diffplug.gradle.spotless.SpotlessExtension
import java.io.BufferedReader
import java.io.InputStreamReader
// import net.ltgt.gradle.errorprone.ErrorProneOptions

plugins {
    java
    jacoco
    id("jacoco-report-aggregation")
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency)
    alias(libs.plugins.spotless)
    // alias(libs.plugins.errorprone)
}

val excludeFromCoverage = mutableListOf<String>()
file("test-coverage-exclude.asap").inputStream().use { inputStream ->
    excludeFromCoverage.addAll(
        BufferedReader(InputStreamReader(inputStream))
            .lines()
            .parallel()
            .map { it.substring(7).trim() }
            .toList()
    )
}

allprojects {
    group = "cwchoiit"
    version = "1.0.0"
    description = "Splearn"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "java-library")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.diffplug.spotless")
    // apply(plugin = "net.ltgt.errorprone")

    var libs = rootProject.the<VersionCatalogsExtension>().named("libs")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }

        register("querydsl") {
            extendsFrom(configurations.compileClasspath.get())
        }
    }

    // Global Dependency
    dependencies {
        implementation(libs.findLibrary("spring-boot-starter-web").orElseThrow())

        /* Null Check
        implementation(libs.findLibrary("jspecify").orElseThrow())
        errorprone(libs.findLibrary("error-prone-core").orElseThrow())
        errorprone(libs.findLibrary("uber-nullaway").orElseThrow())
        */

        compileOnly(libs.findLibrary("lombok").orElseThrow())
        annotationProcessor(libs.findLibrary("lombok").orElseThrow())

        testImplementation(libs.findLibrary("junit-jupiter").orElseThrow())
        testImplementation(libs.findLibrary("spring-boot-starter-test").orElseThrow())
        testImplementation(libs.findLibrary("lombok").orElseThrow())
        testAnnotationProcessor(libs.findLibrary("lombok").orElseThrow())
    }

    // Spotless lint automatically applied in compile time (Intellij)
    if (System.getProperty("idea.active") == "true") {
        tasks.named("processResources") {
            dependsOn("spotlessApply")
        }
    }

    configure<SpotlessExtension> {
        java {
            target("**/*.java")
            targetExclude("**/generated/**/*.java")
            googleJavaFormat().aosp()
            importOrder()
            endWithNewline()
            trimTrailingWhitespace()
        }
    }

    // test configuration
    configure<JacocoPluginExtension> {
        toolVersion = "0.8.12"
    }

    tasks.test {
        testLogging {
            showExceptions = true
            showStackTraces = true
            showStandardStreams = true
        }
        useJUnitPlatform()
        finalizedBy("jacocoTestReport")
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            html.required.set(true)
            xml.required.set(true)
            csv.required.set(false)
        }
        finalizedBy("jacocoTestCoverageVerification")

        classDirectories.setFrom(
            classDirectories.files.map { dir ->
                fileTree(dir) {
                    exclude(excludeFromCoverage.map { "$it.class" })
                }
            }
        )
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                enabled = true
                element = "CLASS"
                excludes = (excludes ?: mutableListOf()) + excludeFromCoverage.map {
                    it.replace("/", ".")
                }

                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = "0.00".toBigDecimal()
                }

                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.00".toBigDecimal()
                }
            }
        }
    }

    /*tasks.withType<JavaCompile>().configureEach {
        (options as ExtensionAware).extensions.configure<ErrorProneOptions>("errorprone") {
            error("NullAway") // NullAway 위반을 컴파일 에러로 처리
            option("NullAway:AnnotatedPackages", "cwchoiit.splearn") // 검사할 패키지
            option("NullAway:JSpecifyMode", "true") // JSpecify 모드 활성화
        }
    }*/
}