# Chatbot Backend for Portfolio Management System

This Python Flask backend provides AI-powered chat functionality using OpenRouter API for the Portfolio Management System.

## Features

- **OpenRouter Integration**: Uses GPT-3.5-turbo via OpenRouter for intelligent responses
- **Free & Open Source**: No API key costs or limits with OpenRouter
- **Portfolio-Specific AI**: Custom system prompt optimized for portfolio management queries
- **RESTful API**: Clean endpoints for chat and health checks
- **CORS Support**: Ready for frontend integration

## Setup Instructions

### 1. Install Dependencies

```bash
cd chatbot_backend
pip install -r requirements.txt
```

### 2. Configure OpenRouter API Key

1. Copy the environment file:
```bash
cp .env.example .env
```

2. Edit `.env` and add your OpenRouter API key:
```
OPENROUTER_API_KEY=your_actual_openrouter_api_key_here
```

### 3. Get OpenRouter API Key

1. Go to [OpenRouter.ai](https://openrouter.ai)
2. Sign up for a free account
3. Navigate to API Keys section
4. Copy your API key to the `.env` file

### 4. Run the Server

```bash
python app.py
```

The server will start on `http://localhost:5000`

## API Endpoints

### Health Check
- **GET** `/health`
- Returns server status

### Chat
- **POST** `/chat`
- **Body**: `{ "message": "your question here" }`
- **Response**: `{ "response": "AI response", "status": "success" }`

## Integration with Frontend

The frontend is already configured to communicate with this backend:
- Frontend connects to `http://localhost:5000`
- AI chat button in the user dashboard
- Chat interface slides in from the right side

## Usage

1. Start this Python backend first
2. Start your Spring Boot application
3. Open the user dashboard
4. Click the "AI Assistant" button
5. Ask questions about portfolio analysis, investments, market trends, etc.

## Example Questions

- "What makes a good investment portfolio?"
- "How do I analyze ROI performance?"
- "What are current market trends?"
- "How can I reduce portfolio risk?"
- "Explain diversification strategies"

## Advantages of OpenRouter

- **Free Tier**: Generous free tier with multiple models
- **No Credit Limits**: Unlike OpenAI's credit system
- **Multiple Models**: Access to various AI models through one API
- **Open Source**: Community-driven and transparent

## Troubleshooting

- **Connection Error**: Ensure the Python server is running on port 5000
- **API Error**: Check your OpenRouter API key in `.env` file
- **CORS Issues**: The backend includes CORS support for frontend integration

## Dependencies

- Flask 2.3.3 - Web framework
- Flask-CORS 4.0.0 - Cross-origin resource sharing
- openrouter 1.0.1 - OpenRouter API client
- requests 2.31.0 - HTTP requests library
- python-dotenv 1.0.0 - Environment variable management
