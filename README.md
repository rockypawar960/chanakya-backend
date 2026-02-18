# Chanakya's Compass - Backend

A production-ready Spring Boot backend for the Chanakya's Compass career guidance platform. This application provides comprehensive APIs for student assessment, career recommendations, learning paths, and resources.

## Features

✅ **Spring Security with JWT Authentication**
- Secure token-based authentication
- Role-based access control (USER, ADMIN)
- Refresh token mechanism
- Stateless API design

✅ **Assessment & Recommendations Engine**
- Multi-question assessment system
- Rule-based career matching algorithm
- Top 3 career recommendations with match percentages
- Detailed reasoning for each recommendation

✅ **Career Management**
- Comprehensive career database
- Career attributes and skill requirements
- Learning paths for each career
- Curated resources and courses

✅ **Production-Ready Architecture**
- Clean layered architecture (Controller → Service → Repository)
- DTO pattern for API contracts
- Global exception handling
- Input validation with annotations
- Comprehensive error responses

✅ **Database**
- MySQL database
- Relational schema with proper constraints
- Sample data included
- Migration-ready structure

✅ **DevOps**
- Docker & Docker Compose setup
- Dev and production configuration profiles
- Swagger/OpenAPI documentation
- CORS configuration

## Tech Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Spring Security**: With JWT
- **Spring Data JPA**: ORM
- **MySQL**: 8.0
- **Docker**: Containerization
- **Swagger/OpenAPI**: API Documentation
- **ModelMapper**: DTO mapping
- **Lombok**: Boilerplate reduction

## Project Structure

```
chanakya-backend/
├── src/main/java/com/chanakya/
│   ├── config/              # Configuration classes
│   ├── controller/          # REST endpoints
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # JPA entities
│   ├── exception/          # Custom exceptions & handlers
│   ├── repository/         # Data access layer
│   ├── security/           # JWT & security components
│   ├── service/            # Business logic
│   └── ChanakvaApplication.java
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
├── sql/
│   └── schema.sql          # Database schema
├── pom.xml                 # Maven configuration
├── Dockerfile              # Docker image
├── docker-compose.yml      # Docker Compose setup
└── postman_collection.json # API endpoints collection
```

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8.0+ OR Docker & Docker Compose
- Git

### Local Setup (Without Docker)

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/chanakya-backend.git
   cd chanakya-backend
   ```

2. **Create MySQL Database**
   ```bash
   mysql -u root -p < sql/schema.sql
   ```

3. **Configure application properties**
   - Edit `src/main/resources/application-dev.properties`
   - Set your MySQL credentials

4. **Build the project**
   ```bash
   mvn clean package
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
   ```

6. **Access the application**
   - API: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html

### Docker Setup (Recommended)

1. **Build and run with Docker Compose**
   ```bash
   docker-compose up --build
   ```

2. **Access the application**
   - API: http://localhost:8080/api
   - Database: localhost:3306

3. **Stop the services**
   ```bash
   docker-compose down
   ```

## API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `POST /auth/refresh` - Refresh access token

### Assessment
- `GET /assessments/questions` - Get all assessment questions
- `POST /assessments/submit` - Submit assessment and get recommendations
- `GET /assessments/my/latest` - Get latest assessment
- `GET /assessments/my/all` - Get all assessments

### Careers
- `GET /careers` - Get all careers
- `GET /careers/{id}` - Get career by ID
- `GET /careers/name/{name}` - Get career by name

### Recommendations
- `GET /recommendations/my` - Get my recommendations
- `GET /recommendations/user/{userId}` - Get user recommendations
- `GET /recommendations/assessment/{assessmentId}` - Get assessment recommendations

### Learning Paths
- `GET /learning-paths/career/{careerId}` - Get learning paths for career
- `GET /learning-paths/{id}` - Get learning path by ID

### Resources
- `GET /resources` - Get all resources
- `GET /resources/career/{careerId}` - Get resources for career
- `GET /resources/{id}` - Get resource by ID

## Authentication

The API uses JWT (JSON Web Tokens) for authentication. 

**How to authenticate:**

1. Register or login to get access token
2. Include token in Authorization header:
   ```
   Authorization: Bearer {access_token}
   ```

**Token Details:**
- Access Token Expiration: 24 hours (configurable)
- Refresh Token Expiration: 7 days (configurable)

## Database Schema

### Key Tables

- **users** - User accounts
- **roles** - User roles (ADMIN, USER)
- **questions** - Assessment questions
- **question_options** - Answer options for questions
- **careers** - Career information
- **assessments** - User assessment results
- **recommendations** - Career recommendations
- **learning_paths** - Learning roadmaps for careers
- **resources** - Educational resources

See `sql/schema.sql` for complete schema.

## Configuration

### Development Profile (`application-dev.properties`)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chanakya_db
spring.datasource.username=root
spring.datasource.password=root
spring.profiles.active=dev
jwt.secret=your-secret-key
jwt.expiration=86400000
```

