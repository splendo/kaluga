## Template 
This project includes all common dependencies and settings for kaluga subproject.

#Steps for adding new subproject
1. CopyPaste `teplete` subproject, select name for your subproject (we recommend to use only lowercase characters).
2. `<your subproject/>src/androidLibMain/AndroidManifest.xml` change `com.splendo.kaluga.template` to your subproject package name.	
3. `<your subproject/>src/androidLibAndroidTests/AndroidManifest.xml` change `com.splendo.kaluga.template` to your subproject package name.
4. `<your subproject/>src/androidLibAndroidTests/kotlin/TestActivity.kt` change package name from `com.splendo.kaluga.template` to `com.splendo.kaluga.your module`.
5. Include your subproject to kaluga project
	go to `kaluga/settings.gradle/kts`
	And add in the end of the file:
	`include(":<your subproject>")`.
6. Add your source code. For more derails check README.md in source root and each platform folder.
7. Remove this markdown file from your subproject.
8. Don't forget to add copyright if files were imported from another project
```text
/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands
 
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
  
 */
``` 
9. Add documentation to your code if needed.
10. Add an example opf usage to the example project.
