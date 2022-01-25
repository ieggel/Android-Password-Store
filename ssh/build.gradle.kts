plugins {
  id("com.github.android-password-store.android-library")
  id("com.github.android-password-store.kotlin-android")
  id("com.github.android-password-store.kotlin-library")
}

android { sourceSets { getByName("test") { resources.srcDir("src/main/res/raw") } } }

dependencies {
  implementation(libs.thirdparty.sshauth)
  implementation(libs.thirdparty.sshj)
}