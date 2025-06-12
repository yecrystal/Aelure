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
    
    # Create or get user
    user = db.query(User).filter(User.google_id == user_data["sub"]).first()
    if not user:
        user = User(
            google_id=user_data["sub"],
            email=user_data["email"],
            name=user_data["name"]
        )
        db.add(user)
        db.commit()
        db.refresh(user)

    response = RedirectResponse(url="/user/me")
    response.set_cookie("api_key", user.api_key, httponly=True)
    return response

@router.get("/logout")
def logout(response: Response):
    response = RedirectResponse(url="/")
    response.delete_cookie("api_key")
    return response


