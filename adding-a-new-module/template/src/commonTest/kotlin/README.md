## Here tests common tests

To run all common tests:
1. Run > Edit Configurations. The Run/Debug Configurations dialog appears.
2. Press "+" button and select "Gradle".
3. In the right part of dialog enter name.
4. Fill in the Gradle project field (kaluga:<your module>).
5. In a field "Tasks" enter cleanAllTests allTests.
6. Press "Ok" or "Apply". Now you can choose and run configuration.

Please note that this scheme will run all gradle test tasks including iOS. Use [test filtering](https://docs.gradle.org/current/userguide/java_testing.html#test_filtering) if you want to run some specific tests.   
Use cleanJvmTest jvmTest if you want to run only JVM tests.
