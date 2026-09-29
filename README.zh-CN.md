# DiPlay 中文版

为兼容的 Android 车机提供有线和无线 CarPlay，并采用熟悉的 DiAuto 风格界面。本分支基于上游 `v0.2.6`，已将应用内的主要界面、连接向导、设置、弹窗、通知和诊断页汉化为简体中文。

[上游项目](https://github.com/shihabal3amri/DiPlay) · [安装说明](docs/INSTALL.md) · [兼容性与故障排查](docs/COMPATIBILITY.md) · [构建说明](docs/BUILD.md)

## 0.2.6 中文版

请将应用安装在车机上，而不是 iPhone 上。日常使用无需越狱、转接盒、Mac、账号或认证服务器；车机必须允许安装 APK。有线连接支持 Android 9 及以上版本，无线连接可使用 Wi-Fi Direct 或车机内置热点，其中 Wi-Fi Direct 需要 Android 10 及以上版本。

- 支持 USB 有线和无线 CarPlay，并在本地完成认证。
- 支持比亚迪 HUD 导航，可在已验证的固件上显示箭头、距离和道路名称。
- 支持车机热点、自动地址发现、固定信道回退和成功配置记忆。
- 可调整图标/文字大小、分辨率和帧率；应用显示设置时会重新连接 CarPlay。
- 可在本地导出诊断报告，只有你主动分享时才会发送。
- 可与 DiAuto 分开安装，但同一时间只应运行一个投屏应用。

## 风险说明

本项目不是 Apple 认证产品。APK 使用从公开 Carlinkit 固件中提取的实验性配件身份，而不是为 DiPlay 新签发的 MFi 身份；打包在 APK 中的私钥可以被提取。未来 iOS 版本是否继续接受该身份、不同车机上的可靠性以及该身份是否适合广泛分发均无法保证。

已知问题包括部分车机偶发音频中断、画面卡顿或忽略图标大小设置。建议先使用 30 fps 并关闭 HEVC，必要时降低分辨率。

## 从源码构建

需要 JDK 25、Android SDK 37、NDK 28.2.13676358，以及仓库自带的 Gradle Wrapper。

```sh
./gradlew :shared:testDebugUnitTest :common:testDebugUnitTest :mobile:lintDebug :mobile:assembleDebug
```

生成的源码构建 APK 不包含配件身份，因此不能独立连接 CarPlay。需要独立车测包时，请按照[构建说明](docs/BUILD.md)从仓库外部提供运行时认证资源；认证资源和 Android 签名密钥均不得提交到 Git。

## 来源与许可证

项目基于 [xcertplay](https://github.com/shilapi/xcertplay)，采用 GPL-3.0 许可证。主页和设置界面改编自 [DiAuto](https://github.com/shihabal3amri/DiAuto)，采用 AGPL-3.0 许可证。分发修改版本时请保留仓库中的许可证和第三方声明。

CarPlay 及其图标归 Apple Inc. 所有。本项目与 Apple 或比亚迪没有隶属或背书关系。
