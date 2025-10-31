#!/bin/bash

# Test script for Leads API
# Usage: ./test-leads-api.sh [base-url]
# Example: ./test-leads-api.sh http://localhost:8090/api

BASE_URL=${1:-"http://localhost:8090/api"}

echo "Testing Leads API at: $BASE_URL"
echo "=================================="

echo ""
echo "1. Test GET /leads (all leads, no filters)..."
curl -s -w "\nHTTP Status: %{http_code}\n" "$BASE_URL/leads" | head -20

echo ""
echo "2. Test GET /leads?status=NEW (with status filter)..."
curl -s -w "\nHTTP Status: %{http_code}\n" "$BASE_URL/leads?status=NEW" | head -20

echo ""
echo "3. Test GET /leads?leadSource=Website (with leadSource filter)..."
curl -s -w "\nHTTP Status: %{http_code}\n" "$BASE_URL/leads?leadSource=Website" | head -20

echo ""
echo "4. Test GET /leads?page=0&size=5 (with pagination)..."
curl -s -w "\nHTTP Status: %{http_code}\n" "$BASE_URL/leads?page=0&size=5" | head -20

echo ""
echo "5. Test GET /leads?status=NEW&leadSource=Website (combined filters)..."
curl -s -w "\nHTTP Status: %{http_code}\n" "$BASE_URL/leads?status=NEW&leadSource=Website" | head -20

echo ""
echo "Tests completed!"

