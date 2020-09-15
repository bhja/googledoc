# googledoc
A simple JAVA program to read GOOGLE DOCS and print the output as json using OAuth.

Update the client_secrets.json with the credentials that can be downloaded from the Google developer console for the Client auth that is setup.

1.Create OAUTH Client for a Web Application and provide a REDIRECT URL.
2. Open the below URL in browser to generate the AUTHORIZATION CODE.substitute THE CLIENT_ID and REDIRECT URL.You will be prompted to authorize the request
   https://accounts.google.com/o/oauth2/auth?client_id=<CLIENT_ID>&redirect_uri=<REDIRECT_URL>&scope=https://www.googleapis.com/auth/documents.readonly&response_type=code&access_type=offline
3. Get the code and substitute the values in the following curl 
curl \
--request POST \
--data "code=<CODE FROM STEP 2>&client_id=<CLIENT_ID>&client_secret=<CLIENT_SECRET>&redirect_uri=<REDIRECT_URL>&grant_type=authorization_code&access_type=offline" \
https://oauth2.googleapis.com/token
4. Get the refresh_token from the response which would look something like this.
	{
  "access_token": "*************************************",
  "expires_in": 3599,
  "refresh_token": "***********************************",
  "scope": "https://www.googleapis.com/auth/drive.readonly",
  "token_type": "Bearer"
  }
5. Substitute the client_id, client_secret and refresh_token in the tokens.json and execute the code. 
    
