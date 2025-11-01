# Comprehensive API Test Script

This test script automatically tests all endpoints from the OpenAPI specification for the Lead Management Service API.

## Features

- **Comprehensive Coverage**: Tests all 20+ endpoints from the OpenAPI specification
- **Multiple User Roles**: Tests with both SALES_PERSON and SALES_MANAGER roles
- **Sample Data**: Uses realistic sample data for all test cases
- **Error Reporting**: Detailed error reporting with response times
- **JSON Report**: Generates a detailed JSON report file

## Setup

### Option 1: Using Helper Script (Recommended)
```bash
./run_tests.sh
```

Or with custom URL:
```bash
./run_tests.sh https://leads-api.ivdata.dev/api
```

### Option 2: Manual Setup with Virtual Environment
```bash
# Create virtual environment
python3 -m venv .venv

# Activate virtual environment
source .venv/bin/activate  # On Linux/Mac
# or
.venv\Scripts\activate  # On Windows

# Install dependencies
pip install -r requirements-test.txt
```

### Option 3: Global Installation (Not Recommended)
```bash
pip install requests
```

## Usage

### Basic Usage (with virtual environment)
```bash
source .venv/bin/activate  # Activate venv first
python test_all_endpoints.py
deactivate  # Deactivate when done
```

### Using Helper Script (Easiest)
```bash
./run_tests.sh
```

### Custom Base URL
```bash
source .venv/bin/activate
python test_all_endpoints.py https://leads-api.ivdata.dev/api
deactivate
```

### With Authentication Token
```bash
source .venv/bin/activate
python test_all_endpoints.py https://leads-api.ivdata.dev/api YOUR_TOKEN
deactivate
```

### With Both Tokens (Sales Person and Manager)
```bash
source .venv/bin/activate
python test_all_endpoints.py https://leads-api.ivdata.dev/api SALES_TOKEN MANAGER_TOKEN
deactivate
```

### Using Environment Variables
```bash
export API_TOKEN="your-sales-person-token"
export MANAGER_TOKEN="your-manager-token"
source .venv/bin/activate
python test_all_endpoints.py https://leads-api.ivdata.dev/api
deactivate
```

Or with helper script:
```bash
export API_TOKEN="your-token"
./run_tests.sh https://leads-api.ivdata.dev/api
```

### Direct Execution (if venv is already active)
```bash
source .venv/bin/activate
./test_all_endpoints.py
deactivate
```

## Test Coverage

The script tests all endpoints organized by category:

**Note:** Authentication endpoints (`/auth/login` and `/auth/register`) are **skipped by default**. Provide tokens via command line or environment variables for authenticated tests.

### Authentication (SKIPPED by default)
- ⏭️ POST `/auth/login` - User authentication (skipped)
- ⏭️ POST `/auth/register` - User registration (skipped)

### Lead Management
- ✅ GET `/leads` - Get all leads (with pagination and filters)
- ✅ POST `/leads` - Create new lead
- ✅ GET `/leads/{id}` - Get lead by ID
- ✅ PUT `/leads/{id}` - Update lead
- ✅ GET `/leads/my-leads` - Get current user's assigned leads
- ✅ GET `/leads/new` - Get new unassigned leads
- ✅ GET `/leads/high-value` - Get high-value leads (>= $1M)
- ✅ PUT `/leads/{id}/status` - Update lead status
- ✅ POST `/leads/{id}/recalculate-score` - Recalculate lead score
- ✅ GET `/leads/distribution-stats` - Get distribution statistics

### Workflow Operations
- ✅ POST `/leads/distribute` - Distribute leads to sales team (Manager only)
- ✅ POST `/leads/{id}/escalate` - Escalate high-value lead
- ✅ POST `/leads/{id}/request-approval` - Request approval for standard lead
- ✅ POST `/leads/{id}/approve` - Approve lead conversion (Manager only)
- ✅ POST `/leads/{id}/reject` - Reject lead conversion (Manager only)

### Lead History
- ✅ GET `/leads/{leadId}/history` - Get lead history with pagination
- ✅ POST `/leads/{leadId}/history` - Add comment to lead
- ✅ GET `/leads/{leadId}/history/recent` - Get recent lead history

## Output

The script provides:

1. **Console Output**: Real-time test results with status indicators
2. **Test Report**: Summary statistics (passed/failed/errors)
3. **JSON Report**: Detailed `test_report.json` file with all test results

### Report Format

```json
{
  "timestamp": "2025-01-30T10:30:00",
  "base_url": "https://leads-api.ivdata.dev/api",
  "summary": {
    "total": 25,
    "passed": 23,
    "failed": 2,
    "errors": 0,
    "success_rate": "92.0%",
    "avg_response_time_ms": 245.67
  },
  "results": [
    {
      "endpoint": "/auth/login",
      "method": "POST",
      "status_code": 200,
      "expected_status": 200,
      "test_status": "PASS",
      "response_time_ms": 123.45,
      "error_message": null
    }
  ]
}
```

## Authentication

**By default, authentication endpoints are skipped.** You can provide JWT tokens in two ways:

### Option 1: Command Line Arguments
```bash
python test_all_endpoints.py <BASE_URL> <TOKEN> [MANAGER_TOKEN]
```

### Option 2: Environment Variables
```bash
export API_TOKEN="your-jwt-token"
export MANAGER_TOKEN="your-manager-jwt-token"  # Optional
python test_all_endpoints.py
```

### Getting a Token

To get a JWT token, you can:
1. Use the `/auth/login` endpoint manually:
   ```bash
   curl -X POST https://leads-api.ivdata.dev/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username":"your-username","password":"your-password"}'
   ```

2. Or test authentication endpoints by setting `skip_auth=False` in the code (line 696)

### Test Credentials (if testing auth manually)

The following test credentials may be available:
- **Sales Person**: `emily.johnson` / `password123`
- **Manager**: `sarah.anderson` / `password123`
- **Alternative**: `sales1` / `password123`

## Sample Data

The script automatically generates realistic sample data for:
- Lead creation
- Lead updates
- Comments
- User registration

All test data is properly formatted according to the OpenAPI specification.

## Troubleshooting

### Authentication Failures
If login fails, verify:
1. The API is accessible at the base URL
2. Test users exist in the database
3. Passwords match the database (default: `password123`)

### Connection Errors
- Check network connectivity
- Verify the base URL is correct
- Check if the API server is running
- Ensure firewall/proxy settings allow access

### Test Failures
- Review the error messages in the console output
- Check the `test_report.json` file for detailed error information
- Verify the API endpoint responses match expected status codes

## Examples

### Run with default URL
```bash
python test_all_endpoints.py
```

### Run with local development server
```bash
python test_all_endpoints.py http://localhost:8080/api
```

### View report
```bash
python test_all_endpoints.py
cat test_report.json | jq '.summary'  # If jq is installed
```

## Exit Codes

- `0`: All tests completed (may include failures)
- Non-zero: Script execution error

## Notes

- Tests that require manager role may show 403 errors when run with sales person token (expected behavior)
- Some workflow operations may fail if lead status doesn't meet requirements (expected behavior)
- Response times are measured for all requests and included in the report
- The script creates a new test lead for comprehensive testing
- All test data uses unique timestamps to avoid conflicts

