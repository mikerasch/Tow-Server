# HELP API
#####Documentation for HELP API
- API Routes
    - Users

## API Routes
### Authentication

| Resource |   Type   | Return | Method |
|----------|----------|--------|--------|
| POST | /login | ResponseEntity | authenticateUser |
| POST | /registeruser | ResponseEntity | registerUser |

#### POST Login [JWT]
<code>/api/users/login</code>

<code>header{key=email, value=email@email.com}, {key=password, value=password}</code>
```json=
{ 
    'id' : Int,
    'username' : String,
    'email': String,
    'firstName': String,
    'lastName': String,
    'role' : ROLE_STRING
    'tokenType' : String,
    'accessToken' : String
}
```

#### POST Register User
<code>/api/users/registeruser</code>

<code>header {key=firstname, value=firstname}, {key=lastname, value=lastname}, {key=email, value=email@email.com}, 
{key=password, value=password}</code>
```json=
{ 
    'message': "User registered successfully!"
}
```

## WebSockets coming soon
