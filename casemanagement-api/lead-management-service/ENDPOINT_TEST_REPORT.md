# Lead Management Service - Comprehensive API Test Report

**Generated:** 2025-10-31 20:48:32
**Base URL:** https://leads-api.ivdata.dev/api

## Test Summary

- **Total Tests:** 22
- **Passed:** 22 (100.0%)
- **Failed:** 0 (0.0%)
- **Errors:** 0 (0.0%)

## Endpoint Test Details

### ✅ GET /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 345.58 ms
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
- **Response Time:** 69.76 ms
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
- **Response Time:** 88.27 ms
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
- **Response Time:** 67.44 ms
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
- **Response Time:** 105.24 ms
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

### ✅ POST /leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 463.79 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Lead created successfully",
  "data": {
    "id": 117,
    "leadName": "Test Lead 1761958109",
    "company": "Test Company Inc",
    "email": "testlead_1761958109@test.com",
    "phone": "555-9999",
    "status": "NEW",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 500000.0,
    "leadSource": "Website Signup",
    "leadScore": 45,
    "createdDate": "2025-11-01T00:
... (truncated)
```

---

### ✅ GET /leads/my-leads

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 37.11 ms
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

### ✅ GET /leads/new

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 48.14 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "id": 117,
      "leadName": "Test Lead 1761958109",
      "company": "Test Company Inc",
      "email": "testlead_1761958109@test.com",
      "phone": "555-9999",
      "status": "NEW",
      "assignedTo": null,
      "assignedToUsername": null,
      "assignedToFirstName": null,
      "assignedToLastName": null,
      "potentialValue": 500000.0,
      "leadSource": "Website Signup",
      "leadScore": 45,
      "
... (truncated)
```

---

### ✅ GET /leads/high-value

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 62.95 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "id": 6,
      "leadName": "Christopher Walsh",
      "company": "Apex Manufacturing Inc",
      "email": "c.walsh@apexman.com",
      "phone": "312-555-0103",
      "status": "PRE_CONVERSION",
      "assignedTo": 6,
      "assignedToUsername": "david.rodriguez",
      "assignedToFirstName": "David",
      "assignedToLastName": "Rodriguez",
      "potentialValue": 3200000.0,
      "leadSource": "Partner Referral",

... (truncated)
```

---

### ✅ GET /leads/distribution-stats

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 61.10 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "activeSalesPersons": 14,
    "newLeadsCount": 1,
    "assignedLeadsCount": 45,
    "totalLeadsCount": 46
  },
  "error": null
}
```

---

### ✅ GET /leads/117

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 46.13 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "id": 117,
    "leadName": "Test Lead 1761958109",
    "company": "Test Company Inc",
    "email": "testlead_1761958109@test.com",
    "phone": "555-9999",
    "status": "NEW",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 500000.0,
    "leadSource": "Website Signup",
    "leadScore": 45,
    "createdDate": "2025-11-01T00:48:29
... (truncated)
```

---

### ✅ PUT /leads/117

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 156.28 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Lead updated successfully",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "NEW",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    "leadSource": "Webinar",
    "leadScore": 45,
    "createdDate": "2025-11-01T00:48:29.645112",
    
... (truncated)
```

---

### ✅ PUT /leads/117/status

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 138.91 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Lead status updated successfully",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "IN_PROGRESS",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    "leadSource": "Webinar",
    "leadScore": 45,
    "createdDate": "2025-11-01T00:48:2
... (truncated)
```

---

### ✅ POST /leads/117/recalculate-score

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 147.97 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Lead score recalculated successfully",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "IN_PROGRESS",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    "leadSource": "Webinar",
    "leadScore": 80,
    "createdDate": "2025-11-01T00:
... (truncated)
```

---

### ✅ POST /leads/distribute

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 30.89 ms
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

### ✅ POST /leads/117/escalate

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 148.69 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Escalation request received. Lead does not meet high-value criteria (potentialValue < $1M) - no escalation performed",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "IN_PROGRESS",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    
... (truncated)
```

---

### ✅ POST /leads/117/request-approval

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 265.21 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Approval requested successfully",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "IN_PROGRESS",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    "leadSource": "Webinar",
    "leadScore": 80,
    "createdDate": "2025-11-01T00:48:29
... (truncated)
```

---

### ✅ POST /leads/117/approve

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 278.74 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Lead approved successfully",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "CONVERTED",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    "leadSource": "Webinar",
    "leadScore": 80,
    "createdDate": "2025-11-01T00:48:29.645112
... (truncated)
```

---

### ✅ POST /leads/117/reject

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 303.92 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Lead rejected successfully",
  "data": {
    "id": 117,
    "leadName": "Updated Test Lead",
    "company": "Updated Company Inc",
    "email": "updated@test.com",
    "phone": "555-8888",
    "status": "REJECTED",
    "assignedTo": null,
    "assignedToUsername": null,
    "assignedToFirstName": null,
    "assignedToLastName": null,
    "potentialValue": 750000.0,
    "leadSource": "Webinar",
    "leadScore": 80,
    "createdDate": "2025-11-01T00:48:29.645112"
... (truncated)
```

---

### ✅ GET /leads/117/history

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 58.22 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 307,
        "leadId": 117,
        "userId": 20,
        "username": "SYSTEM",
        "userFirstName": "System",
        "userLastName": "User",
        "commentText": "Lead conversion rejected by manager. Reason: Test rejection reason",
        "action": "Rejected",
        "timestamp": "2025-11-01T00:48:31.388603",
        "actionType": "WORKFLOW",
        "oldStatus": "CONVERTED",
  
... (truncated)
```

---

### ✅ POST /leads/117/history

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 293.39 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Comment added successfully",
  "data": {
    "id": 308,
    "leadId": 117,
    "userId": 20,
    "username": "SYSTEM",
    "userFirstName": "System",
    "userLastName": "User",
    "commentText": "Test comment from automated test script",
    "action": "Test Action",
    "timestamp": "2025-11-01T00:48:31.743827084",
    "actionType": "USER_ACTION",
    "oldStatus": null,
    "newStatus": null
  },
  "error": null
}
```

---

### ✅ GET /leads/117/history/recent

- **Expected Status:** 200
- **Actual Status:** 200
- **Response Time:** 40.02 ms
- **Test Status:** PASS

**Response Sample:**

```json
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "id": 308,
      "leadId": 117,
      "userId": 20,
      "username": "SYSTEM",
      "userFirstName": "System",
      "userLastName": "User",
      "commentText": "Test comment from automated test script",
      "action": "Test Action",
      "timestamp": "2025-11-01T00:48:31.743827",
      "actionType": "USER_ACTION",
      "oldStatus": null,
      "newStatus": null
    },
    {
      "id": 307,
      "leadId": 1
... (truncated)
```

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
| GET | /leads | ✅ 200 | 345.58 |
| GET | /leads | ✅ 200 | 69.76 |
| GET | /leads | ✅ 200 | 88.27 |
| GET | /leads | ✅ 200 | 67.44 |
| GET | /leads | ✅ 200 | 105.24 |
| POST | /leads | ✅ 200 | 463.79 |
| GET | /leads/my-leads | ✅ 200 | 37.11 |
| GET | /leads/new | ✅ 200 | 48.14 |
| GET | /leads/high-value | ✅ 200 | 62.95 |
| GET | /leads/distribution-stats | ✅ 200 | 61.10 |
| GET | /leads/117 | ✅ 200 | 46.13 |
| PUT | /leads/117 | ✅ 200 | 156.28 |
| PUT | /leads/117/status | ✅ 200 | 138.91 |
| POST | /leads/117/recalculate-score | ✅ 200 | 147.97 |
| POST | /leads/distribute | ✅ 200 | 30.89 |
| POST | /leads/117/escalate | ✅ 200 | 148.69 |
| POST | /leads/117/request-approval | ✅ 200 | 265.21 |
| POST | /leads/117/approve | ✅ 200 | 278.74 |
| POST | /leads/117/reject | ✅ 200 | 303.92 |
| GET | /leads/117/history | ✅ 200 | 58.22 |
| POST | /leads/117/history | ✅ 200 | 293.39 |
| GET | /leads/117/history/recent | ✅ 200 | 40.02 |

## Notes

- Tests marked with ⚠ indicate expected behavior (e.g., authentication required, business rules)
- All timestamps are generated dynamically to ensure unique test data
- The comprehensive JSON report with full request/response data is available in `test_report.json`
