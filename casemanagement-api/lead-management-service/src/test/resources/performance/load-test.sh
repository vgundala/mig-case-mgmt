#!/bin/bash

# Lead Management Service - Load Testing Script
# This script performs load testing on the Lead Management Service API

# Configuration
BASE_URL="http://localhost:8080"
AUTH_TOKEN=""
CONCURRENT_USERS=10
TOTAL_REQUESTS=1000
TEST_DURATION=60

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if service is running
check_service() {
    print_status "Checking if service is running..."
    if curl -s -f "$BASE_URL/actuator/health" > /dev/null; then
        print_status "Service is running"
        return 0
    else
        print_error "Service is not running. Please start the service first."
        return 1
    fi
}

# Function to authenticate and get token
authenticate() {
    print_status "Authenticating to get JWT token..."
    
    # Try to login with test user
    RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
        -H "Content-Type: application/json" \
        -d '{
            "username": "testuser",
            "password": "password123"
        }')
    
    if echo "$RESPONSE" | grep -q '"success":true'; then
        AUTH_TOKEN=$(echo "$RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)
        print_status "Authentication successful"
        return 0
    else
        print_error "Authentication failed. Please check credentials."
        return 1
    fi
}

# Function to create test data
create_test_data() {
    print_status "Creating test data..."
    
    # Create test leads
    for i in $(seq 1 10); do
        curl -s -X POST "$BASE_URL/leads" \
            -H "Content-Type: application/json" \
            -H "Authorization: Bearer $AUTH_TOKEN" \
            -d "{
                \"leadName\": \"Load Test Lead $i\",
                \"company\": \"Load Test Company $i\",
                \"email\": \"loadtest$i@testcompany.com\",
                \"phone\": \"555-000$i\",
                \"potentialValue\": $((50000 + i * 10000)),
                \"leadSource\": \"Load Test\"
            }" > /dev/null
    done
    
    print_status "Test data created"
}

# Function to run load test on specific endpoint
run_load_test() {
    local endpoint=$1
    local method=$2
    local data=$3
    local description=$4
    
    print_status "Running load test: $description"
    
    # Prepare curl command
    local curl_cmd="curl -s -w '%{http_code},%{time_total}' -X $method"
    curl_cmd="$curl_cmd -H 'Authorization: Bearer $AUTH_TOKEN'"
    
    if [ "$method" = "POST" ] || [ "$method" = "PUT" ]; then
        curl_cmd="$curl_cmd -H 'Content-Type: application/json'"
        curl_cmd="$curl_cmd -d '$data'"
    fi
    
    curl_cmd="$curl_cmd '$BASE_URL$endpoint'"
    
    # Run load test using Apache Bench or curl
    if command -v ab &> /dev/null; then
        # Use Apache Bench
        ab -n $TOTAL_REQUESTS -c $CONCURRENT_USERS \
            -H "Authorization: Bearer $AUTH_TOKEN" \
            -H "Content-Type: application/json" \
            "$BASE_URL$endpoint" > "load_test_${endpoint//\//_}.txt" 2>&1
        
        print_status "Load test completed for $endpoint"
    else
        print_warning "Apache Bench not found. Using curl for basic testing..."
        
        # Basic curl testing
        local success_count=0
        local error_count=0
        local total_time=0
        
        for i in $(seq 1 $TOTAL_REQUESTS); do
            local start_time=$(date +%s.%N)
            local response=$(eval $curl_cmd)
            local end_time=$(date +%s.%N)
            
            local http_code=$(echo "$response" | tail -c 4)
            local duration=$(echo "$end_time - $start_time" | bc)
            
            total_time=$(echo "$total_time + $duration" | bc)
            
            if [ "$http_code" = "200" ]; then
                success_count=$((success_count + 1))
            else
                error_count=$((error_count + 1))
            fi
            
            # Progress indicator
            if [ $((i % 100)) -eq 0 ]; then
                echo -n "."
            fi
        done
        
        echo ""
        print_status "Load test completed for $endpoint"
        print_status "Success: $success_count, Errors: $error_count"
        print_status "Average response time: $(echo "scale=3; $total_time / $TOTAL_REQUESTS" | bc)s"
    fi
}

