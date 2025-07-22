#!/bin/bash
set -euo pipefail

# E2E Testing Script for River City Resources
# This script performs comprehensive testing of all application features

BASE_URL="http://localhost:8080"
TEST_RESULTS_FILE="e2e-test-results.txt"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Initialize test results file
echo "River City Resources - E2E Test Results" > "$TEST_RESULTS_FILE"
echo "Test Date: $(date)" >> "$TEST_RESULTS_FILE"
echo "========================================" >> "$TEST_RESULTS_FILE"
echo "" >> "$TEST_RESULTS_FILE"

# Test counter
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Helper function to log results
log_test() {
    local test_name=$1
    local status=$2
    local details=$3
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    
    if [ "$status" = "PASS" ]; then
        echo -e "${GREEN}✓ $test_name${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ $test_name${NC}"
        echo "  Details: $details"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
    
    echo "$test_name: $status" >> "$TEST_RESULTS_FILE"
    if [ "$status" = "FAIL" ]; then
        echo "  Details: $details" >> "$TEST_RESULTS_FILE"
    fi
    echo "" >> "$TEST_RESULTS_FILE"
}

# Helper function to check HTTP response
check_response() {
    local url=$1
    local expected_code=$2
    local test_name=$3
    
    response_code=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    
    if [ "$response_code" = "$expected_code" ]; then
        log_test "$test_name" "PASS" ""
    else
        log_test "$test_name" "FAIL" "Expected HTTP $expected_code but got $response_code"
    fi
}

# Helper function to check if page contains text
check_page_content() {
    local url=$1
    local search_text=$2
    local test_name=$3
    
    if curl -s "$url" | grep -q "$search_text"; then
        log_test "$test_name" "PASS" ""
    else
        log_test "$test_name" "FAIL" "Page does not contain expected text: $search_text"
    fi
}

echo -e "${YELLOW}Starting River City Resources E2E Tests...${NC}"
echo ""

# 1. Test Homepage
echo "Testing Homepage..."
check_response "$BASE_URL" "200" "Homepage loads"
check_page_content "$BASE_URL" "River City Resources" "Homepage contains site title"
check_page_content "$BASE_URL" "San Antonio" "Homepage mentions San Antonio"

# 2. Test Navigation
echo ""
echo "Testing Navigation..."
check_response "$BASE_URL/resource" "200" "Resource list page loads"
check_response "$BASE_URL/category" "200" "Category list page loads"
check_response "$BASE_URL/admin" "200" "Admin page accessible"

# 3. Test Resource Pages
echo ""
echo "Testing Resource Functionality..."
check_response "$BASE_URL/resource/index" "200" "Resource index page"
check_response "$BASE_URL/resource/create" "200" "Resource create page"
check_page_content "$BASE_URL/resource/create" "Create Resource" "Create form has title"

# 4. Test Category Pages
echo ""
echo "Testing Category Functionality..."
check_response "$BASE_URL/category/index" "200" "Category index page"
check_response "$BASE_URL/category/create" "200" "Category create page"

# 5. Test Search Functionality
echo ""
echo "Testing Search..."
check_response "$BASE_URL/resource/search" "200" "Search page loads"
check_page_content "$BASE_URL/resource/search" "Search" "Search page has search functionality"

# 6. Test Static Pages
echo ""
echo "Testing Static Pages..."
check_response "$BASE_URL/about" "200" "About page"
check_response "$BASE_URL/contact" "200" "Contact page"

# 7. Test API Endpoints (if any)
echo ""
echo "Testing API Endpoints..."
check_response "$BASE_URL/api/resources" "200" "Resources API endpoint"
check_response "$BASE_URL/api/categories" "200" "Categories API endpoint"

# 8. Test Error Handling
echo ""
echo "Testing Error Handling..."
check_response "$BASE_URL/nonexistent-page" "404" "404 error for non-existent page"
check_response "$BASE_URL/resource/9999999" "404" "404 for non-existent resource"

# 9. Test Form Submission (Create a test resource)
echo ""
echo "Testing Form Submission..."
# This would require more complex curl commands with form data
# For now, just check that forms are present
check_page_content "$BASE_URL/resource/create" '<form' "Create form exists"
check_page_content "$BASE_URL/resource/create" 'name="name"' "Name field exists"
check_page_content "$BASE_URL/resource/create" 'name="description"' "Description field exists"

# 10. Test Accessibility Features
echo ""
echo "Testing Accessibility..."
check_page_content "$BASE_URL" 'role="navigation"' "Navigation has ARIA role"
check_page_content "$BASE_URL" 'role="main"' "Main content has ARIA role"
check_page_content "$BASE_URL" 'alt=' "Images have alt text"

# Summary
echo ""
echo "========================================" >> "$TEST_RESULTS_FILE"
echo "Test Summary:" >> "$TEST_RESULTS_FILE"
echo "Total Tests: $TOTAL_TESTS" >> "$TEST_RESULTS_FILE"
echo "Passed: $PASSED_TESTS" >> "$TEST_RESULTS_FILE"
echo "Failed: $FAILED_TESTS" >> "$TEST_RESULTS_FILE"
echo "Success Rate: $(( PASSED_TESTS * 100 / TOTAL_TESTS ))%" >> "$TEST_RESULTS_FILE"

echo ""
echo -e "${YELLOW}Test Summary:${NC}"
echo "Total Tests: $TOTAL_TESTS"
echo -e "Passed: ${GREEN}$PASSED_TESTS${NC}"
echo -e "Failed: ${RED}$FAILED_TESTS${NC}"
echo "Success Rate: $(( PASSED_TESTS * 100 / TOTAL_TESTS ))%"
echo ""
echo "Detailed results saved to: $TEST_RESULTS_FILE"

# Exit with error code if any tests failed
if [ $FAILED_TESTS -gt 0 ]; then
    exit 1
fi