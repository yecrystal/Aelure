# FastAPI app entry point
# Mounts routers and adds middleware

from fastapi import FastAPI
from backend.routes import auth, match, user

app = FastAPI(title="Aelure", version="0.1.0")

app.include_router(auth.router, prefix="/auth", tags=["Auth"])
app.include_router(match.router, prefit="/match", tags=["Match"])
app.include_router(user.router, prefix="/user", tags=["User"])

@app.get("/")
def read_root():
    return {"message": "Welcome to Aelure."}