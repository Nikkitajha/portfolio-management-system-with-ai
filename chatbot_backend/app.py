from flask import Flask, request, jsonify
from flask_cors import CORS
import os
import requests
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

app = Flask(__name__)
CORS(app)

# Get API key from .env
OPENROUTER_API_KEY = os.getenv('OPENROUTER_API_KEY')

# System prompt for PMS chatbot
SYSTEM_PROMPT = """You are an AI assistant for a Portfolio Management System (PMS). You help users with:
- Portfolio analysis and insights
- Investment recommendations and guidance
- Market trends and stock information
- Risk management strategies
- Financial planning advice
- Understanding portfolio metrics like ROI, AUM, P&L

IMPORTANT: Format your responses using markdown for better readability:
- Use ### for main topics (investment strategies, risk analysis, market trends)
- Use ## for subtopics (specific strategies, risk factors, sector performance)
- Use - or * for bullet points (lists of recommendations, factors, steps)
- Use **bold** for emphasis on key terms and important numbers
- Use [INFO] for helpful tips and general information
- Use [WARNING] for risk warnings and important cautions

Be professional, helpful, and provide actionable insights. If you don't have real-time data, clearly mention that and give general guidance. Structure complex information with clear headings and bullet points for easy understanding.
"""

# Health check API
@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({
        'status': 'healthy',
        'service': 'chatbot-backend'
    })

# Chat API
@app.route('/chat', methods=['POST'])
def chat():
    try:
        data = request.json
        user_message = data.get('message', '')
        user_portfolio = data.get('portfolio', '')

        if not user_message.strip():
            return jsonify({'error': 'Message cannot be empty'}), 400

        # Headers
        headers = {
            "Authorization": f"Bearer {OPENROUTER_API_KEY}",
            "Content-Type": "application/json"
        }

        # Enhanced user message with formatting instructions
        enhanced_message = f"""User Question: {user_message}

Please provide a structured response using:
- ### for main topics
- ## for subtopics  
- - for bullet points
- **bold** for emphasis
- [INFO] for helpful tips
- [WARNING] for important warnings

Make the response easy to read and understand."""

        # Payload
        payload = {
            "model": "openrouter/auto",  # safer free model
            "messages": [
                {
                    "role": "system",
                    "content": SYSTEM_PROMPT + f"\nUser Portfolio: {user_portfolio}"
                },
                {
                    "role": "user",
                    "content": enhanced_message
                }
            ],
            "max_tokens": 800,  # Increased for longer structured responses
            "temperature": 0.7
        }

        # API call
        response = requests.post(
            "https://openrouter.ai/api/v1/chat/completions",
            headers=headers,
            json=payload
        )

        # Debug if needed
        # print(response.text)

        result = response.json()

        if "choices" not in result:
            return jsonify({
                'error': 'Invalid API response',
                'details': result
            }), 500

        ai_response = result["choices"][0]["message"]["content"]

        return jsonify({
            'response': ai_response,
            'status': 'success'
        })

    except Exception as e:
        print("Error:", str(e))
        return jsonify({
            'error': 'Failed to process your request',
            'details': str(e)
        }), 500

# Run server
if __name__ == '__main__':
    app.run(debug=True, port=5000)

