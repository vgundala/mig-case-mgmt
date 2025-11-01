# Lead Management Service - Comprehensive API Test Report

**Generated:** 2025-10-31 20:43:55
**Base URL:** https://leads-api.ivdata.dev/api

## Test Summary

- **Total Tests:** 22
- **Passed:** 8 (36.4%)
- **Failed:** 14 (63.6%)
- **Errors:** 0 (0.0%)

## Endpoint Test Details

### ✅ GET /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 154.67 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 4,
        "leadName": "Robert Mitchell",
        "company": "GlobalTech Enterprises",
        "email": "robert.mitchell@globaltech.com",
        "phone": "212-555-0101",
        "status": "PRE_CONVERSION",
        "assignedTo": 4,
        "assignedToUsername": "sarah.anderson",
        "assignedToFirstName": "Sarah",
        "assignedToLastName": "Anderson",
        "potentialValue": 250
... (truncated)
```

---

### ✅ GET /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 31.55 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "sorted": false,
        "empty": true,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "last": true,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "size": 20,
    "number": 0,
    "sort": {
      "sorted": false,
      "empty": true,
      "
```

---

### ✅ GET /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 42.08 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 2,
        "leadName": "Jane Doe",
        "company": "XYZ Inc",
        "email": "jane.doe@xyzinc.com",
        "phone": "098-765-4321",
        "status": "ASSIGNED",
        "assignedTo": 2,
        "assignedToUsername": "sales1",
        "assignedToFirstName": "Jane",
        "assignedToLastName": "Smith",
        "potentialValue": 1500000.0,
        "leadSource": "Partner Referral",
 
... (truncated)
```

---

### ✅ GET /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 39.69 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 4,
        "leadName": "Robert Mitchell",
        "company": "GlobalTech Enterprises",
        "email": "robert.mitchell@globaltech.com",
        "phone": "212-555-0101",
        "status": "PRE_CONVERSION",
        "assignedTo": 4,
        "assignedToUsername": "sarah.anderson",
        "assignedToFirstName": "Sarah",
        "assignedToLastName": "Anderson",
        "potentialValue": 250
... (truncated)
```

---

### ✅ GET /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 30.85 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "sorted": false,
        "empty": true,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "last": true,
    "totalElements": 0,
    "totalPages": 0,
    "first": true,
    "size": 20,
    "number": 0,
    "sort": {
      "sorted": false,
      "empty": true,
      "
```

---

### ❌ POST /leads

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 67.75 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ GET /leads/my-leads

- **Expected Status:** 200
- **Actual Status:** 400
- **Response Time:** 22.91 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": null,
  "data": null,
  "error": "User ID is required"
}
```

**Error:** Expected 200, got 400

---

### ✅ GET /leads/new

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 27.52 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": [],
  "error": null
}
```

---

### ❌ GET /leads/high-value

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 34.42 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ✅ GET /leads/distribution-stats

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 34.96 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "activeSalesPersons": 14,
    "newLeadsCount": 0,
    "assignedLeadsCount": 45,
    "totalLeadsCount": 45
  },
  "error": null
}
```

---

### ❌ GET /leads/4

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 29.34 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ PUT /leads/4

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 32.54 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ PUT /leads/4/status

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 41.33 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ POST /leads/4/recalculate-score

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 68.66 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ✅ POST /leads/distribute

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 20.14 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": "Distributed 0 leads",
  "error": null
}
```

---

### ❌ POST /leads/4/escalate

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 39.75 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ POST /leads/4/request-approval

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 22.98 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ POST /leads/4/approve

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 28.26 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ POST /leads/4/reject

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 26.33 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ GET /leads/4/history

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 46.39 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ POST /leads/4/history

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 22.57 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

### ❌ GET /leads/4/history/recent

- **Expected Status:** 200
- **Actual Status:** 500
- **Response Time:** 40.76 ms
- **Test Status:** FAIL

**Response Sample:**

```json
{
  "success": false,
  "message": "Internal server error",
  "data": null,
  "error": "An unexpected error occurred. Please try again later."
}
```

**Error:** Expected 200, got 500

---

## Sample Data Used

The following sample data was used in the test cases:

### Lead Creation Sample

```json
{
  "leadName": "Test Lead [timestamp]",
  "company": "Test Company Inc",
  "email": "testlead_[timestamp]@test.com",
  "phone": "555-9999",
  "potentialValue": 500000.0,
  "leadSource": "Website Signup",
  "description": "Test lead created by automated test script",
  "industry": "Technology",
  "companySize": "Medium",
  "location": "San Francisco, CA"
}
```

### Lead Update Sample

```json
{
  "leadName": "Updated Test Lead",
  "company": "Updated Company Inc",
  "email": "updated@test.com",
  "phone": "555-8888",
  "potentialValue": 750000.0,
  "leadSource": "Webinar",
  "description": "Updated description",
  "industry": "Finance",
  "companySize": "Large",
  "location": "New York, NY"
}
```

### Comment Sample

```json
{
  "commentText": "Test comment from automated test script",
  "action": "Test Action"
}
```

## API Endpoints Tested

| Method | Endpoint | Status | Response Time (ms) |
|--------|----------|--------|-------------------|
| GET | /leads | ✅ 200 | 154.67 |
| GET | /leads | ✅ 200 | 31.55 |
| GET | /leads | ✅ 200 | 42.08 |
| GET | /leads | ✅ 200 | 39.69 |
| GET | /leads | ✅ 200 | 30.85 |
| POST | /leads | ❌ 500 | 67.75 |
| GET | /leads/my-leads | ❌ 400 | 22.91 |
| GET | /leads/new | ✅ 200 | 27.52 |
| GET | /leads/high-value | ❌ 500 | 34.42 |
| GET | /leads/distribution-stats | ✅ 200 | 34.96 |
| GET | /leads/4 | ❌ 500 | 29.34 |
| PUT | /leads/4 | ❌ 500 | 32.54 |
| PUT | /leads/4/status | ❌ 500 | 41.33 |
| POST | /leads/4/recalculate-score | ❌ 500 | 68.66 |
| POST | /leads/distribute | ✅ 200 | 20.14 |
| POST | /leads/4/escalate | ❌ 500 | 39.75 |
| POST | /leads/4/request-approval | ❌ 500 | 22.98 |
| POST | /leads/4/approve | ❌ 500 | 28.26 |
| POST | /leads/4/reject | ❌ 500 | 26.33 |
| GET | /leads/4/history | ❌ 500 | 46.39 |
| POST | /leads/4/history | ❌ 500 | 22.57 |
| GET | /leads/4/history/recent | ❌ 500 | 40.76 |

## Notes

- Tests marked with ⚠ indicate expected behavior (e.g., authentication required, business rules)
- All timestamps are generated dynamically to ensure unique test data
- The comprehensive JSON report with full request/response data is available in `test_report.json`
