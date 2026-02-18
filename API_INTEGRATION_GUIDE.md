# Chanakya's Compass - API Integration Guide

Complete guide for integrating the React frontend with the Spring Boot backend.

---

## Quick Reference

### Base URL
```
Development: http://localhost:8080/api
Production: https://api.yourdomain.com
```

### Authentication
All protected endpoints require:
```
Authorization: Bearer {access_token}
Content-Type: application/json
```

---

## Frontend Setup

### 1. Environment Variables

Create `.env` or `.env.local` in your React project:

```env
# .env.development
VITE_API_URL=http://localhost:8080/api
VITE_API_TIMEOUT=10000

# .env.production
VITE_API_URL=https://api.chanakya-compass.com
VITE_API_TIMEOUT=10000
```

### 2. API Client Setup

Create `src/lib/apiClient.ts`:

```typescript
import axios, { AxiosInstance, AxiosError } from 'axios';

const apiClient: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle token refresh
apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as any;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refresh_token');
        const response = await apiClient.post('/auth/refresh', null, {
          headers: { Authorization: `Bearer ${refreshToken}` },
        });

        const { accessToken, refreshToken: newRefreshToken } = response.data.data;
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', newRefreshToken);

        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return apiClient(originalRequest);
      } catch (err) {
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        window.location.href = '/login';
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);

export default apiClient;
```

---

## API Endpoints & Usage Examples

### Authentication

#### Register User
```typescript
POST /auth/register

Request:
{
  "email": "user@example.com",
  "password": "Password123!",
  "firstName": "John",
  "lastName": "Doe",
  "classOrYear": "12th Grade",
  "stream": "Science",
  "interests": "Technology, AI"
}

Response:
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "USER"
  },
  "status": 201
}

Usage in React:
const register = async (formData) => {
  const response = await apiClient.post('/auth/register', formData);
  const { accessToken, refreshToken } = response.data.data;
  localStorage.setItem('access_token', accessToken);
  localStorage.setItem('refresh_token', refreshToken);
  return response.data.data;
};
```

#### Login User
```typescript
POST /auth/login

Request:
{
  "email": "user@example.com",
  "password": "Password123!"
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "...",
    "refreshToken": "...",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "USER"
  },
  "status": 200
}

Usage in React:
const login = async (email, password) => {
  const response = await apiClient.post('/auth/login', { email, password });
  const { accessToken, refreshToken } = response.data.data;
  localStorage.setItem('access_token', accessToken);
  localStorage.setItem('refresh_token', refreshToken);
  return response.data.data;
};
```

#### Refresh Token
```typescript
POST /auth/refresh

Headers:
Authorization: Bearer {refresh_token}

Response:
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "...",
    "refreshToken": "...",
    "userId": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "USER"
  },
  "status": 200
}
```

---

### Assessment

#### Get All Questions
```typescript
GET /assessments/questions

No authentication required

Response:
{
  "success": true,
  "message": "Questions retrieved successfully",
  "data": [
    {
      "id": 1,
      "questionText": "How interested are you in solving complex problems?",
      "questionType": "RATING",
      "sequenceNumber": 1,
      "options": [
        {
          "id": 1,
          "optionText": "Not at all",
          "optionValue": 1,
          "sequenceNumber": 1
        },
        {
          "id": 2,
          "optionText": "Somewhat",
          "optionValue": 5,
          "sequenceNumber": 2
        },
        {
          "id": 3,
          "optionText": "Very much",
          "optionValue": 10,
          "sequenceNumber": 3
        }
      ]
    },
    // ... more questions
  ],
  "status": 200
}

Usage in React:
const fetchQuestions = async () => {
  const response = await apiClient.get('/assessments/questions');
  return response.data.data; // Array of questions
};
```

#### Submit Assessment
```typescript
POST /assessments/submit

Headers:
Authorization: Bearer {access_token}

Request:
{
  "responses": {
    "1": 10,
    "2": 9,
    "3": "In teams",
    "4": "Mathematics",
    "5": 10,
    "6": 8,
    "7": 9,
    "8": "Hybrid"
  }
}

Response:
{
  "success": true,
  "message": "Assessment submitted successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "totalScore": 73,
    "rawResponses": "{...}",
    "completedAt": "2024-02-14T10:30:00",
    "createdAt": "2024-02-14T10:30:00"
  },
  "status": 201
}

Usage in React:
const submitAssessment = async (responses) => {
  const response = await apiClient.post('/assessments/submit', {
    responses
  });
  return response.data.data;
};
```

