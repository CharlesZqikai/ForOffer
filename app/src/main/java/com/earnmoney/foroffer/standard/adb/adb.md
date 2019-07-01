# adb monkey 禁止下拉控制栏
adb shell settings put global policy_control immersive.full=*   关闭手机的状态栏
adb shell settings put global policy_control null               开启手机状态栏
           
