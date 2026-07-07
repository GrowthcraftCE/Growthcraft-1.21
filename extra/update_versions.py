#!/usr/bin/env python3
"""
Updates GrowthcraftCE/Growthcraft-Updates/growthcraft-versions.json as part of the build/release.

- Reads the current mod and Minecraft versions from gradle.properties.
- Optionally augments entries with End of Support dates using last commit dates
  from the corresponding release branches for older series.
- Commits the updated JSON back to the Growthcraft-Updates repository via the
  GitHub Contents API.

Environment variables (can be overridden by the workflow):
- GITHUB_TOKEN: Required to write to the Growthcraft-Updates repo.
- UPDATES_OWNER: GitHub organization/user owning the updates repo (default: GrowthcraftCE)
- UPDATES_REPO: Updates repo name (default: Growthcraft-Updates)
- UPDATES_PATH: Path to the JSON file within the repo (default: growthcraft-versions.json)
- UPDATES_BRANCH: Branch to read/write (default: master)

Schema assumptions:
- We expect either the entire JSON to be a mapping of series → info, or a top-level
  object containing a key "versions" with that mapping. We preserve existing structure.
- For each series key (e.g., "1.21", "1.19"), we store fields:
  { "minecraft": "1.21.1", "latest": "1.21.1.7", "stable": "1.21.1.7", "endOfSupport": "TBD" }

Notes:
- EoS dates are computed for legacy series using the last commit date of the release
  branches in the corresponding repos. Series mapping is adjustable below.
- If the JSON file does not yet exist, it will be created with the current series only.
"""
from __future__ import annotations
import base64
import json
import os
import sys
from datetime import datetime, timezone
from typing import Dict, Tuple, Any
import urllib.request

REPO_API_BASE = "https://api.github.com"
UA = "Growthcraft-Updates-AutoWriter"

PROJECT_ROOT = os.path.dirname(os.path.dirname(__file__))
GRADLE_PROPS = os.path.join(PROJECT_ROOT, "gradle.properties")

# EoS computation targets: series → (repo, branch)
# The current series (e.g., 1.21) typically stays as "TBD" while active.
EOS_TARGETS: Dict[str, Tuple[str, str]] = {
    "1.19": ("Growthcraft-1.19", "release"),
    "1.18": ("Growthcraft-1.18", "release"),
    # 1.12 is maintained by Doenerstyle; keep as TBD by policy.
}


