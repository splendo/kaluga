## Template 
This project includes all common dependencies and settings for kaluga subproject.

#Steps for adding new subproject
1. CopyPaste template subproject, select name for your subproject (we recommend to use only lowercase characters).
2. `<your subproject>src/androidLibMain/AndroidManifest.xml` change `com.splendo.kaluga.template` to your subproject package name.	
3. `<your subproject>src/androidLibAndroidTests/AndroidManifest.xml` change `com.splendo.kaluga.template` to your subproject package name.
4. `<your subproject>src/androidLibAndroidTests/kotlin/TestActivity.kt` change package name from `com.splendo.kaluga.template` to `com.splendo.kaluga.your module`.
5. Include your subproject to kaluga project
	go to `kaluga/settings.gradle/kts`
	And add in the end of the file:
	`include(":<your subproject>")`.
6. Add your source code. For more derails check README.md in source root and each platform folder.
7. Remove template README files from your subproject when you don't need them anymore.
8. Don't forget to update copyright if files were imported from another project. To do that use **Code > Update Copyright...**.
9. Add documentation to your code if needed.
10. Add an example of usage to the example project.
