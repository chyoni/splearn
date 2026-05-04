package cwchoiit.splearn.member;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(
        packages = "cwchoiit.splearn.member",
        importOptions = ImportOption.DoNotIncludeTests.class)
public class HexagonalArchitectureTest {

    @ArchTest
    void hexagonalArchitecture(JavaClasses classes) {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("domain")
                .definedBy("cwchoiit.splearn.member.domain..")
                .layer("application")
                .definedBy("cwchoiit.splearn.member.application..")
                .layer("adapter")
                .definedBy("cwchoiit.splearn.member.adapter..")
                .whereLayer("domain")
                .mayOnlyBeAccessedByLayers("application", "adapter")
                .whereLayer("application")
                .mayOnlyBeAccessedByLayers("adapter")
                .whereLayer("adapter")
                .mayNotBeAccessedByAnyLayer()
                .check(classes);
    }
}