### Production Profile (`application-prod.properties`)

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
jwt.secret=${JWT_SECRET}
spring.jpa.hibernate.ddl-auto=validate
```

**Environment Variables for Production:**
- `DB_URL` - Database connection URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `JWT_SECRET` - JWT signing key (min 256 bits)
- `CORS_ALLOWED_ORIGINS` - Allowed frontend URLs

## Security Features

### Implemented
- ✅ BCrypt password encryption
- ✅ JWT token validation
- ✅ Spring Security configuration
- ✅ Role-based access control
- ✅ CORS configuration
- ✅ Input validation
- ✅ SQL injection prevention (parameterized queries)
- ✅ Global exception handling

### Recommendations for Production
- Use environment variables for secrets
- Enable HTTPS/TLS
- Implement rate limiting
- Add API key management
- Enable audit logging
- Regular security updates

## Recommendation Algorithm

The career recommendation system uses a rule-based scoring mechanism:

1. **Base Score**: 50%
2. **Popularity Bonus**: Career popularity score × 2
3. **Assessment Influence**: Normalized assessment score (0-50)
4. **Final Score**: Min(100%, sum of all factors)

**Output**: Top 3 recommended careers with:
- Match percentage (0-100%)
- Ranking (primary, alternate, etc.)
- Detailed reasoning

## Testing

### Using Postman

1. Import `postman_collection.json` into Postman
2. Set the following variables:
   - `access_token` - JWT token from login response
   - `refresh_token` - Refresh token from login response
3. Test the endpoints

### Manual Testing with cURL

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@example.com",
    "password":"Password123!",
    "firstName":"John",
    "lastName":"Doe"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"test@example.com",
    "password":"Password123!"
  }'

# Get Careers (no auth required)
curl http://localhost:8080/api/careers

# Submit Assessment (requires token)
curl -X POST http://localhost:8080/api/assessments/submit \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"responses":{"1":10,"2":9,...}}'
```

## Deployment

### Docker Deployment

1. **Build image**
   ```bash
   docker build -t chanakya-backend:1.0.0 .
   ```

2. **Run container**
   ```bash
   docker run -e SPRING_PROFILES_ACTIVE=prod \
     -e DB_URL=jdbc:mysql://db-host:3306/chanakya \
     -e DB_USERNAME=user \
     -e DB_PASSWORD=pass \
     -e JWT_SECRET=your-secret-key \
     -p 8080:8080 \
     chanakya-backend:1.0.0
   ```

### Cloud Deployment (AWS, GCP, Azure)

1. Use managed database services
2. Configure environment variables
3. Enable SSL/TLS
4. Setup monitoring and logging
5. Configure auto-scaling

## Troubleshooting

### Database Connection Error
- Ensure MySQL is running
- Check database credentials
- Verify database exists and schema is initialized

### JWT Token Errors
- Ensure token is included in Authorization header
- Check token hasn't expired
- Verify JWT_SECRET matches across environments

### CORS Errors
- Update `CORS_ALLOWED_ORIGINS` in properties
- Check SecurityConfig CORS settings

### Port Already in Use
```bash
# Kill process on port 8080
lsof -i :8080
kill -9 <PID>
```

## API Documentation

Interactive API documentation available at:
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v3/api-docs`

## Contributing

1. Create feature branch (`git checkout -b feature/amazing-feature`)
2. Commit changes (`git commit -m 'Add amazing feature'`)
3. Push to branch (`git push origin feature/amazing-feature`)
4. Open Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact: support@chanakyas-compass.com

## Roadmap

- [ ] User profile management
- [ ] Assessment history and analytics
- [ ] Advanced recommendation engine
- [ ] Admin dashboard
- [ ] Email notifications
- [ ] Mobile app API optimizations
- [ ] GraphQL API layer
- [ ] API rate limiting
- [ ] WebSocket support for real-time updates

---

**Last Updated**: February 2026
**Version**: 1.0.0
**Status**: Production Ready
