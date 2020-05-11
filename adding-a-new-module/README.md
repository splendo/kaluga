## Template 
This **project** includes all common dependencies and settings for kaluga module.

#Steps for adding new module
1. CopyPaste template module, select name for your module (we recommend to use only lowercase characters).
2. `<your module>src/androidLibMain/AndroidManifest.xml` change `com.splendo.kaluga.template` to your module package name.	
3. `<your module>src/androidLibAndroidTests/AndroidManifest.xml` change `com.splendo.kaluga.template` to your module package name.
4. `<your module>src/androidLibAndroidTests/kotlin/TestActivity.kt` change package name from `com.splendo.kaluga.template` to `com.splendo.kaluga.your module`.
5. Include your module to kaluga project
	go to `kaluga/settings.gradle/kts`
	And add in the end of the file:
	`include(":<your module>")`.
6. Add your source code.
    * <a name="commonMain-sources">**commonMain**</a> - non-platform-specific implementation and `expect` declaration.
    * <a name="iosMain-sources">**iosMain**</a> - iOS specific implementation.
    * <a name="androidLibMain-sources">**androidLibMain**</a> - Android specific implementation.
    * <a name="jvmMain-sources">**jvmMain**</a> - jvm specific implementation.
    * <a name="jsMain-sources">**jvmMain**</a> - js specific implementation.
7. Remove template README files from your module when you don't need them anymore.
8. Don't forget to update copyright if files were imported from another project. To do that use **Code > Update Copyright...**.
9. Add documentation to your code if needed.
10. Add an example of usage to the example project.
