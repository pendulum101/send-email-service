# Email Handling API
## Overview

This API endpoint is designed to handle the submission of a list of email addresses (EmailDTO). It uses the HTTP POST method to receive a JSON payload containing the email addresses to be processed. The endpoint is responsible for validating the input and returning an appropriate response.

## Endpoint
#### POST /api/v1/sendemail

Use this endpoint to submit a list of email addresses for processing.

### Request

    Method: POST
    Path: /api/v1/sendemail
    Request Body: JSON array containing a list of EmailDTO objects.

#### Sample RequestBody
```JSON
[
  {
    "emailAddr": "user1@example.com"
  },
  {
    "emailAddr": "user2@example.com"
  }
  // Add more email objects as needed
]
```

### Response

    HTTP Status Codes:
        200 OK: The request was successful, and the emails were sent but no new email created in Database.
        201 Created: The request was successful, and the emails were processed.
        404 Not Found: The email addresses are invalid.

### Usage

Send a POST request to the `/api/v1/sendemail` endpoint with a JSON payload containing the list of EmailDTO objects.

Example using cURL:
```bash
curl -X POST -H "Content-Type: application/json" -d @request_payload.json http://localhost:8080/api/v1/sendemail
```

### Validation
The payload is validated to ensure that it contains a JSON Body with a list of valid Email Addresses