#### Get Latest Assessment
```typescript
GET /assessments/my/latest

Headers:
Authorization: Bearer {access_token}

Response:
{
  "success": true,
  "message": "Assessment retrieved successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "totalScore": 73,
    "rawResponses": "{...}",
    "completedAt": "2024-02-14T10:30:00",
    "createdAt": "2024-02-14T10:30:00"
  },
  "status": 200
}
```

---

### Careers

#### Get All Careers
```typescript
GET /careers

No authentication required

Response:
{
  "success": true,
  "message": "Careers retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Software Engineer",
      "description": "Develops software solutions and applications",
      "requiredSkills": "Java,Python,JavaScript,Problem Solving",
      "jobScope": "Backend, Frontend, Full Stack Development",
      "salaryRange": "₹10 LPA - ₹30 LPA",
      "educationPath": "B.Tech/B.Sc in Computer Science",
      "topCompanies": "Google, Microsoft, Amazon",
      "popularityScore": 9
    },
    // ... more careers
  ],
  "status": 200
}

Usage in React:
const fetchCareers = async () => {
  const response = await apiClient.get('/careers');
  return response.data.data; // Array of careers
};
```

#### Get Career By ID
```typescript
GET /careers/{id}

No authentication required

Example: GET /careers/1

Response:
{
  "success": true,
  "message": "Career retrieved successfully",
  "data": {
    "id": 1,
    "name": "Software Engineer",
    // ... career details
  },
  "status": 200
}

Usage in React:
const fetchCareer = async (careerId) => {
  const response = await apiClient.get(`/careers/${careerId}`);
  return response.data.data;
};
```

---

### Recommendations

#### Get My Recommendations
```typescript
GET /recommendations/my

Headers:
Authorization: Bearer {access_token}

Response:
{
  "success": true,
  "message": "Your recommendations retrieved successfully",
  "data": [
    {
      "id": 1,
      "careerId": 1,
      "careerName": "Software Engineer",
      "careerDescription": "Develops software solutions...",
      "matchPercentage": 85,
      "rank": 1,
      "reasoning": "Based on your assessment responses, we've calculated an 85% match for Software Engineer..."
    },
    {
      "id": 2,
      "careerId": 2,
      "careerName": "Data Scientist",
      "careerDescription": "Analyzes data and builds...",
      "matchPercentage": 72,
      "rank": 2,
      "reasoning": "..."
    },
    {
      "id": 3,
      "careerId": 5,
      "careerName": "DevOps Engineer",
      "careerDescription": "Manages infrastructure...",
      "matchPercentage": 65,
      "rank": 3,
      "reasoning": "..."
    }
  ],
  "status": 200
}

Usage in React:
const fetchMyRecommendations = async () => {
  const response = await apiClient.get('/recommendations/my');
  return response.data.data; // Array of recommendations
};
```

#### Get Recommendations for Assessment
```typescript
GET /recommendations/assessment/{assessmentId}

Headers:
Authorization: Bearer {access_token}

Example: GET /recommendations/assessment/1

Response:
Same as "Get My Recommendations"

Usage in React:
const fetchAssessmentRecommendations = async (assessmentId) => {
  const response = await apiClient.get(`/recommendations/assessment/${assessmentId}`);
  return response.data.data;
};
```

---

### Learning Paths

