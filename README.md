# 🔍 **Course Search Application**

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=for-the-badge&logo=springboot)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-8.11.0-005571?style=for-the-badge&logo=elasticsearch)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java)
![Maven](https://img.shields.io/badge/Maven-3.6+-C71A36?style=for-the-badge&logo=apachemaven)
![Docker](https://img.shields.io/badge/Docker-20.10+-2496ED?style=for-the-badge&logo=docker)

</div>

---

## 📋 **Overview**

A **production-ready** Spring Boot application that provides powerful course search functionality with Elasticsearch integration. Features advanced search capabilities including full-text search, multi-criteria filtering, intelligent autocomplete, and fuzzy search for enhanced user experience.

> 📊 **For detailed assignment completion status and implementation details, please check [ASSIGNMENT_REPORT.md](./ASSIGNMENT_REPORT.md)**

---

## ✨ **Key Features**

<table>
<tr>
<td align="center">🔍</td>
<td><strong>Full-text Search</strong><br/>Search across course titles and descriptions with advanced relevance scoring</td>
</tr>
<tr>
<td align="center">🎯</td>
<td><strong>Multi-criteria Filtering</strong><br/>Filter by age range, category, type, price range, and session dates</td>
</tr>
<tr>
<td align="center">📊</td>
<td><strong>Flexible Sorting</strong><br/>Sort by upcoming sessions, price (ascending/descending), and relevance</td>
</tr>
<tr>
<td align="center">📄</td>
<td><strong>Smart Pagination</strong><br/>Configurable page sizes with efficient result navigation</td>
</tr>
<tr>
<td align="center">💡</td>
<td><strong>Autocomplete Suggestions</strong><br/>Intelligent course title suggestions as you type</td>
</tr>
<tr>
<td align="center">🔤</td>
<td><strong>Fuzzy Search</strong><br/>Typo tolerance for better user experience</td>
</tr>
</table>

---

## 🛠️ **Technology Stack**

<div align="center">

| **Category** | **Technology** | **Version** | **Purpose** |
|:------------:|:-------------:|:-----------:|:----------:|
| **Backend** | Java | 17 | Core Language |
| **Framework** | Spring Boot | 3.2.0 | Application Framework |
| **Search Engine** | Elasticsearch | 8.11.0 | Full-text Search |
| **Data Access** | Spring Data Elasticsearch | Latest | Data Layer |
| **Containerization** | Docker & Docker Compose | Latest | Container Management |
| **Build Tool** | Maven | 3.6+ | Dependency Management |
| **Development** | Lombok | Latest | Code Generation |

</div>

---

## 📋 **Prerequisites**

> **Before you begin, ensure you have the following installed:**

- ☕ **Java 17 or higher**
- 🔧 **Maven 3.6+**
- 🐳 **Docker and Docker Compose**

## 🚀 **Getting Started**

> 💡 **Quick Tip:** For complete assignment evaluation guide and testing scenarios, see [ASSIGNMENT_REPORT.md](./ASSIGNMENT_REPORT.md)

### **Step 1: Start Elasticsearch**

<details>
<summary><strong>🐳 Launch Elasticsearch with Docker</strong></summary>

First, start the Elasticsearch server using Docker Compose:

```bash
docker-compose up -d
```

**Verify Elasticsearch is running:**

```bash
curl http://localhost:9200
```

**Expected Response:**
```json
{
  "name" : "elasticsearch",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "...",
  "version" : {
    "number" : "8.11.0",
    ...
  }
}
```

</details>

---

### **Step 2: Build and Run Application**

<details>
<summary><strong>🔧 Build & Execute Options</strong></summary>

**Option A: Using Maven**
```bash
# Build the application
mvn clean package

# Run the application
mvn spring-boot:run
```

**Option B: Using JAR file**
```bash
java -jar target/course-search-1.0.0.jar
```

**✅ Application will start on:** `http://localhost:8080`

</details>

---

### **Step 3: Data Initialization**

<details>
<summary><strong>📊 Sample Data Loading</strong></summary>

The application automatically loads **50+ sample courses** from `src/main/resources/sample-courses.json` on startup, including:

- 📚 **Diverse Categories**: Technology, Science, Math, Language, Arts
- 🎯 **Multiple Types**: COURSE, ONE_TIME, CLUB
- 👥 **Age Ranges**: 5-65 years
- 💰 **Price Ranges**: Free to $500

**Verify data loading:**
```bash
curl "http://localhost:8080/api/search?size=0"
```

</details>

## 🌐 **API Endpoints**

### 🔍 **Search Courses**

<div align="center">

| **Method** | **Endpoint** | **Description** |
|:----------:|:------------:|:----------------|
| `GET` | `/api/search` | Main search endpoint with filters |
| `GET` | `/api/search/suggest` | Autocomplete suggestions |
| `GET` | `/api/health` | Health check endpoint |

</div>

---

#### **Primary Search Endpoint**

```http
GET /api/search
```

<details>
<summary><strong>📋 Request Parameters</strong></summary>

| **Parameter** | **Type** | **Required** | **Description** |
|:-------------:|:--------:|:------------:|:----------------|
| `q` | String | ❌ | Search keyword for title and description |
| `minAge` | Integer | ❌ | Minimum age filter |
| `maxAge` | Integer | ❌ | Maximum age filter |
| `category` | String | ❌ | Course category filter |
| `type` | String | ❌ | Course type filter (ONE_TIME, COURSE, CLUB) |
| `minPrice` | Double | ❌ | Minimum price filter |
| `maxPrice` | Double | ❌ | Maximum price filter |
| `startDate` | ISO-8601 | ❌ | Start date filter |
| `sort` | String | ❌ | Sort order: `upcoming`, `priceAsc`, `priceDesc` |
| `page` | Integer | ❌ | Page number (default: 0) |
| `size` | Integer | ❌ | Page size (default: 10) |

</details>

<details>
<summary><strong>📤 Response Format</strong></summary>

```json
{
  "total": 50,
  "courses": [
    {
      "id": "1",
      "title": "Introduction to Mathematics",
      "description": "Basic mathematics concepts for beginners...",
      "category": "Math",
      "type": "COURSE",
      "gradeRange": "1st-3rd",
      "minAge": 6,
      "maxAge": 9,
      "price": 120.50,
      "nextSessionDate": "2025-01-15T09:00:00Z"
    }
  ]
}
```

</details>

---

#### **Autocomplete Suggestions**

```http
GET /api/search/suggest
```

<details>
<summary><strong>💡 Suggestion Parameters & Response</strong></summary>

**Parameters:**
- `q` - Partial course title for suggestions

**Response:**
```json
[
  "Introduction to Mathematics",
  "Advanced Mathematics", 
  "Statistics and Data Analysis"
]
```

</details>

## 🧪 **API Testing Examples**

### 🔍 **Basic Search Examples**

<details>
<summary><strong>📋 Core Search Operations</strong></summary>

| **Operation** | **Command** | **Description** |
|:-------------:|:------------|:----------------|
| **All Courses** | `curl "http://localhost:8080/api/search"` | Retrieve all available courses |
| **Keyword Search** | `curl "http://localhost:8080/api/search?q=math"` | Search by keyword |
| **Fuzzy Search** | `curl "http://localhost:8080/api/search?q=dinors"` | Typo tolerance (matches "Dinosaurs") |
| **Category Filter** | `curl "http://localhost:8080/api/search?category=Science"` | Filter by category |
| **Type Filter** | `curl "http://localhost:8080/api/search?type=COURSE"` | Filter by course type |
| **Age Range** | `curl "http://localhost:8080/api/search?minAge=10&maxAge=15"` | Filter by age range |
| **Price Range** | `curl "http://localhost:8080/api/search?minPrice=100&maxPrice=200"` | Filter by price range |
| **Date Filter** | `curl "http://localhost:8080/api/search?startDate=2025-02-01T00:00:00Z"` | Filter by start date |

</details>

---

### 📊 **Sorting Examples**

<details>
<summary><strong>🔢 Sort Operations</strong></summary>

```bash
# Sort by price (ascending)
curl "http://localhost:8080/api/search?sort=priceAsc"

# Sort by price (descending)  
curl "http://localhost:8080/api/search?sort=priceDesc"

# Sort by upcoming sessions (default)
curl "http://localhost:8080/api/search?sort=upcoming"
```

</details>

---

### 📄 **Pagination Examples**

<details>
<summary><strong>📋 Page Navigation</strong></summary>

```bash
# Get second page with 5 items
curl "http://localhost:8080/api/search?page=1&size=5"

# Get specific page of science courses
curl "http://localhost:8080/api/search?category=Science&page=0&size=3"
```

</details>

---

### 🎯 **Advanced Combined Filters**

<details>
<summary><strong>🔍 Complex Search Scenarios</strong></summary>

```bash
# Science courses for teenagers, sorted by price
curl "http://localhost:8080/api/search?category=Science&minAge=13&maxAge=18&sort=priceAsc"

# Affordable courses under $100
curl "http://localhost:8080/api/search?maxPrice=100&sort=priceAsc"

# Math courses starting after January 20, 2025
curl "http://localhost:8080/api/search?category=Math&startDate=2025-01-20T00:00:00Z"
```

</details>

---

### 💡 **Autocomplete Examples**

<details>
<summary><strong>🔤 Suggestion Testing</strong></summary>

```bash
# Get suggestions for "phy"
curl "http://localhost:8080/api/search/suggest?q=phy"

# Get suggestions for "art"
curl "http://localhost:8080/api/search/suggest?q=art"

# Get suggestions for "comp"
curl "http://localhost:8080/api/search/suggest?q=comp"
```

</details>

---

### 🏥 **Health Check**

<details>
<summary><strong>📊 System Status Monitoring</strong></summary>

```bash
curl "http://localhost:8080/api/health"
```

**Expected Response:**
```json
{
  "status": "UP",
  "elasticsearch": "UP", 
  "timestamp": 1642678800000
}
```

</details>

## 🧪 **Testing**

<details>
<summary><strong>🔬 Run Test Suite</strong></summary>

The application includes comprehensive tests. Execute them using:

```bash
mvn test
```

**Test Coverage:**
- ✅ Unit Tests for Services
- ✅ Integration Tests for Controllers
- ✅ Repository Tests
- ✅ API Endpoint Tests

</details>

---

## ⚙️ **Configuration**

<details>
<summary><strong>🔧 Application Configuration</strong></summary>

Configure the application via `src/main/resources/application.yml`:

```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    socket-timeout: 10s
    connection-timeout: 5s

server:
  port: 8080
  
logging:
  level:
    com.example.coursesearch: INFO
    org.springframework.data.elasticsearch: DEBUG
```

**Key Configuration Options:**
- **Server Port**: Default 8080
- **Elasticsearch URL**: localhost:9200
- **Timeout Settings**: Connection and socket timeouts
- **Logging Levels**: Configurable per package

</details>

---

## 📊 **Sample Data**

<details>
<summary><strong>📚 Dataset Overview</strong></summary>

The application includes **50+ sample courses** with diverse characteristics:

<div align="center">

| **Attribute** | **Range** | **Examples** |
|:-------------:|:---------:|:-------------|
| **Categories** | 8 types | Math, Science, Art, Technology, Music, Language |
| **Types** | 3 types | COURSE, ONE_TIME, CLUB |
| **Age Ranges** | 5-65 years | Kindergarten to Adult |
| **Prices** | $25-$320 | Free to Premium |
| **Sessions** | Jan-Mar 2025 | Upcoming dates |

</div>

</details>

## 📁 **Project Architecture**

<details>
<summary><strong>🏗️ Detailed Project Structure</strong></summary>

```
Spring Boot Elasticsearch/
├── 📂 src/
│   ├── 📂 main/
│   │   ├── 📂 java/com/example/coursesearch/
│   │   │   ├── 📂 controller/
│   │   │   │   ├── 🌐 SearchController.java      # Main API endpoints
│   │   │   │   └── 🏥 HealthController.java      # Health check
│   │   │   ├── 📂 service/
│   │   │   │   └── ⚙️ CourseService.java         # Business logic
│   │   │   ├── 📂 repository/
│   │   │   │   └── 🗄️ CourseRepository.java      # Data access
│   │   │   ├── 📂 document/
│   │   │   │   └── 📄 CourseDocument.java        # Elasticsearch mapping
│   │   │   ├── 📂 dto/
│   │   │   │   ├── 📝 SearchRequest.java         # Request DTOs
│   │   │   │   ├── 📤 SearchResponse.java        # Response DTOs
│   │   │   │   ├── 📚 CourseResponseDto.java     # Course data
│   │   │   │   └── 💡 SuggestionDto.java         # Autocomplete data
│   │   │   ├── 📂 config/
│   │   │   │   └── 🔧 ElasticsearchIndexConfig.java # ES configuration
│   │   │   ├── 📂 exception/
│   │   │   │   └── ⚠️ GlobalExceptionHandler.java # Error handling
│   │   │   └── 🚀 CourseSearchApplication.java    # Main application
│   │   └── 📂 resources/
│   │       ├── ⚙️ application.yml                 # Configuration
│   │       ├── 📋 logback-spring.xml             # Logging config
│   │       └── 📊 sample-courses.json           # Sample data
│   └── 📂 test/
│       └── 📂 java/com/example/coursesearch/     # Test classes
├── 🐳 docker-compose.yml                          # Elasticsearch setup
├── 📦 pom.xml                                     # Maven dependencies
├── 📖 README.md                                   # Documentation
└── 📋 ASSIGNMENT_REPORT.md                        # Assignment completion report
```

**Layer Architecture:**
- **🌐 Controller Layer**: REST API endpoints
- **⚙️ Service Layer**: Business logic and search operations
- **🗄️ Repository Layer**: Data access and Elasticsearch queries
- **📄 Document Layer**: Elasticsearch document mappings
- **📝 DTO Layer**: Data transfer objects for API

</details>

---

## 🔧 **Configuration Overview**

<details>
<summary><strong>⚙️ Application Configuration</strong></summary>

<div align="center">

| **Setting** | **Value** | **Description** |
|:------------|:---------:|:----------------|
| **Server Port** | 8080 | Application port |
| **Elasticsearch URL** | localhost:9200 | Search engine endpoint |
| **Index Name** | courses | Elasticsearch index |
| **Default Page Size** | 10 | Results per page |
| **Maximum Page Size** | 100 | Max results per page |

</div>

**Sample Data Summary:**
- 📚 **Total Courses**: 50+
- 📂 **Categories**: Technology, Science, Mathematics, Language, Arts
- 🏷️ **Types**: ONE_TIME, COURSE, CLUB
- 👥 **Age Ranges**: 5-65 years
- 💰 **Price Ranges**: Free to $500

</details>

---

## 🏛️ **Clean Architecture Pattern**

<details>
<summary><strong>📐 Architecture Overview</strong></summary>

The application follows a **clean architecture pattern** with clear separation of concerns:

```
🏗️ src/main/java/com/example/coursesearch/
├── 🔧 config/          # Elasticsearch configuration
├── 🌐 controller/      # REST controllers
├── 📄 document/        # Elasticsearch document entities
├── 📝 dto/            # Data transfer objects
├── 🗄️ repository/     # Data access layer
└── ⚙️ service/        # Business logic layer
```

**Benefits:**
- ✅ Clear separation of concerns
- ✅ Testable components
- ✅ Maintainable codebase
- ✅ Scalable architecture

</details>

---

## 🔧 **Troubleshooting Guide**

<details>
<summary><strong>🚨 Common Issues & Solutions</strong></summary>

### **🐳 Elasticsearch Issues**

| **Problem** | **Solution** |
|:------------|:-------------|
| **Elasticsearch not starting** | Ensure Docker is running, check port 9200 availability |
| **Connection refused** | Verify Elasticsearch: `curl http://localhost:9200` |
| **No search results** | Check data loading: `curl "http://localhost:8080/api/search?size=0"` |

### **📋 Quick Fixes**

```bash
# Restart Elasticsearch
docker-compose down && docker-compose up -d

# Check Elasticsearch logs
docker-compose logs elasticsearch

# Verify application health
curl "http://localhost:8080/api/health"
```

### **🔍 Debug Logging**

Enable detailed logging in `application.yml`:

```yaml
logging:
  level:
    com.example.coursesearch: DEBUG
    org.springframework.data.elasticsearch: DEBUG
```

</details>

---

## 🚀 **Future Enhancements**

<details>
<summary><strong>🔮 Potential Improvements</strong></summary>

<div align="center">

| **Feature** | **Description** | **Priority** |
|:------------|:----------------|:------------:|
| **Enhanced Autocomplete** | Elasticsearch completion suggester | 🔥 High |
| **Geolocation Search** | Location-based filtering | 🔥 High |
| **User Reviews** | Course ratings and reviews | 🟡 Medium |
| **Real-time Updates** | WebSocket integration | 🟡 Medium |
| **Advanced Analytics** | Search analytics dashboard | 🟡 Medium |
| **Caching Layer** | Redis integration | 🟢 Low |
| **Security** | Authentication & authorization | 🟢 Low |
| **API Documentation** | OpenAPI/Swagger integration | 🟢 Low |

</div>

</details>

---

## 📜 **License**

<div align="center">

**📚 Educational Project**

This project is created for educational purposes as part of a technical assignment.

</div>

---

## 📋 **Assignment Information**

<div align="center">

**📊 For comprehensive assignment completion report, implementation details, and evaluation metrics:**

### **👉 [View ASSIGNMENT_REPORT.md](./ASSIGNMENT_REPORT.md)**

*This report contains detailed information about:*
- ✅ **Assignment A & B completion status**
- 🏗️ **Technical implementation details**
- 🧪 **Testing scenarios and examples**
- 📊 **Quality assurance metrics**
- 🎯 **Success criteria fulfillment**

</div>

---

<div align="center">

**� Made with ❤️ using Spring Boot & Elasticsearch**

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Elasticsearch](https://img.shields.io/badge/Elasticsearch-005571?style=flat-square&logo=elasticsearch&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=java&logoColor=white)

</div>