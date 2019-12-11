package com.packagename.myapp;

import com.packagename.myapp.application.ApplicationService;
import com.packagename.myapp.domain.model.Repository;
import com.packagename.myapp.domain.model.ValueObject;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = "com.packagename.myapp")
public class ArchitectureTest {

    public static final String UI_PACKAGE = "..ui..";
    public static final String DOMAIN_MODEL_PACKAGE = "..domain.model..";
    public static final String DOMAIN_SERVICE_PACKAGE = "..domain.service..";
    public static final String APPLICATION_SERVICE_PACKAGE = "..application..";

    // @ArchTest
    public static final ArchRule layers = layeredArchitecture()
            .layer("UI").definedBy(UI_PACKAGE)
            .layer("Application").definedBy(APPLICATION_SERVICE_PACKAGE)
            .layer("DomainServices").definedBy(DOMAIN_SERVICE_PACKAGE)
            .layer("DomainModel").definedBy(DOMAIN_MODEL_PACKAGE)
            .whereLayer("Application").mayOnlyBeAccessedByLayers("UI")
            .whereLayer("DomainServices").mayOnlyBeAccessedByLayers("DomainModel", "Application")
            .whereLayer("UI").mayNotBeAccessedByAnyLayer();

    // @ArchTest
    public static final ArchRule no_cyclic_dependencies_within_domain_model = slices()
            .matching("..domain.model.(*)..")
            .should()
            .beFreeOfCycles();

    // @ArchTest
    public static final ArchRule public_application_service_methods_should_have_nullability_annotations = methods()
            .that()
            .arePublic()
            .and()
            .doNotHaveRawReturnType(new DescribedPredicate<JavaClass>("native or void") {
                @Override
                public boolean apply(JavaClass javaClass) {
                    return javaClass.isPrimitive() || javaClass.isEquivalentTo(Void.TYPE);
                }
            })
            .and()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(ApplicationService.class)
            .should()
            .beAnnotatedWith(NonNull.class)
            .orShould()
            .beAnnotatedWith(Nullable.class);

    // @ArchTest
    public static final ArchRule application_service_classes_should_be_package_private = classes()
            .that()
            .areAnnotatedWith(ApplicationService.class)
            .should()
            .bePackagePrivate();

    // @ArchTest
    public static final ArchRule ui_must_not_access_repositories_directly = noClasses()
            .that()
            .resideInAnyPackage(UI_PACKAGE)
            .should()
            .accessClassesThat().areAssignableTo(Repository.class);

    // @ArchTest
    public static final ArchRule value_object_fields_in_entities_should_have_converters = fields()
            .that()
            .areDeclaredInClassesThat().areAnnotatedWith(Entity.class)
            .or()
            .areDeclaredInClassesThat().areAnnotatedWith(MappedSuperclass.class)
            .and()
            .haveRawType(new DescribedPredicate<JavaClass>("value object") {
                @Override
                public boolean apply(JavaClass javaClass) {
                    return javaClass.isAssignableTo(ValueObject.class);
                }
            })
            .should()
            .beAnnotatedWith(Convert.class);
}
