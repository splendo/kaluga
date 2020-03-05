## Permissions

This library provide support for out-of-the-box access to device permissions.

Supported device features:
 - Bluetooth
 - Calendar
 - Camera
 - Contacts
 - Location (both with and without background)
 - Microphone
 - Notifications
 - Storage (aka Photos on iOS)

### Usage
Permissions can be requested through an instance of the `Permissions` class. This class is instantiated with a `PermissionsBuilder`, which defaults to requesting permissions for the Application Context (Android) or Main NSBundle (iOS).

Permissions are modelled as a `Flowable` `State`, providing updates should the state change during observation. A permission can either be `Allowed` or `Denied`. A `Denied` permission can in turn be `Locked`, preventing the app from requesting it, or `Requestable`.

To request a permission, simply call `permissions[$type].request()`, where `$type` is the `Permission` you want to request. This function will return true or false when the user has given or explicitly denied the permission. This function returns false if the user denies any part of the permission, even if the other permissions are granted. For instance the `Location` permission will be denied if background support is requested but only foreground is given. If a request fails, a new request with a more limited scope can then be made.