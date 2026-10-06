# DiPlay {{VERSION}}（SurfaceView 兜底测试版）

基于**官方 DiPlay v0.2.13 源码**，仅追加一个显示路径补丁：当车机 ROM 不给应用窗口硬件加速时，自动把 CarPlay 视频输出从 TextureView 切换到 SurfaceView。包名 `com.shihab.diplay`，与官方版同包，可直接覆盖安装（本版 versionCode 33 > 官方 32）。

**为什么需要这个补丁**

部分 Android 9 车机（实测：长安 CS75PLUS 2021 款 / 梧桐 S311_ICA ROM）不向应用窗口提供硬件加速：`W TextureView: A TextureView or a subclass can only be used with hardware acceleration enabled.` 在这种窗口里，TextureView 永远不会产生 SurfaceTexture（`onSurfaceTextureAvailable` 不回调），视频管线拿不到输出面 → CarPlay 会话建立、音频正常、视频包以 ~20fps 到达，但画面全黑（上游 issue #278 的典型症状之一）。SurfaceView 不依赖窗口级硬件加速，由 SurfaceFlinger 直接合成，是这类 ROM 上视频播放的通用路径。

**本版更新**

- 新增：检测到窗口 `isHardwareAccelerated=false` 时，视频层自动切换为 SurfaceView（`attachSurface` 直接使用 `SurfaceHolder.getSurface()`，黑边/信箱区域按既有 `contentRect()` 计算摆位），并在诊断日志记录（`Video window hardwareAccelerated=…` / `Falling back to SurfaceView video output`）。
- 除显示路径外，其余代码与官方 v0.2.13 一致（连接、音频、触控、设置等全部沿用官方实现）。
- 补丁全文：`patches/sw-video-fallback.patch`（基于官方源码树，可 `git apply`）。

**下载**

- APK：本 Release 附带的 `DiPlay-v{{VERSION}}.apk`
- SHA-256：同目录 `.sha256` 文件

**给长安 CS75PLUS（21 款）车机安装的注意**

1. 本 APK 的签名与本仓库 CI 的通用测试签名一致，**装车机前必须用 91ee 证书重签（v1 签名）**：车机只认证书序列号 `91EE0710F45B5E2E`。
2. 覆盖安装需保持同证书 + 更高 versionCode（本版 33，官方 0.2.13 为 32）。
3. 安装后可通过应用内「诊断报告」核验：日志应出现 `Falling back to SurfaceView video output`，且视频统计 `shown` 应为正数。

**免责声明（简）**

独立社区项目，非 Apple 认证产品，与 Apple Inc.、比亚迪及其关联公司无隶属、合作或授权关系；“Apple”“CarPlay”“iPhone” 是 Apple Inc. 的商标。本项目仅用于个人学习与技术研究，不含 Apple 专有代码；运行所用的公开渠道配件身份数据说明见仓库文档。软件按“现状”提供、不作任何保证；请在停车时安装与设置，驾驶中请勿操作。基于 xcertplay（GPL-3.0），再分发须保留许可与署名信息。