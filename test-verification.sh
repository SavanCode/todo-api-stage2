#!/bin/bash

echo "🧪 Running Todo API Tests..."
echo "================================"

# Run tests and capture output
TEST_OUTPUT=$(./mvnw test 2>&1)

# Extract test results
TESTS_RUN=$(echo "$TEST_OUTPUT" | grep "Tests run:" | tail -1)
BUILD_RESULT=$(echo "$TEST_OUTPUT" | grep "BUILD" | tail -1)

echo ""
echo "📊 Test Results:"
echo "$TESTS_RUN"
echo "$BUILD_RESULT"
echo ""

# Check if tests passed
if echo "$BUILD_RESULT" | grep -q "SUCCESS"; then
    echo "✅ All tests passed successfully!"
    echo "🎉 Your Todo API is working correctly."
    exit 0
else
    echo "❌ Some tests failed. Please check the output above."
    echo "🔍 Run 'mvn test' for detailed information."
    exit 1
fi 