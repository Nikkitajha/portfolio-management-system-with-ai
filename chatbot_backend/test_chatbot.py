#!/usr/bin/env python3
"""
Test script for chatbot backend to verify structured responses
"""

import requests
import json

def test_chatbot():
    """Test the chatbot with various questions"""
    
    base_url = "http://localhost:5000"
    
    test_questions = [
        "Suggest investment strategy",
        "Analyze portfolio risk", 
        "Current market trends",
        "Best stocks to buy now"
    ]
    
    print("🤖 Testing Chatbot Backend with Structured Responses")
    print("=" * 60)
    
    for i, question in enumerate(test_questions, 1):
        print(f"\n📝 Test {i}: {question}")
        print("-" * 40)
        
        try:
            response = requests.post(
                f"{base_url}/chat",
                json={
                    "message": question,
                    "portfolio": "Sample portfolio with RELIANCE, TCS, INFY"
                },
                timeout=30
            )
            
            if response.status_code == 200:
                data = response.json()
                print("✅ Success!")
                print("📄 Response:")
                print(data.get('response', 'No response'))
            else:
                print(f"❌ Error: {response.status_code}")
                print(f"Details: {response.text}")
                
        except requests.exceptions.ConnectionError:
            print("❌ Connection Error: Make sure the backend is running on localhost:5000")
        except Exception as e:
            print(f"❌ Error: {str(e)}")
    
    # Test health endpoint
    print(f"\n🏥 Testing Health Endpoint")
    print("-" * 40)
    try:
        health_response = requests.get(f"{base_url}/health")
        if health_response.status_code == 200:
            print("✅ Health check passed!")
            print(json.dumps(health_response.json(), indent=2))
        else:
            print(f"❌ Health check failed: {health_response.status_code}")
    except Exception as e:
        print(f"❌ Health check error: {str(e)}")

if __name__ == "__main__":
    test_chatbot()
