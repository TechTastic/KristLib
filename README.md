# KristLib

Java wrapper for the [Krist] API

[![github](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg)](https://github.com/TechTastic/KristLib)
[![jitpack](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/jitpack_vector.svg)](https://jitpack.io/#TechTastic/KristLib/master-SNAPSHOT)
[![maven](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/maven_vector.svg)](https://maven.apache.org/)
[![java](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/built-with/java_vector.svg)](https://java.com)

## Adding to your project

### Maven with JitPack

Add the repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency:

```xml
<dependency>
<groupId>com.github.TechTastic</groupId>
<artifactId>KristLib</artifactId>
<version>Tag</version>
</dependency>
```

### Gradle with JitPack

Add the repository:

```groovy
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency:

```groovy
dependencies {
    implementation 'com.github.TechTastic:KristLib:Tag'
}
```

[Krist]: https://krist.dev