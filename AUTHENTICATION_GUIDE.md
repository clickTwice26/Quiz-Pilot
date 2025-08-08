# QuizPilot API Authentication Guide

## Overview
All MainController endpoints (`/api/*`) are now secured and require a valid session token for access. Users must first login to obtain a session token, then use this token in subsequent requests.

## Authentication Flow

### 1. User Registration
```http
POST /api/users/register
Content-Type: application/json

{
    "username": "johndoe",
    "emailAddress": "john@example.com",
    "fullname": "John Doe",
    "password": "securePassword123"
}
```

### 2. User Login
```http
POST /api/users/login
Content-Type: application/json

{
    "emailAddress": "john@example.com",
    "password": "securePassword123"
}
```

**Response:**
```json
{
    "statusCode": "200",
    "message": "Login successful",
    "emailAddress": "john@example.com",
    "sessionToken": "session_abc123def456..."
}
```

### 3. Using Secured Endpoints
After login, use the `sessionToken` in the Authorization header for all MainController endpoints:

```http
GET /api/health
Authorization: Bearer session_abc123def456...
```

## Secured Endpoints

All MainController endpoints now return an `AuthenticatedResponse` structure:

```json
{
    "status": "200",
    "message": "Success message",
    "user": {
        "id": 1,
        "username": "johndoe",
        "emailAddress": "john@example.com",
        "fullname": "John Doe",
        "userRole": "STUDENT"
    },
    "data": {
        // Endpoint-specific data
    }
}
```

### Available Secured Endpoints:

1. **GET /api/health** - Health check with user verification
2. **GET /api/info** - Application information with user verification
3. **GET /api/** - Welcome message with user verification
4. **GET /api/endpoints** - Endpoints list with user verification
5. **GET /api/status** - Service status with user verification

## Error Responses

### 401 Unauthorized
```json
{
    "status": "401",
    "message": "Invalid or missing token",
    "user": null,
    "data": null
}
```

## Testing with Swagger UI

1. Go to `http://localhost:8081/swagger-ui.html`
2. First, use the `/api/users/login` endpoint to get a session token
3. Copy the session token from the response
4. For secured endpoints, click the "Authorize" button or add the token manually:
   - Header: `Authorization`
   - Value: `Bearer YOUR_SESSION_TOKEN_HERE`

## Implementation Details

- **AuthService**: Handles token verification and extraction
- **UserRepository**: Extended with `findBySessionToken()` method
- **AuthenticatedResponse**: Generic response wrapper that includes user info
- **Token Format**: `session_` + random 192-character string
- **Token Storage**: Stored in the `sessionToken` field of the User entity

## Security Features

- Session tokens are randomly generated (192 characters)
- Tokens are linked to specific users
- Invalid/missing tokens return 401 Unauthorized
- User passwords are never exposed in API responses
- Each endpoint verifies token validity before processing
