Note for CMC Code Challenge
Ryan TSUI 8 Nov 2023

1.	The project has been updated to new Gradle and SDK, below is the environment detail.

Android Studio 2022.3.1
Gradle 7.6.3
Android gradle plugin 7.4.2
Compile SDK 33
Kotlin JDK 1.8.20
Java JDK 17

2.	Databinding library is needed for XML layout element binding, previous library Kotlin Android Extensions is deprecated. Please refer to:
      https://developer.android.com/topic/libraries/view-binding/migration

Databinding library is only used for view binding. As per requirement, view data is still manipulated and handled by program in View.

3.	Inputting length (not field length) is limited to 10 digits intentionally, to prevent incorrect behaviours.



Thought for improvements:

1.	Consider using Websocket or server side pushing to update data, this will help improving battery perform for the app.

2.	A sliding chart could be added for easy navigation of price movement.
