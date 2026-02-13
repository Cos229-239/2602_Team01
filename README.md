# Class Project Template

Start here. Keep this README updated with your team name, members, and a short plan.

Team Name - Team 1 Campus 
Team Member(s) - Daniel Wetenkamp
Plan - I'll be creating an app using Android Studio and writing in Kotlin Multiplatform. The app will be a digital file cabinet of sort, but I want to make it highly customizeable by the user. It's to help keep all sorts of documents (pdfs), information, and even pictures saved in one place. For example: PDF of home, roof, HVAC system warranties; yr/mk/mdl of car(s) with oil weight, tire type/size, reminders for oil changes/maintenance; either pictures of or user-added info of their preferred company regarding HVAC/Mechanic/Lawncare/etc.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…