#### Get Learning Paths for Career
```typescript
GET /learning-paths/career/{careerId}

No authentication required

Example: GET /learning-paths/career/1

Response:
{
  "success": true,
  "message": "Learning paths retrieved successfully",
  "data": [
    {
      "id": 1,
      "careerId": 1,
      "pathName": "Foundation Programming",
      "description": "Learn the basics of programming and data structures",
      "skills": "Java,Python,OOP,Data Structures",
      "resources": "Online Courses, Books",
      "sequenceNumber": 1,
      "durationMonths": 3
    },
    {
      "id": 2,
      "careerId": 1,
      "pathName": "Web Development",
      "description": "Master frontend and backend technologies",
      "skills": "JavaScript,React,Node.js,REST APIs",
      "sequenceNumber": 2,
      "durationMonths": 4
    },
    {
      "id": 3,
      "careerId": 1,
      "pathName": "Advanced Topics",
      "description": "System design and advanced concepts",
      "skills": "System Design,Microservices,Architecture",
      "sequenceNumber": 3,
      "durationMonths": 3
    }
  ],
  "status": 200
}

Usage in React:
const fetchLearningPaths = async (careerId) => {
  const response = await apiClient.get(`/learning-paths/career/${careerId}`);
  return response.data.data; // Array of learning paths
};
```

---

### Resources

#### Get All Resources
```typescript
GET /resources

No authentication required

Response:
{
  "success": true,
  "message": "Resources retrieved successfully",
  "data": [
    {
      "id": 1,
      "careerId": 1,
      "title": "The Complete Java Developer Course",
      "description": "Comprehensive Java programming course",
      "resourceType": "COURSE",
      "url": "https://www.udemy.com/course/complete-java",
      "provider": "Udemy",
      "difficulty": "BEGINNER",
      "estimatedDuration": "40 hours"
    },
    // ... more resources
  ],
  "status": 200
}

Usage in React:
const fetchAllResources = async () => {
  const response = await apiClient.get('/resources');
  return response.data.data;
};
```

#### Get Resources for Career
```typescript
GET /resources/career/{careerId}

No authentication required

Example: GET /resources/career/1

Response:
{
  "success": true,
  "message": "Resources retrieved successfully",
  "data": [
    // Array of resources for the career
  ],
  "status": 200
}

Usage in React:
const fetchCareerResources = async (careerId) => {
  const response = await apiClient.get(`/resources/career/${careerId}`);
  return response.data.data;
};
```

---

## Complete Integration Example

### Home Page Component
```typescript
import { useEffect, useState } from 'react';
import apiClient from '@/lib/apiClient';

export default function HomePage() {
  const [careers, setCareers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCareers = async () => {
      try {
        const response = await apiClient.get('/careers');
        setCareers(response.data.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchCareers();
  }, []);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <h1>Explore Careers</h1>
      {careers.map((career) => (
        <div key={career.id}>
          <h2>{career.name}</h2>
          <p>{career.description}</p>
        </div>
      ))}
    </div>
  );
}
```

### Assessment Page Component
```typescript
import { useEffect, useState } from 'react';
import apiClient from '@/lib/apiClient';
import { useNavigate } from 'react-router-dom';

export default function AssessmentPage() {
  const [questions, setQuestions] = useState([]);
  const [responses, setResponses] = useState({});
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchQuestions = async () => {
      try {
        const response = await apiClient.get('/assessments/questions');
        setQuestions(response.data.data);
      } catch (err) {
        console.error('Error fetching questions:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchQuestions();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await apiClient.post('/assessments/submit', {
        responses,
      });
      // Redirect to recommendations page
      navigate('/recommendations');
    } catch (err) {
      console.error('Error submitting assessment:', err);
    }
  };

  if (loading) return <div>Loading questions...</div>;

  return (
    <form onSubmit={handleSubmit}>
      {questions.map((question) => (
        <div key={question.id}>
          <label>{question.questionText}</label>
          {question.questionType === 'RATING' && (
            <input
              type="range"
              min="1"
              max="10"
              value={responses[question.id] || 5}
              onChange={(e) =>
                setResponses({
                  ...responses,
                  [question.id]: parseInt(e.target.value),
                })
              }
            />
          )}
          {question.questionType === 'MULTIPLE_CHOICE' && (
            <select
              value={responses[question.id] || ''}
              onChange={(e) =>
                setResponses({
                  ...responses,
                  [question.id]: e.target.value,
                })
              }
            >
              {question.options.map((option) => (
                <option key={option.id} value={option.optionText}>
                  {option.optionText}
                </option>
              ))}
            </select>
          )}
        </div>
      ))}
      <button type="submit">Submit Assessment</button>
    </form>
  );
}
```

