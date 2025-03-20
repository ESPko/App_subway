plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "com.example.app"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.example.app"
    minSdk = 29
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }
  viewBinding{
    enable = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)

  // 글라이드
  // https://mvnrepository.com/artifact/com.github.bumptech.glide/glide
  implementation("com.github.bumptech.glide:glide:4.16.0")
  // Gson
  // https://mvnrepository.com/artifact/com.google.code.gson/gson
  implementation("com.google.code.gson:gson:2.12.1")
  // 레트로핏
  // https://mvnrepository.com/artifact/com.squareup.retrofit2/retrofit
  implementation("com.squareup.retrofit2:retrofit:2.11.0")
  // 레트로핏 Gson 컨버터
  // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-gson
  implementation("com.squareup.retrofit2:converter-gson:2.11.0")
  // xml 파일 컨버터
  // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-simplexml
  implementation("com.squareup.retrofit2:converter-simplexml:2.11.0")
  // 구글 플레이 지도 서비스
  implementation("com.google.android.gms:play-services-location:21.0.1")

  // 네이버 지도 SDK
  implementation("com.naver.maps:map-sdk:3.20.0")

 // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-scalars
  implementation("com.squareup.retrofit2:converter-scalars:2.11.0")


}