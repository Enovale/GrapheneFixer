# GrapheneFixer

GrapheneOS prevents most XPosed modules from operating correctly because XPosed trips DCL code spawning protection.

This module allows you to configure all exploit protection for system apps, so you can allow DCL for these problematic system apps.

*Note*: The Settings app is forced to always allow memory DCL.

## Installation and Usage

### 1. Download the APK
Download the latest APK from [here](https://github.com/Enovale/GrapheneFixer/releases/latest) and install it on your device.

### 2. Enable the Module
Enable the module in either Xposed or LSPosed.

### 3. Enable the right scopes
Select the "Recommended" scopes, i.e. the `android`, and `com.android.settings` scopes.

### 4. Reboot
Reboot your device for the changes to take effect.

### 5. Enable DCL for needed modules
Whenever a module scopes to a system app, make sure to go into that app's info and enable DCL before rebooting.

---
## License
This project is licensed under the [MIT License](LICENSE).