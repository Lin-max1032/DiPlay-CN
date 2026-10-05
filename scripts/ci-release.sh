#!/usr/bin/env bash
set -euo pipefail

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
