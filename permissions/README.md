## Permissions

This library provide support for out-of-the-box access to device permissions.

Supported device features:
 - bluetooth (android/ios only)
 - TODO:
    * Location

### Usage
`Permissions` class is used to provide platform specific builder. 
`Permissions` instance provides a set of methods `get[Deveice-feature-name]Manager()`,
which returns instance of `PermissionManager`. `PermissionManager` implementation provides set of methods:

 - `fun checkSupport(): Support` - returns `Support` object which specifies if this feature is supported by device
 - `fun checkPermit(): Permit` - returns `Permit` object which specifies if user allows/denies access to this device feature
 - `fun openSettings()` - call this to open device settings to allow user to change feature settings

#### TODO:
 - finish tests for ios bluetooth
 - provide function request user permission