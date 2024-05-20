## Spring X-Roles Authorities starter

Provides an ability to extract authorities from `X-Roles` header:

```http request
// Basic authentication
GET https://example.com
Authorization: // any auth
X-Roles: my-role, another-role
```

Allows hitting this endpoint:

```java
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
final class MyController {

    @PreAuthorize("hasAnyAuthority('my-role')")
    public ResponseEntity<String> sayHi() {
        return "Hi!";
    }
}
```