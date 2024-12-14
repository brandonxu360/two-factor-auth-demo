# two-factor-auth-demo
A demo full-stack application with two-factor authentication to protect endpoints using a Spring Boot + Spring Security backend and a React frontend.

## Two-Factor Authentication Implementation

This application uses Spring Security with a custom authorization manager to protect endpoints with two-factor authentication (2FA). The two factors of authentication are:
- **Social Login** (OAuth) with Google or Github
- **Time-based One-time Password (TOTP)** with the algorithm specified in RFC 6238.

### Key Components

1. **Security Configuration (`SecurityConfig.java`)**
   - The `SecurityConfig` class configures web security for specific HTTP requests.
   - It sets up a `SecurityFilterChain` that includes a custom CSRF token repository and a custom authorization manager (`MfaAuthorizationManager`).
   - The `MfaAuthorizationManager` ensures that only users who have passed the 2FA check can access protected endpoints.

2. **MfaAuthorizationManager**
   - The `MfaAuthorizationManager` class checks if the user has been authenticated with MFA.
   - This manager ensures that users who have not completed the 2FA process are blocked from accessing protected resources.
   - It bypasses the MFA check if the user does not have MFA enabled and enforces MFA by checking a session attribute `"isTwoFactorAuthenticated"` to determine if the user has completed the 2FA process.

3. **Credential Repository (`TotpCredentialRepository.java`)**
   - This class implements the `ICredentialRepository` interface to store and retrieve TOTP (Time-based One-Time Password) credentials.
   - It manages user secret keys and is responsible for storing and retrieving these keys required for generating TOTP codes.

## Getting Started

### Prerequisites

- Java (21 or higher recommended)
- Node.js (22.11.0 or higher recommended)
- npm

### Backend Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/brandonxu360/two-factor-auth-demo.git
    cd two-factor-auth-demo
    ```

2. Navigate to the backend directory:
    ```bash
    cd backend
    ```

3. Build the project:
    ```bash
    ./gradlew clean build
    ```

4. Run the Spring Boot application:
    ```bash
    ./gradlew bootRun
    ```

### Frontend Setup

1. Navigate to the frontend directory:
    ```bash
    cd frontend
    ```

2. Install dependencies:
    ```bash
    npm install
    ```

3. Start the development server:
    ```bash
    npm run dev
    ```

