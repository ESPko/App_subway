plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
  implementation(libs.protolite.well.known.types)
  testImplementation(libs.junit)
  implementation(libs.cronet.embedded)
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
  // 레트로핏 scalars 컨버터
  // https://mvnrepository.com/artifact/com.squareup.retrofit2/converter-scalars
  implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

  // 네이버 지도 SDK
  implementation("com.naver.maps:map-sdk:3.20.0")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

  
}