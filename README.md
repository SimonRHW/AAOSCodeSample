# AAOSCodeSample

基于AAOS系统，进行车辆信息读取、车辆驾驶模式适配、车辆行驶状态获取

https://source.android.com/docs/devices/automotive/driver_distraction/car_uxr
https://source.android.com/docs/devices/automotive/driver_distraction/consume

关于car service的一些指令，有需要可以看下，
https://cs.android.com/android/platform/superproject/+/master:packages/services/Car/service/src/com/android/car/CarShellCommand.java
例如：
adb shell dumpsys car_service emulate-driving-state drive  限制交互
adb shell dumpsys car_service emulate-driving-state park   解除限制

permission声明：
https://cs.android.com/android/platform/superproject/+/android-12.0.0_r34:packages/services/Car/service/AndroidManifest.xml

android:protectionLevel声明：
https://cs.android.com/android/platform/superproject/+/android-12.0.0_r34:frameworks/base/core/res/res/values/attrs_manifest.xml;l=241