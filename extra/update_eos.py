#!/usr/bin/env python3
"""
Updates the "End of Support" dates in README.md based on the last commit date
of specified GrowthcraftCE repos/branches.

Notes:
- Growthcraft 4 (1.12.2) is intentionally left as "TBD" because it remains
  supported by Doenerstyle.
- Default branch name assumed is "release". Adjust the mapping below if your
  release branch differs.

Usage:
  python extra/update_eos.py                # anonymous (subject to rate limits)
  GITHUB_TOKEN=xxxx python extra/update_eos.py

The script updates the README.md in-place.
"""
from __future__ import annotations
import json
import os
import re
import sys
import urllib.request
from datetime import datetime, timezone
from typing import Dict, List, Tuple

REPO_OWNER = "GrowthcraftCE"
README_PATH = os.path.join(os.path.dirname(os.path.dirname(__file__)), "README.md")

# Map README section headers to (repo, branch)
TARGETS: List[Tuple[str, str, str]] = [
    ("### Growthcraft 8 (Minecraft 1.19.4)", "Growthcraft-1.19", "release"),
    ("### Growthcraft 7 (Minecraft 1.18)", "Growthcraft-1.18", "release"),
    # Growthcraft 4 (1.12.2) is left as TBD per policy.
]

API_BASE = "https://api.github.com"
UA = "Growthcraft-EOS-Updater-Python"

def get_last_commit_date(owner: str, repo: str, branch: str, token: str | None) -> str:
    url = f"{API_BASE}/repos/{owner}/{repo}/commits?sha={branch}&per_page=1"
    req = urllib.request.Request(url, headers={"User-Agent": UA})
    if token:
        req.add_header("Authorization", f"Bearer {token}")
    with urllib.request.urlopen(req) as resp:
        data = json.loads(resp.read().decode("utf-8"))
    if not data:
        raise RuntimeError(f"No commits found for {repo}:{branch}")
    commit = data[0]["commit"]
    # Prefer committer date; fall back to author date
    iso = commit.get("committer", {}).get("date") or commit.get("author", {}).get("date")
    if not iso:
        raise RuntimeError(f"Could not find commit date for {repo}:{branch}")
    dt = datetime.fromisoformat(iso.replace("Z", "+00:00")).astimezone(timezone.utc)
    return dt.strftime("%d %b %Y").upper()

def update_readme_section(readme_lines: List[str], header: str, date_str: str) -> List[str]:
    try:
        idx = readme_lines.index(header + "\n") if not readme_lines.count(header) else readme_lines.index(header)
    except ValueError:
        # Also try without trailing newline sensitivity
        try:
            idx = next(i for i, l in enumerate(readme_lines) if l.strip() == header.strip())
        except StopIteration:
            raise RuntimeError(f"Header not found in README: {header}")

    # Find the 'End of Support:' line after the header, before the next '### '
    i = idx
    while i < len(readme_lines):
        line = readme_lines[i]
        if i > idx and line.startswith("### "):
            break
        if line.strip().startswith("End of Support:"):
            # Preserve trailing double spaces for markdown line break
            readme_lines[i] = f"End of Support: {date_str}  \n"
            return readme_lines
        i += 1

    raise RuntimeError(f"Could not find 'End of Support:' line after header: {header}")


def main() -> int:
    token = os.environ.get("GITHUB_TOKEN")
    if not os.path.exists(README_PATH):
        print(f"README not found at {README_PATH}", file=sys.stderr)
        return 1

    with open(README_PATH, "r", encoding="utf-8") as f:
        lines = f.readlines()

    for header, repo, branch in TARGETS:
        try:
            date_str = get_last_commit_date(REPO_OWNER, repo, branch, token)
            print(f"{repo}:{branch} → {date_str}")
            lines = update_readme_section(lines, header, date_str)
        except Exception as e:
            print(f"[WARN] {e}", file=sys.stderr)

    with open(README_PATH, "w", encoding="utf-8") as f:
        f.writelines(lines)

    print("Done. Review README.md to confirm the updated 'End of Support' dates.")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
