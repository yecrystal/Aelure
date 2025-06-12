from fastapi import APIRouter, Request, Depends, HTTPException
from fastapi.responses import RedirectResponse
from backend.services.oauth_client import get_google_auth_url, handle_google_callback

router = APIRouter()

@router.get("/login")
def login():
    auth_url = get_google_auth_url()
    return RedirectResponse(auth_url)

@router.get("/callback")
async def callback(request: Request):
    code = request.query_params.get("code")
    if not code:
        raise HTTPException(status_code=400, detail="Authorization code not found.")
    user_data = await handle_google_callback(code)
    return {"message": "Login successful", "user": user_data}
