from authlib.integrations.httpx_client import AsyncOAuth2Client
from backend.config import GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, GOOGLE_REDIRECT_URI

GOOGLE_AUTH_URL = "https://accounts.google.com/o/oauth2/v2/auth"
GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token"
GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo"

client = AsyncOAuth2Client(
    client_id=GOOGLE_CLIENT_ID,
    client_secret=GOOGLE_CLIENT_SECRET,
    client_kwargs={"scope": "openid email profile"},
    scope=[]"openid", "email", "profile"]
)

def get_google_auth_url():
    url, _ = client.create_authorization_url(GOOGLE_AUTH_URL)
    return url

async def handle_google_callback(code: str):
    token = await client.fetch_token(
        GOOGLE_TOKEN_URL,
        code=code,
        grant_type="authorization_code",
        client_id=GOOGLE_CLIENT_ID,
        client_secret=GOOGLE_CLIENT_SECRET
    )
    resp = await client.get(GOOGLE_USER_INFO_URL, token=token)
    user_info = resp.json()
    return user_info