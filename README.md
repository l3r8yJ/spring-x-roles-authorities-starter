## Spring X-Roles Authorities starter

This starter allows you to test your role model for non-production scenarios. 

[Link](https://www.l3r8y.ru/2024/05/24/role-model-testing-with-spring-boot) to blog post.

[![maven central](http://maven-badges.herokuapp.com/maven-central/ru.l3r8y/spring-x-roles-authorities-starter/badge.svg)](https://search.maven.org/artifact/ru.l3r8y/spring-x-roles-authorities-starter)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/l3r8yJ/spring-x-roles-authorities-starter/blob/master/LICENSE.txt)

## How to

1. Add a dependency

```xml

<dependency>
    <groupId>ru.l3r8y</groupId>
    <artifactId>spring-x-roles-authorities-starter</artifactId>
    <version><!-- latest --></version>
</dependency>
```

2. Add to `application.yaml`

```yaml
x-roles:
  enabled: true
```

Done! Now starter provides an ability to extract authorities from `X-Roles` header:

```http request
// Basic authentication
GET https://example.com/endpoint
Authorization: // any auth
X-Roles: my-role, another-role
```

Allows hitting this endpoint:

```java
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
final class Controller {

    @GetMapping("/endpoint")
    @PreAuthorize("hasAnyAuthority('my-role')")
    public ResponseEntity<String> sayHi() {
        return "Hi!";
    }
}
```

## How to Contribute

Fork repository, make changes, then send us a [pull request](https://www.yegor256.com/2014/04/15/github-guidelines.html).
We will review your changes and apply them to the `master` branch shortly,
provided they don't violate our quality standards.
To avoid frustration,
before sending us your pull request please run full Maven build:

```bash
mvn clean install
```

You will need [Maven 3.3+](https://maven.apache.org) and Java 8+ installed.