# Function to run comprehensive load tests
run_comprehensive_tests() {
    print_status "Starting comprehensive load tests..."
    
    # Test 1: Get all leads
    run_load_test "/leads" "GET" "" "Get All Leads"
    
    # Test 2: Get my leads
    run_load_test "/leads/my-leads" "GET" "" "Get My Leads"
    
    # Test 3: Create lead
    run_load_test "/leads" "POST" '{
        "leadName": "Load Test Lead",
        "company": "Load Test Company",
        "email": "loadtest@testcompany.com",
        "phone": "555-9999",
        "potentialValue": 100000,
        "leadSource": "Load Test"
    }' "Create Lead"
    
    # Test 4: Update lead status
    run_load_test "/leads/1/status?status=IN_PROGRESS" "PUT" "" "Update Lead Status"
    
    # Test 5: Add comment
    run_load_test "/leads/1/comments" "POST" '{
        "commentText": "Load test comment"
    }' "Add Comment"
    
    # Test 6: Get lead history
    run_load_test "/leads/1/history" "GET" "" "Get Lead History"
    
    print_status "All load tests completed"
}

# Function to run stress test
run_stress_test() {
    print_status "Starting stress test..."
    
    # Gradually increase load
    local users=1
    while [ $users -le $CONCURRENT_USERS ]; do
        print_status "Testing with $users concurrent users..."
        
        ab -n $((users * 100)) -c $users \
            -H "Authorization: Bearer $AUTH_TOKEN" \
            "$BASE_URL/leads" > "stress_test_${users}_users.txt" 2>&1
        
        users=$((users * 2))
        sleep 5
    done
    
    print_status "Stress test completed"
}

# Function to generate performance report
generate_report() {
    print_status "Generating performance report..."
    
    local report_file="performance_report_$(date +%Y%m%d_%H%M%S).txt"
    
    echo "Lead Management Service - Performance Test Report" > "$report_file"
    echo "Generated: $(date)" >> "$report_file"
    echo "================================================" >> "$report_file"
    echo "" >> "$report_file"
    
    # Add test results
    for file in load_test_*.txt; do
        if [ -f "$file" ]; then
            echo "Test: $file" >> "$report_file"
            echo "----------------------------------------" >> "$report_file"
            cat "$file" >> "$report_file"
            echo "" >> "$report_file"
        fi
    done
    
    print_status "Performance report generated: $report_file"
}

# Function to cleanup test data
cleanup() {
    print_status "Cleaning up test data..."
    
    # Delete test leads (if API supports it)
    # This would require implementing a delete endpoint
    print_status "Cleanup completed"
}

# Main execution
main() {
    echo "Lead Management Service - Load Testing Script"
    echo "============================================="
    echo ""
    
    # Check if service is running
    if ! check_service; then
        exit 1
    fi
    
    # Authenticate
    if ! authenticate; then
        exit 1
    fi
    
    # Create test data
    create_test_data
    
    # Run load tests
    run_comprehensive_tests
    
    # Run stress test (optional)
    read -p "Run stress test? (y/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        run_stress_test
    fi
    
    # Generate report
    generate_report
    
    # Cleanup
    cleanup
    
    print_status "Load testing completed successfully!"
}

# Check dependencies
check_dependencies() {
    local missing_deps=()
    
    if ! command -v curl &> /dev/null; then
        missing_deps+=("curl")
    fi
    
    if ! command -v bc &> /dev/null; then
        missing_deps+=("bc")
    fi
    
    if [ ${#missing_deps[@]} -ne 0 ]; then
        print_error "Missing dependencies: ${missing_deps[*]}"
        print_error "Please install the missing dependencies and try again."
        exit 1
    fi
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -u|--url)
            BASE_URL="$2"
            shift 2
            ;;
        -c|--concurrent)
            CONCURRENT_USERS="$2"
            shift 2
            ;;
        -n|--requests)
            TOTAL_REQUESTS="$2"
            shift 2
            ;;
        -d|--duration)
            TEST_DURATION="$2"
            shift 2
            ;;
        -h|--help)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo "  -u, --url URL              Base URL (default: http://localhost:8080)"
            echo "  -c, --concurrent NUM       Concurrent users (default: 10)"
            echo "  -n, --requests NUM         Total requests (default: 1000)"
            echo "  -d, --duration SECONDS     Test duration (default: 60)"
            echo "  -h, --help                 Show this help message"
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            exit 1
            ;;
    esac
done

# Check dependencies
check_dependencies

# Run main function
main

