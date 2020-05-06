## Template 
This project includes all common dependencies and settings for kaluga subproject.

#Steps for adding new subproject
1. CopyPaste `teplete` subproject, select name for your subproject (we recommend to use only lowercase characters)
2. `<your subproject/>src/androidLibMain/AndroidManifest.xml`
	change `com.splendo.kaluga.template` to your subproject package name
3. Include your subproject to kaluga project
	go to `kaluga/settings.gradle/kts`
	And add in the end of the file:
	`include(":<your subproject>")`
4. Add your source code. For more derails check README.md in source root and each platform folder.
5. Remove this markdown file from your subproject.