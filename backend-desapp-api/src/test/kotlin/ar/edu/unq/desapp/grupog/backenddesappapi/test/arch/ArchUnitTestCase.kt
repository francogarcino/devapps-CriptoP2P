package ar.edu.unq.desapp.grupog.backenddesappapi.test.arch

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

class ArchUnitTestCase {

    @Test
    fun testArch_allTestFileNamesEndsWithTestCase() {
        val basePackage = "ar.edu.unq.desapp.grupog.backenddesappapi"
        val testPackage = "$basePackage.test"
        val classesUnderTest : JavaClasses = ClassFileImporter().importPackages(testPackage)
        val rule : ArchRule = classes().that().resideInAnyPackage(testPackage)
            .and().resideOutsideOfPackage("$testPackage.utils")
            .and().doNotHaveSimpleName("BackendDesappApiApplicationTests")
            .should().haveSimpleNameEndingWith("TestCase")

        rule.check(classesUnderTest)
    }

    @Test
    fun testArch_allFunctionsWithTestAnnontationShouldStartWithTest() {
        val basePackage = "ar.edu.unq.desapp.grupog.backenddesappapi"
        val testPackage = "$basePackage.test"
        val classesUnderTest : JavaClasses = ClassFileImporter().importPackages(testPackage)

        val rule : ArchRule = methods().that().areAnnotatedWith(Test::class.java)
            .and().areDeclaredInClassesThat().resideInAPackage(testPackage)
            .should().haveNameStartingWith("test")

        rule.check(classesUnderTest)
    }
}