def read_gradle_properties(path: str) -> Dict[str, str]:
    props: Dict[str, str] = {}
    with open(path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            if "=" in line:
                k, v = line.split("=", 1)
                props[k.strip()] = v.strip()
    return props


def gh_api_request(url: str, token: str | None, method: str = "GET", data: bytes | None = None, content_type: str | None = None) -> Any:
    headers = {"User-Agent": UA, "Accept": "application/vnd.github+json"}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    if content_type:
        headers["Content-Type"] = content_type
    req = urllib.request.Request(url, headers=headers, method=method)
    if data is not None:
        req.data = data
    with urllib.request.urlopen(req) as resp:
        return json.loads(resp.read().decode("utf-8"))


def get_last_commit_date(owner: str, repo: str, branch: str, token: str | None) -> str:
    url = f"{REPO_API_BASE}/repos/{owner}/{repo}/commits?sha={branch}&per_page=1"
    data = gh_api_request(url, token)
    if not data:
        raise RuntimeError(f"No commits found for {repo}:{branch}")
    commit = data[0]["commit"]
    iso = commit.get("committer", {}).get("date") or commit.get("author", {}).get("date")
    if not iso:
        raise RuntimeError(f"Could not find commit date for {repo}:{branch}")
    dt = datetime.fromisoformat(iso.replace("Z", "+00:00")).astimezone(timezone.utc)
    return dt.strftime("%d %b %Y").upper()


def fetch_versions_json(owner: str, repo: str, path: str, branch: str, token: str | None) -> Tuple[Dict[str, Any], str | None]:
    url = f"{REPO_API_BASE}/repos/{owner}/{repo}/contents/{path}?ref={branch}"
    try:
        resp = gh_api_request(url, token)
        content_b64 = resp.get("content", "")
        sha = resp.get("sha")
        content = base64.b64decode(content_b64.encode("utf-8")).decode("utf-8")
        try:
            data = json.loads(content)
        except json.JSONDecodeError:
            # If file exists but is not valid JSON, start over cautiously
            data = {}
        return data, sha
    except Exception:
        # Assume file does not exist or cannot be fetched; start fresh
        return {}, None


def to_versions_mapping(data: Dict[str, Any]) -> Tuple[Dict[str, Any], bool]:
    """Return (versions_mapping, is_nested). If nested, mapping is under data['versions']."""
    if isinstance(data, dict) and "versions" in data and isinstance(data["versions"], dict):
        return data["versions"], True
    elif isinstance(data, dict):
        return data, False
    else:
        return {}, False


def commit_versions_json(owner: str, repo: str, path: str, branch: str, token: str | None, data: Dict[str, Any], sha: str | None, message: str) -> Any:
    content = json.dumps(data, indent=2, ensure_ascii=False) + "\n"
    content_b64 = base64.b64encode(content.encode("utf-8")).decode("utf-8")
    url = f"{REPO_API_BASE}/repos/{owner}/{repo}/contents/{path}"
    body = {
        "message": message,
        "content": content_b64,
        "branch": branch,
    }
    if sha:
        body["sha"] = sha
    return gh_api_request(url, token, method="PUT", data=json.dumps(body).encode("utf-8"), content_type="application/json")


def main() -> int:
    token = os.environ.get("GITHUB_TOKEN")
    updates_owner = os.environ.get("UPDATES_OWNER", "GrowthcraftCE")
    updates_repo = os.environ.get("UPDATES_REPO", "Growthcraft-Updates")
    updates_path = os.environ.get("UPDATES_PATH", "growthcraft-versions.json")
    updates_branch = os.environ.get("UPDATES_BRANCH", "master")

    if not os.path.exists(GRADLE_PROPS):
        print(f"gradle.properties not found at {GRADLE_PROPS}", file=sys.stderr)
        return 1

    props = read_gradle_properties(GRADLE_PROPS)
    mod_version = props.get("mod_version")
    mc_version = props.get("minecraft_version")
    mc_short = props.get("minecraft_version_short")
    if not (mod_version and mc_version and mc_short):
        print("Missing required gradle.properties entries (mod_version, minecraft_version, minecraft_version_short)", file=sys.stderr)
        return 1

    # Fetch existing JSON
    data, sha = fetch_versions_json(updates_owner, updates_repo, updates_path, updates_branch, token)
    versions_map, is_nested = to_versions_mapping(data)

    # Build or update current series entry
    current_entry = versions_map.get(mc_short, {}) if isinstance(versions_map, dict) else {}
    current_entry["minecraft"] = mc_version
    current_entry["latest"] = mod_version
    current_entry["stable"] = mod_version
    # Default EoS TBD for active series
    current_entry.setdefault("endOfSupport", "TBD")
    # Timestamp for audit
    current_entry["updatedAt"] = datetime.now(timezone.utc).strftime("%Y-%m-%dT%H:%M:%SZ")

    versions_map[mc_short] = current_entry

    # Augment EoS for known legacy series
    for series, (repo, branch) in EOS_TARGETS.items():
        try:
            eos_date = get_last_commit_date("GrowthcraftCE", repo, branch, token)
            entry = versions_map.get(series, {})
            entry.setdefault("minecraft", mc_version if series == mc_short else None)
            entry["endOfSupport"] = eos_date
            versions_map[series] = entry
        except Exception as e:
            print(f"[WARN] EoS for {series}: {e}", file=sys.stderr)

    # Write back into the full data structure, preserving nesting if used
    if is_nested:
        data["versions"] = versions_map
    else:
        data = versions_map

    message = f"chore: update versions for Minecraft {mc_version} → {mod_version}"
    try:
        commit_versions_json(updates_owner, updates_repo, updates_path, updates_branch, token, data, sha, message)
    except Exception as e:
        print(f"Failed to commit updates JSON: {e}", file=sys.stderr)
        return 1

    print(f"Updated {updates_owner}/{updates_repo}:{updates_branch}/{updates_path} for series {mc_short} → {mod_version}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
