# Eclipse import guide

This repository is configured as a Gradle + Spring Boot Eclipse project using Java 17.

## Import

1. Open Eclipse IDE with Java 17.
2. Choose **File → Import → Gradle → Existing Gradle Project**.
3. Select the `corporate_management` repository folder.
4. Finish the import and let Buildship synchronize Gradle dependencies.
5. In Package Explorer, the project will appear as a Java/Gradle project with `src/main/java`, `src/main/resources`, and `src/test/java`.
6. Run `CorporateTravelApplication.java` as **Spring Boot App**.

The repository includes `.project`, `.classpath`, and `.settings` metadata for Eclipse. Gradle remains the source of truth for dependencies and build configuration.
