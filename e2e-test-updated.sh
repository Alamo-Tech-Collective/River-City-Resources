#!/bin/bash
set -euo pipefail

# E2E Testing Script for River City Resources - Updated Version
# This script performs comprehensive testing of all application features

BASE_URL="http://localhost:8080"
TEST_RESULTS_FILE="e2e-test-results-updated.txt"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Initialize test results file
echo "River City Resources - E2E Test Results (Updated)" > "$TEST_RESULTS_FILE"
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

# Helper function to check redirect
check_redirect() {
    local url=$1
    local expected_location=$2
    local test_name=$3
    
    response=$(curl -s -I "$url")
    response_code=$(echo "$response" | grep -E "^HTTP" | awk '{print $2}')
    location=$(echo "$response" | grep -E "^Location:" | sed 's/Location: //' | tr -d '\r')
    
    if [ "$response_code" = "302" ] && [[ "$location" == *"$expected_location"* ]]; then
        log_test "$test_name" "PASS" ""
    else
        log_test "$test_name" "FAIL" "Expected redirect to $expected_location but got $location with code $response_code"
    fi
}

echo -e "${YELLOW}Starting River City Resources E2E Tests (Updated)...${NC}"
echo ""

# 1. Test Homepage
echo "Testing Homepage..."
check_response "$BASE_URL" "200" "Homepage loads"
check_page_content "$BASE_URL" "River City Resources" "Homepage contains site title"
check_page_content "$BASE_URL" "San Antonio" "Homepage mentions San Antonio"
check_page_content "$BASE_URL" "disability resources" "Homepage mentions disability resources"

# 2. Test Public Pages
echo ""
echo "Testing Public Pages..."
check_response "$BASE_URL/directory/search" "200" "Search page loads"
check_page_content "$BASE_URL/directory/search" "Search" "Search page has search functionality"
check_page_content "$BASE_URL/directory/search" "Keywords" "Search page has keywords field"

# 3. Test Navigation Links
echo ""
echo "Testing Navigation..."
check_page_content "$BASE_URL" 'href="/"' "Home link exists"
check_page_content "$BASE_URL" 'href="/directory/search"' "Search link exists"
check_page_content "$BASE_URL" 'href="/login"' "Admin login link exists"

# 4. Test Category Links (from homepage)
echo ""
echo "Testing Category Links..."
check_page_content "$BASE_URL" "/directory/category/" "Category links exist"

# 5. Test Authentication-Required Pages
echo ""
echo "Testing Authentication..."
check_redirect "$BASE_URL/resource" "/login/auth" "Resource page requires authentication"
check_redirect "$BASE_URL/category" "/login/auth" "Category management requires authentication"
check_redirect "$BASE_URL/admin" "/login/auth" "Admin page requires authentication"
check_redirect "$BASE_URL/resource/create" "/login/auth" "Resource create requires authentication"

# 6. Test Login Page
echo ""
echo "Testing Login Page..."
check_response "$BASE_URL/login" "200" "Login page accessible"
check_page_content "$BASE_URL/login" "Username" "Login page has username field"
check_page_content "$BASE_URL/login" "Password" "Login page has password field"

# 7. Test Search Functionality
echo ""
echo "Testing Search Features..."
check_page_content "$BASE_URL/directory/search" '<form' "Search form exists"
check_page_content "$BASE_URL/directory/search" 'name="searchQuery"' "Search query field exists"
check_page_content "$BASE_URL/directory/search" 'role="search"' "Search form has ARIA role"

# 8. Test Accessibility Features
echo ""
echo "Testing Accessibility..."
check_page_content "$BASE_URL" 'role="navigation"' "Navigation has ARIA role"
check_page_content "$BASE_URL" 'role="main"' "Main content has ARIA role"
check_page_content "$BASE_URL" 'Skip to main content' "Skip navigation link exists"
check_page_content "$BASE_URL" 'aria-describedby' "ARIA descriptions present"
check_page_content "$BASE_URL" 'alt=' "Images have alt text"

# 9. Test Error Handling
echo ""
echo "Testing Error Handling..."
check_redirect "$BASE_URL/nonexistent-page" "/login/auth" "Non-existent page redirects to login"
check_redirect "$BASE_URL/resource/9999999" "/login/auth" "Non-existent resource redirects to login"

# 10. Test Static Assets
echo ""
echo "Testing Static Assets..."
check_response "$BASE_URL/assets/bootstrap.css" "200" "Bootstrap CSS loads"
check_response "$BASE_URL/assets/accessibility.css" "200" "Accessibility CSS loads"
check_response "$BASE_URL/assets/favicon.ico" "200" "Favicon loads"

# 11. Test Search with Parameters
echo ""
echo "Testing Search with Parameters..."
check_response "$BASE_URL/directory/search?searchQuery=test" "200" "Search with query parameter"
check_response "$BASE_URL/directory/search?category=1" "200" "Search with category filter"

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