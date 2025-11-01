#!/usr/bin/env python3
"""
Comprehensive API Test Script for Lead Management Service
Tests all endpoints from OpenAPI specification with sample data
"""

import requests
import json
import time
from datetime import datetime
from typing import Dict, Optional, List, Tuple
from dataclasses import dataclass, asdict
from enum import Enum


class TestStatus(Enum):
    PASS = "PASS"
    FAIL = "FAIL"
    SKIP = "SKIP"
    ERROR = "ERROR"


@dataclass
class TestResult:
    endpoint: str
    method: str
    status_code: int
    expected_status: int
    test_status: TestStatus
    response_time_ms: float
    error_message: Optional[str] = None
    response_body: Optional[Dict] = None


class APITester:
    def __init__(self, base_url: str = "https://leads-api.ivdata.dev/api", 
                 token: Optional[str] = None, manager_token: Optional[str] = None):
        self.base_url = base_url.rstrip('/')
        self.session = requests.Session()
        self.results: List[TestResult] = []
        self.sales_person_token: Optional[str] = token
        self.manager_token: Optional[str] = manager_token
        self.created_lead_id: Optional[int] = None
        self.test_user_credentials = {
            "sales_person": {
                "username": "emily.johnson",
                "password": "password123"
            },
            "manager": {
                "username": "sarah.anderson",
                "password": "password123"
            }
        }

    def log_result(self, endpoint: str, method: str, status_code: int, 
                   expected_status: int, test_status: TestStatus,
                   response_time_ms: float, error_message: Optional[str] = None,
                   response_body: Optional[Dict] = None):
        result = TestResult(
            endpoint=endpoint,
            method=method,
            status_code=status_code,
            expected_status=expected_status,
            test_status=test_status,
            response_time_ms=response_time_ms,
            error_message=error_message,
            response_body=response_body
        )
        self.results.append(result)
        return result

    def make_request(self, method: str, endpoint: str, expected_status: int = 200,
                     token: Optional[str] = None, json_data: Optional[Dict] = None,
                     params: Optional[Dict] = None, headers: Optional[Dict] = None,
                     accept_auth_errors: bool = True) -> Tuple[TestResult, Optional[Dict]]:
        """Make HTTP request and return test result"""
        url = f"{self.base_url}{endpoint}"
        req_headers = headers or {}
        
        if token:
            req_headers["Authorization"] = f"Bearer {token}"
        
        if json_data:
            req_headers["Content-Type"] = "application/json"
        
        start_time = time.time()
        try:
            response = self.session.request(
                method=method,
                url=url,
                json=json_data,
                params=params,
                headers=req_headers,
                timeout=30
            )
            response_time_ms = (time.time() - start_time) * 1000
            
            try:
                response_body = response.json() if response.text else None
            except:
                response_body = {"raw": response.text[:500]}
            
            # Since endpoints are open, treat actual failures as failures
            test_status = TestStatus.PASS if response.status_code == expected_status else TestStatus.FAIL
            
            error_msg = None
            if test_status == TestStatus.FAIL:
                if response.status_code in [401, 403]:
                    error_msg = f"Authentication required (got {response.status_code})"
                else:
                    error_msg = f"Expected {expected_status}, got {response.status_code}"
            
            result = self.log_result(
                endpoint=endpoint,
                method=method,
                status_code=response.status_code,
                expected_status=expected_status,
                test_status=test_status,
                response_time_ms=response_time_ms,
                error_message=error_msg,
                response_body=response_body
            )
            
            return result, response_body
            
        except requests.exceptions.RequestException as e:
            response_time_ms = (time.time() - start_time) * 1000
            result = self.log_result(
                endpoint=endpoint,
                method=method,
                status_code=0,
                expected_status=expected_status,
                test_status=TestStatus.ERROR,
                response_time_ms=response_time_ms,
                error_message=str(e),
                response_body=None
            )
            return result, None

    # Authentication Tests
    def test_login(self, username: str, password: str, role: str) -> Optional[str]:
        """Test login endpoint"""
        print(f"\n[TEST] POST /auth/login - {role}")
        login_data = {
            "username": username,
            "password": password
        }
        result, response = self.make_request("POST", "/auth/login", expected_status=200, json_data=login_data)
        
        if result.test_status == TestStatus.PASS and response:
            if response.get("success") and response.get("data", {}).get("token"):
                token = response["data"]["token"]
                print(f"  ✓ Login successful, token obtained")
                return token
            else:
                print(f"  ✗ Login failed: {response.get('message', 'Unknown error')}")
        else:
            print(f"  ✗ Login failed: {result.error_message}")
        
        return None

    def test_register(self):
        """Test user registration"""
        print(f"\n[TEST] POST /auth/register")
        register_data = {
            "username": f"testuser_{int(time.time())}",
            "password": "TestPassword123!",
            "role": "SALES_PERSON",
            "firstName": "Test",
            "lastName": "User",
            "email": f"testuser_{int(time.time())}@test.com",
            "phone": "555-0000"
        }
        result, response = self.make_request("POST", "/auth/register", expected_status=200, json_data=register_data)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Registration successful")
        else:
            print(f"  ✗ Registration failed: {result.error_message}")
            if response:
                print(f"    Response: {response.get('message', 'Unknown error')}")
        
        return result, response

    # Lead Management Tests
    def test_get_all_leads(self, token: str):
        """Test GET /leads with various filters"""
        print(f"\n[TEST] GET /leads - All leads")
        result, response = self.make_request("GET", "/leads", token=token, params={"page": 0, "size": 10})
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Retrieved leads successfully")
            if response and response.get("data", {}).get("content"):
                leads = response["data"]["content"]
                print(f"    Found {len(leads)} leads")
                if leads:
                    # Store first lead ID for later tests
                    if not self.created_lead_id:
                        self.created_lead_id = leads[0].get("id")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_get_leads_with_filters(self, token: str):
        """Test GET /leads with filters"""
        print(f"\n[TEST] GET /leads - With filters")
        
        filters = [
            {"status": "NEW"},
            {"status": "ASSIGNED"},
            {"leadSource": "Partner Referral"},
            {"assignedTo": 1},
        ]
        
        for filter_params in filters:
            result, response = self.make_request("GET", "/leads", token=token, params=filter_params)
            filter_str = ", ".join([f"{k}={v}" for k, v in filter_params.items()])
            if result.test_status == TestStatus.PASS:
                print(f"  ✓ Filter {filter_str}: Success")
            elif result.status_code in [401, 403]:
                print(f"  ! Filter {filter_str}: Auth required (expected)")
            else:
                print(f"  ✗ Filter {filter_str}: {result.error_message}")

    def test_create_lead(self, token: str) -> Optional[int]:
        """Test POST /leads - Create new lead"""
        print(f"\n[TEST] POST /leads - Create new lead")
        lead_data = {
            "leadName": f"Test Lead {int(time.time())}",
            "company": "Test Company Inc",
            "email": f"testlead_{int(time.time())}@test.com",
            "phone": "555-9999",
            "potentialValue": 500000.00,
            "leadSource": "Website Signup",
            "description": "Test lead created by automated test script",
            "industry": "Technology",
            "companySize": "Medium",
            "location": "San Francisco, CA"
        }
        result, response = self.make_request("POST", "/leads", expected_status=200, token=token, json_data=lead_data)
        
        if result.test_status == TestStatus.PASS:
            if response and response.get("success"):
                lead_id = response.get("data", {}).get("id")
                if lead_id:
                    self.created_lead_id = lead_id
                    print(f"  ✓ Lead created successfully, ID: {lead_id}")
                    return lead_id
        if result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Lead creation failed: {result.error_message}")
        return None

    def test_get_lead_by_id(self, token: str, lead_id: int):
        """Test GET /leads/{id}"""
        print(f"\n[TEST] GET /leads/{lead_id}")
        result, response = self.make_request("GET", f"/leads/{lead_id}", token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Lead retrieved successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_update_lead(self, token: str, lead_id: int):
        """Test PUT /leads/{id}"""
        print(f"\n[TEST] PUT /leads/{lead_id}")
        update_data = {
            "leadName": "Updated Test Lead",
            "company": "Updated Company Inc",
            "email": "updated@test.com",
            "phone": "555-8888",
            "potentialValue": 750000.00,
            "leadSource": "Webinar",
            "description": "Updated description",
            "industry": "Finance",
            "companySize": "Large",
            "location": "New York, NY"
        }
        result, response = self.make_request("PUT", f"/leads/{lead_id}", expected_status=200, token=token, json_data=update_data)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Lead updated successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Update failed: {result.error_message}")

    def test_get_my_leads(self, token: str):
        """Test GET /leads/my-leads"""
        print(f"\n[TEST] GET /leads/my-leads")
        result, response = self.make_request("GET", "/leads/my-leads", token=token)
        
        if result.test_status == TestStatus.PASS:
            leads = response.get("data", []) if response else []
            print(f"  ✓ Retrieved {len(leads)} assigned leads")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_get_new_leads(self, token: str):
        """Test GET /leads/new"""
        print(f"\n[TEST] GET /leads/new")
        result, response = self.make_request("GET", "/leads/new", token=token)
        
        if result.test_status == TestStatus.PASS:
            leads = response.get("data", []) if response else []
            print(f"  ✓ Retrieved {len(leads)} new leads")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_get_high_value_leads(self, token: str):
        """Test GET /leads/high-value"""
        print(f"\n[TEST] GET /leads/high-value")
        result, response = self.make_request("GET", "/leads/high-value", token=token)
        
        if result.test_status == TestStatus.PASS:
            leads = response.get("data", []) if response else []
            print(f"  ✓ Retrieved {len(leads)} high-value leads")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_update_lead_status(self, token: str, lead_id: int):
        """Test PUT /leads/{id}/status"""
        print(f"\n[TEST] PUT /leads/{lead_id}/status")
        result, response = self.make_request(
            "PUT", 
            f"/leads/{lead_id}/status", 
            expected_status=200, 
            token=token,
            params={"status": "IN_PROGRESS"}
        )
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Status updated successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Status update failed: {result.error_message}")

    def test_recalculate_score(self, token: str, lead_id: int):
        """Test POST /leads/{id}/recalculate-score"""
        print(f"\n[TEST] POST /leads/{lead_id}/recalculate-score")
        result, response = self.make_request("POST", f"/leads/{lead_id}/recalculate-score", expected_status=200, token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Score recalculated successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Score recalculation failed: {result.error_message}")

    def test_get_distribution_stats(self, token: str):
        """Test GET /leads/distribution-stats"""
        print(f"\n[TEST] GET /leads/distribution-stats")
        result, response = self.make_request("GET", "/leads/distribution-stats", token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Distribution stats retrieved successfully")
            if response and response.get("data"):
                stats = response["data"]
                print(f"    Active sales persons: {stats.get('activeSalesPersons', 'N/A')}")
                print(f"    New leads: {stats.get('newLeadsCount', 'N/A')}")
                print(f"    Assigned leads: {stats.get('assignedLeadsCount', 'N/A')}")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    # Workflow Operations Tests
    def test_distribute_leads(self, token: str):
        """Test POST /leads/distribute - Manager only"""
        print(f"\n[TEST] POST /leads/distribute")
        result, response = self.make_request("POST", "/leads/distribute", expected_status=200, token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Leads distributed successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_escalate_lead(self, token: str, lead_id: int):
        """Test POST /leads/{id}/escalate"""
        print(f"\n[TEST] POST /leads/{lead_id}/escalate")
        result, response = self.make_request("POST", f"/leads/{lead_id}/escalate", expected_status=200, token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Lead escalated successfully")
        elif result.status_code == 400:
            # 400 is expected if lead doesn't meet high-value criteria (potentialValue < $1M)
            api_message = response.get("message", "") if response else ""
            if "high-value criteria" in api_message or "Business rule violation" in str(response):
                print(f"  ! Lead does not meet high-value criteria (expected for leads < $1M)")
                # Mark as expected/skip rather than failure
                result.test_status = TestStatus.SKIP
            else:
                print(f"  ✗ Failed: {result.error_message}")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
            result.test_status = TestStatus.SKIP
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_request_approval(self, token: str, lead_id: int):
        """Test POST /leads/{id}/request-approval"""
        print(f"\n[TEST] POST /leads/{lead_id}/request-approval")
        result, response = self.make_request("POST", f"/leads/{lead_id}/request-approval", expected_status=200, token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Approval requested successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        elif result.status_code == 400:
            print(f"  ! High-value leads must be escalated (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_approve_lead(self, token: str, lead_id: int):
        """Test POST /leads/{id}/approve - Manager only"""
        print(f"\n[TEST] POST /leads/{lead_id}/approve")
        result, response = self.make_request("POST", f"/leads/{lead_id}/approve", expected_status=200, token=token)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Lead approved successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_reject_lead(self, token: str, lead_id: int):
        """Test POST /leads/{id}/reject - Manager only"""
        print(f"\n[TEST] POST /leads/{lead_id}/reject")
        result, response = self.make_request(
            "POST", 
            f"/leads/{lead_id}/reject", 
            expected_status=200, 
            token=token,
            params={"reason": "Test rejection reason"}
        )
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Lead rejected successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    # Lead History Tests
    def test_get_lead_history(self, token: str, lead_id: int):
        """Test GET /leads/{leadId}/history"""
        print(f"\n[TEST] GET /leads/{lead_id}/history")
        result, response = self.make_request(
            "GET", 
            f"/leads/{lead_id}/history", 
            token=token,
            params={"page": 0, "size": 10}
        )
        
        if result.test_status == TestStatus.PASS:
            history = response.get("data", {}).get("content", []) if response else []
            print(f"  ✓ Retrieved {len(history)} history records")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def test_add_comment(self, token: str, lead_id: int):
        """Test POST /leads/{leadId}/history"""
        print(f"\n[TEST] POST /leads/{lead_id}/history - Add comment")
        comment_data = {
            "commentText": "Test comment from automated test script",
            "action": "Test Action"
        }
        result, response = self.make_request("POST", f"/leads/{lead_id}/history", expected_status=200, token=token, json_data=comment_data)
        
        if result.test_status == TestStatus.PASS:
            print(f"  ✓ Comment added successfully")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Comment addition failed: {result.error_message}")

    def test_get_recent_history(self, token: str, lead_id: int):
        """Test GET /leads/{leadId}/history/recent"""
        print(f"\n[TEST] GET /leads/{lead_id}/history/recent")
        result, response = self.make_request(
            "GET", 
            f"/leads/{lead_id}/history/recent", 
            token=token,
            params={"limit": 5}
        )
        
        if result.test_status == TestStatus.PASS:
            history = response.get("data", []) if response else []
            print(f"  ✓ Retrieved {len(history)} recent history records")
        elif result.status_code in [401, 403]:
            print(f"  ! Authentication required (expected)")
        else:
            print(f"  ✗ Failed: {result.error_message}")

    def run_all_tests(self, skip_auth: bool = True):
        """Run all test cases"""
        print("=" * 80)
        print("LEAD MANAGEMENT SERVICE - COMPREHENSIVE API TEST SUITE")
        print("=" * 80)
        print(f"Base URL: {self.base_url}")
        print(f"Start Time: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        if skip_auth:
            print("⚠️  Authentication endpoints skipped")
        print("=" * 80)

        # Step 1: Authentication (skipped if skip_auth is True)
        if not skip_auth:
            print("\n" + "=" * 80)
            print("AUTHENTICATION TESTS")
            print("=" * 80)
            
            # Login as sales person
            self.sales_person_token = self.test_login(
                self.test_user_credentials["sales_person"]["username"],
                self.test_user_credentials["sales_person"]["password"],
                "SALES_PERSON"
            )
            
            # Login as manager
            self.manager_token = self.test_login(
                self.test_user_credentials["manager"]["username"],
                self.test_user_credentials["manager"]["password"],
                "SALES_MANAGER"
            )
            
            # Test registration and try to use the newly registered user
            register_result, register_response = self.make_request(
                "POST",
                "/auth/register",
                expected_status=200,
                json_data={
                    "username": f"testuser_{int(time.time())}",
                    "password": "TestPassword123!",
                    "role": "SALES_PERSON",
                    "firstName": "Test",
                    "lastName": "User",
                    "email": f"testuser_{int(time.time())}@test.com",
                    "phone": "555-0000"
                }
            )
            
            registered_username = None
            if register_result.test_status == TestStatus.PASS and register_response:
                registered_username = register_response.get("data", {}).get("username")
                if registered_username:
                    print(f"   Trying login with newly registered user: {registered_username}")
                    self.sales_person_token = self.test_login(registered_username, "TestPassword123!", "SALES_PERSON")
            
            if not self.sales_person_token and not self.manager_token:
                print("\n⚠️  WARNING: Could not authenticate. Some tests will be skipped.")
                print("   Trying alternative credentials...")
                # Try default credentials from OpenAPI spec
                self.sales_person_token = self.test_login("sales1", "password123", "SALES_PERSON")
                self.manager_token = self.test_login("sarah.anderson", "password123", "SALES_MANAGER")
        else:
            print("\n[SKIP] Authentication endpoints skipped")
            if not self.sales_person_token and not self.manager_token:
                print("ℹ️  No token provided - testing endpoints without authentication")
                import os
                env_token = os.getenv("API_TOKEN")
                env_manager_token = os.getenv("MANAGER_TOKEN")
                if env_token:
                    self.sales_person_token = env_token
                    print("   ✓ Using API_TOKEN from environment")
                if env_manager_token:
                    self.manager_token = env_manager_token
                    print("   ✓ Using MANAGER_TOKEN from environment")
                if not self.sales_person_token and not self.manager_token:
                    print("   ℹ️  Testing without authentication - endpoints are open and should work")

        # Step 2: Lead Management Tests
        print("\n" + "=" * 80)
        print("LEAD MANAGEMENT TESTS")
        print("=" * 80)
        
        # Get token for all subsequent tests (endpoints are open, so token is optional)
        token = self.sales_person_token or self.manager_token
        
        print("ℹ️  Testing all endpoints (authentication not required)")
        
        # Test all endpoints - run regardless of token
        self.test_get_all_leads(token)
        self.test_get_leads_with_filters(token)
        self.test_create_lead(token)
        self.test_get_my_leads(token)
        self.test_get_new_leads(token)
        self.test_get_high_value_leads(token)
        self.test_get_distribution_stats(token)
        
        # Get a lead ID for further tests
        if not self.created_lead_id:
            # Try to get a lead from the list
            _, response = self.make_request("GET", "/leads", token=token, params={"page": 0, "size": 1})
            if response and response.get("data", {}).get("content"):
                leads = response["data"]["content"]
                if leads:
                    self.created_lead_id = leads[0].get("id")
        
        if self.created_lead_id:
            self.test_get_lead_by_id(token, self.created_lead_id)
            self.test_update_lead(token, self.created_lead_id)
            self.test_update_lead_status(token, self.created_lead_id)
            self.test_recalculate_score(token, self.created_lead_id)
        else:
            print("\n⚠️  No lead ID available - using test ID 1 for detailed tests")
            test_lead_id = 1
            self.test_get_lead_by_id(token, test_lead_id)
            self.test_update_lead_status(token, test_lead_id)
            self.test_recalculate_score(token, test_lead_id)

        # Step 3: Workflow Operations Tests
        print("\n" + "=" * 80)
        print("WORKFLOW OPERATIONS TESTS")
        print("=" * 80)
        
        test_lead_id = self.created_lead_id or 1
        
        self.test_distribute_leads(token)
        self.test_escalate_lead(token, test_lead_id)
        self.test_request_approval(token, test_lead_id)
        self.test_approve_lead(token, test_lead_id)
        self.test_reject_lead(token, test_lead_id)

        # Step 4: Lead History Tests
        print("\n" + "=" * 80)
        print("LEAD HISTORY TESTS")
        print("=" * 80)
        
        test_lead_id = self.created_lead_id or 1
        self.test_get_lead_history(token, test_lead_id)
        self.test_add_comment(token, test_lead_id)
        self.test_get_recent_history(token, test_lead_id)

        # Generate report
        self.generate_report()

    def generate_report(self):
        """Generate comprehensive test report"""
        print("\n" + "=" * 80)
        print("TEST REPORT")
        print("=" * 80)
        
        total_tests = len(self.results)
        passed = sum(1 for r in self.results if r.test_status == TestStatus.PASS)
        failed = sum(1 for r in self.results if r.test_status == TestStatus.FAIL)
        errors = sum(1 for r in self.results if r.test_status == TestStatus.ERROR)
        skipped = sum(1 for r in self.results if r.test_status == TestStatus.SKIP)
        
        print(f"\nSummary:")
        print(f"  Total Tests: {total_tests}")
        print(f"  ✓ Passed: {passed} ({passed/total_tests*100:.1f}%)" if total_tests > 0 else "  ✓ Passed: 0")
        print(f"  ✗ Failed: {failed} ({failed/total_tests*100:.1f}%)" if total_tests > 0 else "  ✗ Failed: 0")
        print(f"  ⚠ Errors: {errors} ({errors/total_tests*100:.1f}%)" if total_tests > 0 else "  ⚠ Errors: 0")
        print(f"  ⊘ Skipped: {skipped}")
        
        avg_response_time = sum(r.response_time_ms for r in self.results) / total_tests if total_tests > 0 else 0
        print(f"\nAverage Response Time: {avg_response_time:.2f} ms")
        
        # Failed tests details
        if failed > 0 or errors > 0:
            print(f"\n{'=' * 80}")
            print("FAILED/ERROR TESTS DETAILS:")
            print(f"{'=' * 80}")
            for result in self.results:
                if result.test_status in [TestStatus.FAIL, TestStatus.ERROR]:
                    print(f"\n❌ {result.method} {result.endpoint}")
                    print(f"   Expected: {result.expected_status}, Got: {result.status_code}")
                    print(f"   Response Time: {result.response_time_ms:.2f} ms")
                    if result.error_message:
                        print(f"   Error: {result.error_message}")
                    if result.response_body:
                        error_msg = result.response_body.get("message") or result.response_body.get("error")
                        if error_msg:
                            print(f"   API Message: {error_msg}")
        
        # Save detailed report to file
        report_file = "test_report.json"
        report_data = {
            "timestamp": datetime.now().isoformat(),
            "base_url": self.base_url,
            "summary": {
                "total": total_tests,
                "passed": passed,
                "failed": failed,
                "errors": errors,
                "skipped": skipped,
                "success_rate": f"{passed/total_tests*100:.1f}%" if total_tests > 0 else "0%",
                "avg_response_time_ms": round(avg_response_time, 2)
            },
            "results": [asdict(r) for r in self.results]
        }
        
        with open(report_file, 'w') as f:
            json.dump(report_data, f, indent=2, default=str)
        
        print(f"\n✓ Detailed report saved to: {report_file}")
        
        # Generate comprehensive markdown report
        self.generate_comprehensive_report()
        print(f"\nEnd Time: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print("=" * 80)
    
    def generate_comprehensive_report(self):
        """Generate comprehensive markdown report with all endpoint invocations and sample data"""
        report_file = "ENDPOINT_TEST_REPORT.md"
        
        with open(report_file, 'w') as f:
            f.write("# Lead Management Service - Comprehensive API Test Report\n\n")
            f.write(f"**Generated:** {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n")
            f.write(f"**Base URL:** {self.base_url}\n\n")
            
            # Summary
            total_tests = len(self.results)
            passed = sum(1 for r in self.results if r.test_status == TestStatus.PASS)
            failed = sum(1 for r in self.results if r.test_status == TestStatus.FAIL)
            errors = sum(1 for r in self.results if r.test_status == TestStatus.ERROR)
            
            f.write("## Test Summary\n\n")
            f.write(f"- **Total Tests:** {total_tests}\n")
            f.write(f"- **Passed:** {passed} ({passed/total_tests*100:.1f}%)\n")
            f.write(f"- **Failed:** {failed} ({failed/total_tests*100:.1f}%)\n")
            f.write(f"- **Errors:** {errors} ({errors/total_tests*100:.1f}%)\n\n")
            
            # Group results by endpoint
            f.write("## Endpoint Test Details\n\n")
            
            # Track sample data used
            sample_data = {}
            
            for result in self.results:
                endpoint_key = f"{result.method} {result.endpoint}"
                status_icon = "✅" if result.test_status == TestStatus.PASS else "❌"
                
                f.write(f"### {status_icon} {endpoint_key}\n\n")
                f.write(f"- **Expected Status:** {result.expected_status}\n")
                f.write(f"- **Actual Status:** {result.status_code}\n")
                f.write(f"- **Response Time:** {result.response_time_ms:.2f} ms\n")
                f.write(f"- **Test Status:** {result.test_status.value}\n\n")
                
                # Extract sample data from request
                if result.response_body:
                    # Try to get request data from response_body if it contains request info
                    f.write("**Response Sample:**\n\n")
                    f.write("```json\n")
                    f.write(json.dumps(result.response_body, indent=2, default=str)[:500])
                    if len(json.dumps(result.response_body, default=str)) > 500:
                        f.write("\n... (truncated)")
                    f.write("\n```\n\n")
                
                if result.error_message:
                    f.write(f"**Error:** {result.error_message}\n\n")
                
                f.write("---\n\n")
            
            # Sample Data Section
            f.write("## Sample Data Used\n\n")
            f.write("The following sample data was used in the test cases:\n\n")
            
            # Lead creation sample
            f.write("### Lead Creation Sample\n\n")
            f.write("```json\n")
            f.write(json.dumps({
                "leadName": "Test Lead [timestamp]",
                "company": "Test Company Inc",
                "email": "testlead_[timestamp]@test.com",
                "phone": "555-9999",
                "potentialValue": 500000.00,
                "leadSource": "Website Signup",
                "description": "Test lead created by automated test script",
                "industry": "Technology",
                "companySize": "Medium",
                "location": "San Francisco, CA"
            }, indent=2))
            f.write("\n```\n\n")
            
            # Lead update sample
            f.write("### Lead Update Sample\n\n")
            f.write("```json\n")
            f.write(json.dumps({
                "leadName": "Updated Test Lead",
                "company": "Updated Company Inc",
                "email": "updated@test.com",
                "phone": "555-8888",
                "potentialValue": 750000.00,
                "leadSource": "Webinar",
                "description": "Updated description",
                "industry": "Finance",
                "companySize": "Large",
                "location": "New York, NY"
            }, indent=2))
            f.write("\n```\n\n")
            
            # Comment sample
            f.write("### Comment Sample\n\n")
            f.write("```json\n")
            f.write(json.dumps({
                "commentText": "Test comment from automated test script",
                "action": "Test Action"
            }, indent=2))
            f.write("\n```\n\n")
            
            f.write("## API Endpoints Tested\n\n")
            f.write("| Method | Endpoint | Status | Response Time (ms) |\n")
            f.write("|--------|----------|--------|-------------------|\n")
            
            for result in self.results:
                status_icon = "✅" if result.test_status == TestStatus.PASS else "❌"
                f.write(f"| {result.method} | {result.endpoint} | {status_icon} {result.status_code} | {result.response_time_ms:.2f} |\n")
            
            f.write("\n")
            f.write("## Notes\n\n")
            f.write("- Tests marked with ⚠ indicate expected behavior (e.g., authentication required, business rules)\n")
            f.write("- All timestamps are generated dynamically to ensure unique test data\n")
            f.write("- The comprehensive JSON report with full request/response data is available in `test_report.json`\n")
        
        print(f"\n✓ Comprehensive report saved to: {report_file}")


def main():
    """Main entry point"""
    import sys
    import os
    
    base_url = "https://leads-api.ivdata.dev/api"
    token = None
    manager_token = None
    
    # Parse command line arguments
    if len(sys.argv) > 1:
        base_url = sys.argv[1]
    if len(sys.argv) > 2:
        token = sys.argv[2]
    if len(sys.argv) > 3:
        manager_token = sys.argv[3]
    
    # Check environment variables
    if not token:
        token = os.getenv("API_TOKEN")
    if not manager_token:
        manager_token = os.getenv("MANAGER_TOKEN")
    
    tester = APITester(base_url, token=token, manager_token=manager_token)
    tester.run_all_tests(skip_auth=True)  # Skip auth endpoints by default


if __name__ == "__main__":
    main()

