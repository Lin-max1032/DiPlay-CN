#!/usr/bin/env bash
set -euo pipefail

# Derive GH_TOKEN from the checkout credential (the workflow intentionally avoids
# GitHub template expressions, which trip the local MCP relay when pushed through it).
if [ -z "${GH_TOKEN:-}" ]; then
  b64=$(git config --get "http.https://github.com/.extraheader" 2>/dev/null | awk '{print $NF}' || true)
  if [ -n "${b64:-}" ]; then
    GH_TOKEN=$(printf '%s' "$b64" | base64 -d | cut -d: -f2)
    export GH_TOKEN
  fi
fi

version=$(grep -o 'versionName = "[^"]*"' mobile/build.gradle.kts | cut -d'"' -f2)
apk="DiPlay-cn-v${version}.apk"
cp mobile/build/outputs/apk/release/mobile-release.apk "$apk"
sha256sum "$apk" > "$apk.sha256"
cp docs/GITHUB_RELEASE.md release-notes.md
sed -i "s/{{VERSION}}/${version}/g" release-notes.md

gh release delete "v${version}" --repo "$GITHUB_REPOSITORY" --yes --cleanup-tag || true
gh release create "v${version}" \
  "$apk#DiPlay CN IPv4-only" \
  "$apk.sha256#SHA-256" \
  --repo "$GITHUB_REPOSITORY" \
  --title "DiPlay CN ${version} (IPv4-only)" \
  --notes-file release-notes.md
