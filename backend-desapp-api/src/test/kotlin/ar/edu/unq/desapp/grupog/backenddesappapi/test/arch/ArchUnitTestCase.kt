package ar.edu.unq.desapp.grupog.backenddesappapi.test.arch

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Service

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArchUnitTestCase {
    private lateinit var classesUnderTest: JavaClasses

    @BeforeEach
    fun setUp() {
        val basePackage = "ar.edu.unq.desapp.grupog.backenddesappapi"
        classesUnderTest = ClassFileImporter().importPackages(basePackage)
    }

    @Test
    fun testArch_testClassesNameShouldEndWithTestCase() {
        classes().that().resideInAPackage("..test..")
            .and().areAnnotatedWith(SpringBootTest::class.java)
            .should().haveSimpleNameEndingWith("TestCase")
            .check(classesUnderTest)
    }

    @Test
    fun testArch_testMethodsShouldStartWithTest() {
        methods().that().areDeclaredInClassesThat().resideInAPackage("..test..")
            .and().areAnnotatedWith(Test::class.java)
            .should().haveNameStartingWith("test")
            .check(classesUnderTest)
    }

    @Test
    fun testArch_classesWithServiceAnnotationShouldEndWithImpl() {
        classes().that().resideInAPackage("..service..")
            .and().areAnnotatedWith(Service::class.java)
            .and().doNotHaveSimpleName("InitServiceInMemory")
            .should().haveSimpleNameEndingWith("Impl")
            .check(classesUnderTest)
    }
}