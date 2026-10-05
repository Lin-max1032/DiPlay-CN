#!/usr/bin/env bash
set -euo pipefail

OFFICIAL_APK_URL="https://github.com/shihabal3amri/DiPlay/releases/download/v0.2.12/DiPlay-0.2.12.apk"
OFFICIAL_APK_SHA256="840f2d62fc2c7d10250555b99444da7e0539be3834f635c6aa22ecbd635f7cf4"

apk="$RUNNER_TEMP/official.apk"
assets="$RUNNER_TEMP/auth-assets"
curl -fsSL -o "$apk" "$OFFICIAL_APK_URL"
python3 scripts/extract_official_identity.py "$apk" "$assets" "$OFFICIAL_APK_SHA256"
export DIPLAY_AUTH_ASSETS_DIR="$assets"
python3 scripts/check_public_tree.py

if ! ./gradlew :shared:testDebugUnitTest :common:testDebugUnitTest \
    :mobile:lintRelease :mobile:assembleStandaloneRelease \
    --stacktrace > gradle.log 2>&1; then
  tail -200 gradle.log
  {
    echo "run ${GITHUB_RUN_ID:-manual} failed at $(date -u +%FT%TZ)"
    echo '--- compile/lint errors ---'
    grep -nE "^e: |error: |FAILURE:|What went wrong" -A 4 gradle.log | head -200 || true
    echo '--- failed tests ---'
    grep -rn --include='TEST-*.xml' -o '<failure' . | head -60 || true
    echo '--- gradle.log tail ---'
    tail -120 gradle.log
  } > failure.txt
  git config user.name "github-actions[bot]"
  git config user.email "41898282+github-actions[bot]@users.noreply.github.com"
  git checkout -b "ci/diagnostics-${GITHUB_RUN_ID:-manual}"
  git add failure.txt
  git commit -m "diagnostics ${GITHUB_RUN_ID:-manual}"
  git push origin "HEAD:ci/diagnostics-${GITHUB_RUN_ID:-manual}"
  exit 1
fi

python3 scripts/check_public_tree.py
python3 scripts/verify_apk_identity.py mobile/build/outputs/apk/release/mobile-release.apk
bash scripts/ci-release.sh
