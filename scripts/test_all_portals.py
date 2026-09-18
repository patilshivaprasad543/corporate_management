#!/usr/bin/env python3
"""Test all 9 CorporateTravel360 role portals and capture screenshots."""

import json
import os
import urllib.request
from playwright.sync_api import sync_playwright

ARTIFACTS = "/opt/cursor/artifacts"
os.makedirs(ARTIFACTS, exist_ok=True)

ROLES = [
    ("ROLE_EMPLOYEE", "traveler@acmetech.com", "employee"),
    ("ROLE_APPROVER", "manager@acmetech.com", "approver"),
    ("ROLE_TRAVEL_MANAGER", "travelmgr@acmetech.com", "travel_manager"),
    ("ROLE_FINANCE", "finance@acmetech.com", "finance"),
    ("ROLE_COMPANY_ADMIN", "admin@acmetech.com", "company_admin"),
    ("ROLE_SUPER_ADMIN", "superadmin@corporatetravel.com", "super_admin"),
    ("ROLE_HR", "hr@acmetech.com", "hr"),
    ("ROLE_VENDOR", "partner@indigoair.com", "vendor"),
    ("ROLE_SUPPORT", "support@corporatetravel.com", "support"),
]

results = []

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    page = browser.new_page(viewport={"width": 1440, "height": 900})

    page.goto("http://localhost:8080/", wait_until="networkidle")
    page.screenshot(path=f"{ARTIFACTS}/portal_login_gateway.png", full_page=False)

    for role, email, slug in ROLES:
        # API login verification
        req = urllib.request.Request(
            "http://localhost:8080/api/auth/login",
            data=json.dumps({"usernameOrEmail": email, "password": "password123"}).encode(),
            headers={"Content-Type": "application/json"},
            method="POST",
        )
        with urllib.request.urlopen(req) as resp:
            login_data = json.load(resp)

        token = login_data["data"]["accessToken"]
        api_ok = login_data.get("success", False)

        page.goto("http://localhost:8080/", wait_until="networkidle")
        page.evaluate(
            """([role, token]) => {
                localStorage.setItem('corporate_jwt_token', token);
                localStorage.setItem('corporate_user_role', role);
            }""",
            [role, token],
        )
        page.reload(wait_until="networkidle")

        # Dashboard view
        page.wait_for_timeout(1500)
        dash_path = f"{ARTIFACTS}/portal_{slug}_dashboard.png"
        page.screenshot(path=dash_path, full_page=False)

        # Login portal view
        page.evaluate("navigateToTab('login-portal')")
        page.wait_for_timeout(1200)
        portal_path = f"{ARTIFACTS}/portal_{slug}_identity.png"
        page.screenshot(path=portal_path, full_page=False)

        hero = page.locator(".portal-hero").first
        hero_visible = hero.count() > 0 and hero.is_visible()

        results.append(
            {
                "role": role,
                "email": email,
                "api_login": api_ok,
                "dashboard_screenshot": dash_path,
                "portal_screenshot": portal_path,
                "hero_visible": hero_visible,
            }
        )
        print(f"OK {role}: api={api_ok} hero={hero_visible}")

    browser.close()

summary_path = f"{ARTIFACTS}/portal_test_results.json"
with open(summary_path, "w") as f:
    json.dump(results, f, indent=2)

print(f"\nTested {len(results)} portals. Results: {summary_path}")
