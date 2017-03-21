 # Add project specific ProGuard rules here.
# By default, the flags in this document are appended to flags specified
# in D:\ASpath\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and agendaOrder by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#忽略警告
#ignorewarnings

#有Gson包，需要添加下面这条，防止类型转换错误
# -keepattributes Signature

#让Gson包不混淆
#-dontwarn com.google.gson.**
#-keep class com.google.gson.**{ *;}

#v4  ,  v7  包不混淆
#-dontwarn android.support.v4.**
#-keep class android.support.v4.**{ *;}

#-dontwarn android.support.v7.**
#-keep class android.support.v7.**{ *;}

#-dontwarn android.support.annotation.**
#-keep class android.support.annotation.**{ *;}

#retrofit 官网声明，如果使用了retrofit，并且有proguard文件，需要以下配置：现先全部注释
# Platform calls Class.forName on types which do not exist on Android to determine platform.
#-dontnote retrofit2.Platform

# Platform used when running on RoboVM on iOS. Will not be used at runtime.
#-dontnote retrofit2.Platform$IOS$MainThreadExecutor

# Platform used when running on Java 8 VMs. Will not be used at runtime.
#-dontwarn retrofit2.Platform$Java8

# Retain generic type information for use by reflection by converters and adapters.
#-keepattributes Signature

# Retain declared checked exceptions for use by a Proxy instance.
#-keepattributes Exceptions
