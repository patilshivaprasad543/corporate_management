#!/usr/bin/env python3
"""Generate a visual preview of CorporateTravel360 for the user."""

import json
import os
import urllib.request
from playwright.sync_api import sync_playwright

ARTIFACTS = "/opt/cursor/artifacts"
os.makedirs(ARTIFACTS, exist_ok=True)

PREVIEW_STEPS = [
    ("preview_01_login_gateway", "login-portal", None, None, "Login & 9 role portals", None),
    ("preview_02_employee_dashboard", "dashboard", "ROLE_EMPLOYEE", "traveler@acmetech.com", "Employee dashboard banner", None),
    ("preview_03_travel_request_wizard", "requests", "ROLE_EMPLOYEE", "traveler@acmetech.com", "4-step travel request wizard", "openNewRequestModal()"),
    ("preview_04_approvals_tabs", "approvals", "ROLE_APPROVER", "manager@acmetech.com", "Approvals hub with tabs", None),
    ("preview_05_boarding_pass_wallet", "itinerary", "ROLE_EMPLOYEE", "traveler@acmetech.com", "Wallet boarding pass", "openBoardingPassModal()"),
    ("preview_06_admin_command_center", "dashboard", "ROLE_COMPANY_ADMIN", "admin@acmetech.com", "Admin command center", None),
    ("preview_07_mobile_bottom_nav", "dashboard", "ROLE_EMPLOYEE", "traveler@acmetech.com", "Mobile bottom nav", None, {"width": 390, "height": 844}),
    ("preview_08_analytics", "analytics", "ROLE_SUPER_ADMIN", "superadmin@corporatetravel.com", "Executive analytics", None),
]


def login_token(email: str) -> tuple[str, str]:
    req = urllib.request.Request(
        "http://localhost:8080/api/auth/login",
        data=json.dumps({"usernameOrEmail": email, "password": "password123"}).encode(),
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    with urllib.request.urlopen(req) as resp:
        data = json.load(resp)
    token = data["data"]["accessToken"]
    role = data["data"]["roles"][0]
    return token, role


with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    default_viewport = {"width": 1440, "height": 900}
    context = browser.new_context(
        viewport=default_viewport,
        record_video_dir=ARTIFACTS,
        record_video_size=default_viewport,
    )
    page = context.new_page()

    captured = []

    for filename, tab, role_key, email, label, js_action, *rest in PREVIEW_STEPS:
        viewport = rest[0] if rest else default_viewport
        if viewport != default_viewport:
            page.set_viewport_size(viewport)

        page.goto("http://localhost:8080/", wait_until="networkidle")

        if email and role_key:
            token, api_role = login_token(email)
            page.evaluate(
                """([role, token]) => {
                    localStorage.setItem('corporate_jwt_token', token);
                    localStorage.setItem('corporate_user_role', role);
                    localStorage.setItem('corporate_splash_seen', 'true');
                }""",
                [api_role, token],
            )
            page.reload(wait_until="networkidle")

        if tab != "login-portal":
            page.evaluate(f"navigateToTab('{tab}')")
            page.wait_for_timeout(1500)

        if js_action:
            page.evaluate(js_action)
            page.wait_for_timeout(1200)

        path = f"{ARTIFACTS}/{filename}.png"
        page.screenshot(path=path, full_page=False)
        captured.append({"file": path, "label": label})
        page.wait_for_timeout(500)

        if viewport != default_viewport:
            page.set_viewport_size(default_viewport)

    video_path = page.video.path() if page.video else None
    context.close()
    browser.close()

    if video_path and os.path.exists(video_path):
        final_video = f"{ARTIFACTS}/corporate_travel_project_preview.mp4"
        os.replace(video_path, final_video)
        print(f"VIDEO:{final_video}")

    for item in captured:
        print(f"SHOT:{item['file']}:{item['label']}")