### Recommendations Page Component
```typescript
import { useEffect, useState } from 'react';
import apiClient from '@/lib/apiClient';
import { useNavigate } from 'react-router-dom';

export default function RecommendationsPage() {
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRecommendations = async () => {
      try {
        const response = await apiClient.get('/recommendations/my');
        setRecommendations(response.data.data);
      } catch (err) {
        console.error('Error fetching recommendations:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchRecommendations();
  }, []);

  if (loading) return <div>Loading recommendations...</div>;

  return (
    <div>
      <h1>Your Career Recommendations</h1>
      {recommendations.map((rec) => (
        <div key={rec.id}>
          <h2>
            #{rec.rank} - {rec.careerName} ({rec.matchPercentage}% match)
          </h2>
          <p>{rec.reasoning}</p>
          <button
            onClick={() => navigate(`/career/${rec.careerId}`)}
          >
            View Details
          </button>
        </div>
      ))}
    </div>
  );
}
```

---

## Error Handling

### Standard Error Response
```typescript
{
  "success": false,
  "message": "Error description",
  "status": 400
}

// Validation errors include field-specific errors:
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Email must be valid",
    "password": "Password must be at least 8 characters"
  },
  "status": 400
}
```

### Handle Errors in Frontend
```typescript
try {
  const response = await apiClient.post('/endpoint', data);
  // Handle success
} catch (error) {
  if (error.response?.status === 400) {
    // Handle validation errors
    console.log(error.response.data.data);
  } else if (error.response?.status === 401) {
    // Handle unauthorized - auto refresh or redirect to login
  } else if (error.response?.status === 404) {
    // Handle not found
  } else {
    // Handle other errors
  }
}
```

---

## CORS & Headers

### Default Headers (automatically set)
```
Content-Type: application/json
Authorization: Bearer {access_token} (for protected routes)
```

### Frontend CORS Configuration
If you get CORS errors, ensure your backend's `application.properties` includes:
```
cors.allowed-origins=http://localhost:5173,http://localhost:3000
cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
cors.allow-credentials=true
```

---

## Token Management

### Store Tokens
```typescript
// After login/register
const { accessToken, refreshToken } = response.data.data;
localStorage.setItem('access_token', accessToken);
localStorage.setItem('refresh_token', refreshToken);
```

### Retrieve Tokens
```typescript
const accessToken = localStorage.getItem('access_token');
const refreshToken = localStorage.getItem('refresh_token');
```

### Clear Tokens (on logout)
```typescript
localStorage.removeItem('access_token');
localStorage.removeItem('refresh_token');
```

---

## Testing the Integration

### 1. Test with Postman
1. Import `postman_collection.json`
2. Register/Login to get tokens
3. Set tokens in Postman variables
4. Test protected endpoints

### 2. Test with Frontend
```bash
# Start backend
docker-compose up

# Start frontend
npm run dev

# Navigate to http://localhost:5173
```

---

## Troubleshooting

### 401 Unauthorized
- Check if access token is expired
- Refresh token using `/auth/refresh`
- Re-login if refresh fails

### 403 Forbidden
- User doesn't have required role
- Check user's role in database

### 404 Not Found
- Check endpoint URL is correct
- Verify resource ID exists

### 500 Internal Server Error
- Check backend logs
- Verify database is running
- Check JWT secret configuration

---

## Performance Considerations

1. **Token Caching**: Tokens are cached by the axios interceptor
2. **Request Timeout**: Default 10 seconds (configurable via `VITE_API_TIMEOUT`)
3. **Pagination**: Not yet implemented, add as needed
4. **Caching**: Consider implementing React Query or SWR

---

## Security Best Practices

1. ✅ Never expose tokens in URL
2. ✅ Use HTTPS in production
3. ✅ Store tokens securely (httpOnly cookies recommended)
4. ✅ Implement CSRF protection
5. ✅ Validate all inputs
6. ✅ Use environment variables for API URLs

---

## What's Next?

1. Implement user profile endpoints
2. Add assessment history tracking
3. Implement progress tracking for learning paths
4. Add resource bookmarking
5. Implement admin dashboard
6. Add email notifications
7. Implement pagination for large datasets

---

**For more details, see the main README.md file.**
