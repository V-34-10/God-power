# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep OkHttp's platform implementations
-keep class okhttp3.internal.platform.* { *; }

# Bouncy Castle
-keep class org.bouncycastle.jsse.** { *; }
-keep interface org.bouncycastle.jsse.** { *; }

# Conscrypt
-keep class org.conscrypt.** { *; }
-keep interface org.conscrypt.** { *; }

# OpenJSSE (usually not necessary for Java 11+)
-keep class org.openjsse.** { *; }
-keep interface org.openjsse.** { *; }

# OkHttp itself (generally not necessary, but included for completeness)
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Kotlin reflection (if using Kotlin)
-keep class kotlin.reflect.jvm.internal.** { *; }
-dontwarn kotlin.reflect.jvm.internal.**