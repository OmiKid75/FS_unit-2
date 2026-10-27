# Secure Backend API — Experiments 2.3.1 and 2.3.2

One Spring Boot project demonstrates two related but distinct experiments:

- **Experiment 2.3.1:** JWT authentication, Spring Security filter chain, stateless access, RBAC (`USER`/`ADMIN`), and method-level `@PreAuthorize`.
- **Experiment 2.3.2:** AES-256-GCM encryption, encrypted credential storage, short-lived access JWTs, opaque refresh tokens, refresh-token rotation, and logout/revocation.

## Requirements

- JDK 17 or later
- Maven 3.6+ (or open the project in VS Code/IntelliJ and let it import Maven)
- Postman

## Run in VS Code PowerShell

```powershell
# Verify Java
java -version

# Run the application (Maven must be installed)
mvn spring-boot:run
```

The Maven Wrapper scripts are included. On Windows PowerShell, use `./mvnw.cmd spring-boot:run`; on macOS/Linux, use `./mvnw spring-boot:run`. The wrapper downloads Maven the first time it runs.

Application base URL: `http://localhost:8080`
H2 console for local lab only: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/secure_backend_db`, user `sa`, blank password).

## Demo users

| Username | Password | Role |
|---|---|---|
| `student` | `Student123!` | USER |
| `admin` | `Admin123!` | ADMIN |

These are **classroom demo credentials only**. Do not use them in production. Demo users are seeded automatically on first start. New registrations receive the USER role; the public registration endpoint cannot assign ADMIN.

## Postman workflow

Import `docs/postman_collection.json`. Login as `student` or `admin`, copy the returned `accessToken` and `refreshToken` into the collection variables, and send the protected requests with `Authorization: Bearer {{accessToken}}`.

### Experiment 2.3.1 proof

1. Call `GET /api/user/profile` without a token: expect **401**.
2. Login as `student`; call `/api/user/profile`: expect **200**.
3. With the student's access token, call `/api/admin/dashboard`: expect **403**.
4. Login as `admin`; call `/api/admin/dashboard`: expect **200**.
5. Inspect `SecurityConfig.java`, `JwtAuthenticationFilter.java`, and `@PreAuthorize` in the controllers.

### Experiment 2.3.2 proof

1. `POST /api/crypto/encrypt` with `{"value":"Sample secret for lab"}`. Record the ciphertext.
2. Login as admin and call `POST /api/crypto/decrypt` with that ciphertext; the plaintext should match.
3. As admin, `POST /api/credentials` with a sample label and secret. The response returns ciphertext, not the secret.
4. Open the H2 console and inspect `ENCRYPTED_CREDENTIALS`; the `CIPHERTEXT` column should not contain the original secret.
5. Login and use `/api/auth/refresh` with the refresh token. Save the new pair. Reusing the old refresh token should fail because it was rotated.
6. Call `/api/auth/logout` with the current refresh token, then try to refresh it; it should fail.

## Main endpoints

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public; creates USER only |
| POST | `/api/auth/login` | Public |
| POST | `/api/auth/refresh` | Public; refresh token required |
| POST | `/api/auth/logout` | Public; revokes refresh token if found |
| GET | `/api/auth/me` | Authenticated |
| GET | `/api/public/hello` | Public |
| GET | `/api/user/profile` | USER or ADMIN |
| GET | `/api/admin/dashboard` | ADMIN only |
| POST | `/api/crypto/encrypt` | Public demonstration endpoint; use fake data only |
| POST | `/api/crypto/decrypt` | ADMIN only |
| POST | `/api/credentials` | ADMIN only; encrypts before storing |
| GET | `/api/credentials/{id}` | ADMIN only; returns ciphertext only |

## Security and limitations

- Passwords are hashed with BCrypt, not stored as plaintext.
- Access JWTs expire after 5 minutes by default. Refresh tokens are random opaque values, stored only as SHA-256 hashes, expire after 7 days, and rotate on refresh.
- AES uses AES-256-GCM with a fresh random 96-bit IV for each encryption.
- The application properties contain **demo fallback keys only** so the classroom project starts easily. Set `JWT_SECRET_BASE64` and `AES_SECRET_BASE64` environment variables to strong random values for your own local run; never commit real secrets. For example, generate a 32-byte Base64 key with `openssl rand -base64 32`.
- This is a teaching project, not a production-ready identity system. For production, remove public crypto demonstrations and H2 console, use a managed secret store/KMS, add rate limiting, stronger password policy, audit logs, HTTPS, CSRF/CORS review, and robust refresh-token family/reuse handling.
- The refresh-token rotation implementation is intended to demonstrate the lifecycle concept. Concurrent refresh requests and full token-family reuse detection are not implemented.

## Screenshots for your practical record

1. Successful login response with token fields (hide tokens in any public screenshot).
2. Protected endpoint without token: 401.
3. USER access to profile: 200.
4. USER blocked from admin endpoint: 403.
5. ADMIN access to admin endpoint: 200.
6. AES encrypt response showing ciphertext.
7. AES decrypt response showing original demo text.
8. Credential storage response plus H2 row showing ciphertext.
9. Refresh response showing new token pair; old refresh token rejected.
10. Logout and subsequent refresh rejection.

## GitHub

Before pushing, check that no real `.env` file or production secrets are staged. `.env` is ignored by `.gitignore`.
