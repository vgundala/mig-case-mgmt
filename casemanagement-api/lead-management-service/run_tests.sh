#!/bin/bash
# Helper script to run API tests with virtual environment

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# Activate virtual environment
if [ -d ".venv" ]; then
    source .venv/bin/activate
    echo "✓ Virtual environment activated"
else
    echo "⚠️  Virtual environment not found. Creating..."
    python3 -m venv .venv
    source .venv/bin/activate
    pip install --upgrade pip
    pip install -r requirements-test.txt
    echo "✓ Virtual environment created and dependencies installed"
fi

# Run the test script
BASE_URL="${1:-https://leads-api.ivdata.dev/api}"
TOKEN="${2:-$API_TOKEN}"
MANAGER_TOKEN="${3:-$MANAGER_TOKEN}"

echo "Running tests against: $BASE_URL"
if [ -n "$TOKEN" ]; then
    echo "Using provided token for authentication"
fi
echo ""

if [ -n "$TOKEN" ] && [ -n "$MANAGER_TOKEN" ]; then
    python test_all_endpoints.py "$BASE_URL" "$TOKEN" "$MANAGER_TOKEN"
elif [ -n "$TOKEN" ]; then
    python test_all_endpoints.py "$BASE_URL" "$TOKEN"
else
    python test_all_endpoints.py "$BASE_URL"
fi

# Deactivate virtual environment
deactivate

