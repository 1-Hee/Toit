import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.text.SimpleDateFormat
import java.util.Date

fun getApiKey(propertyKey: String): String {
    /*
        if gradle AGP version under 8.3.0 , can use these method,

            return gradleLocalProperties(rootDir).getProperty(propertyKey)

        but upper than 8.3.0 you should change codes like this...
     */
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey)
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.gms.oss-licenses-plugin")
    kotlin("kapt")// 사용하는 코틀린 버전에 맞게 해주어야함!
}

android {

    namespace = "com.one.toit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.one.toit"
        minSdk = 24
        targetSdk = 33
        versionCode = 3
        versionName = "1.0.1"
        versionName = "1.1.5"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            manifestPlaceholders["ADMOB_APP_KEY"] = getApiKey("ADMOB_TEST_APP_KEY")
            buildConfigField("String", "ADMOB_SDK_KEY", getApiKey("ADMOB_TEST_BANNER_SDK_KEY"))
        }
        release {
            manifestPlaceholders["ADMOB_APP_KEY"] = getApiKey("ADMOB_APP_KEY")
            buildConfigField("String", "ADMOB_SDK_KEY", getApiKey("ADMOB_BANNER_SDK_KEY"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    signingConfigs {
        create("debugSignedKey") {
            /*
             */
        }

        create("releaseSignedKey") {
            /*
             */
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    applicationVariants.all {
        val variant = this
        val currentDate = Date();
        val formattedDate = SimpleDateFormat("yyyy_MM_dd").format(currentDate)
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                if(output.outputFile != null){
                    if(output.outputFile.name.endsWith(".apk")){
                        val appPrefix = "to_it"
                        val versionName = variant.versionName
                        val buildType = variant.buildType.name
                        val outputName = "${appPrefix}_${buildType}_${formattedDate}_${versionName}.apk"
                        output.outputFileName = outputName
                    }
                }
            }
    }

    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // default config
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Room 테스트 라이브러리
    testImplementation("androidx.room:room-testing:2.4.3") // 최신 버전 확인 필요
    // Coroutine 테스트 라이브러리
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    // Paging 테스트 라이브러리
    testImplementation("androidx.paging:paging-common:3.1.0") // 최신 버전 확인 필요

    // compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // 컴포저블을 위한 라이브 데이터 의존성 추가! (= 컴포저블의 옵저버 패턴을 위하여)
    implementation("androidx.compose.runtime:runtime-livedata:1.3.1")

    // debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.compose.material:material:1.3.1")
    implementation("com.google.android.material:material:1.6.0-alpha01")
    implementation("io.coil-kt:coil-compose:2.2.2")

    // compose navigation
    val comp_nav_version = "2.4.1"
    implementation("androidx.navigation:navigation-compose:$comp_nav_version")

    // glide compose
    implementation("com.github.skydoves:landscapist-glide:1.4.7")

    // tools
    // multi dex
    implementation("androidx.multidex:multidex:2.0.1")

    /** firebase, 나중에 기능구현할 때 풀기 !
     *    // Import the Firebase BoM
     *     implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
     *
     *     // TODO: Add the dependencies for Firebase products you want to use
     *     // When using the BoM, don't specify versions in Firebase dependencies
     *     implementation("com.google.firebase:firebase-analytics")
     */

    // room implements
    val room_version = "2.5.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Room의 Kotlin 확장 (선택 사항)
    kapt("androidx.room:room-compiler:$room_version") // Room 애노테이션 프로세서 (kapt 구성)
    implementation("androidx.room:room-paging:$room_version") // 페이징 의존성
    // for paging
    val paging_version ="3.3.0"
    implementation("androidx.paging:paging-runtime:$paging_version")


    // rxjava, rxkotlin implements
    val rx_java_version = "3.1.8"
    val rx_kotlin_version = "3.0.1"
    implementation("io.reactivex.rxjava3:rxjava:$rx_java_version")
    implementation("io.reactivex.rxjava3:rxkotlin:$rx_kotlin_version") // rx kotlin
    val rx_android_version = "3.0.2";
    implementation("io.reactivex.rxjava3:rxandroid:$rx_android_version")

    // timber implements
    val timer_version = "4.7.1"
    implementation("com.jakewharton.timber:timber:$timer_version")

    // ViewModel implements
    val lifeCycleVersion = "2.5.1"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleVersion")

    // navigation implements
    val navVersion = "2.3.2"
    implementation ("androidx.navigation:navigation-fragment:$navVersion")
    implementation ("androidx.navigation:navigation-ui:$navVersion")

    // gson implements
    implementation("com.google.code.gson:gson:2.9.1")

    // tedpermission implements, for java
    val tedVersion = "3.3.0"
    // tedpermission implements, for Coroutine
    implementation("io.github.ParkSangGwon:tedpermission-coroutine:$tedVersion")

    /**
     * TODO.. ViewPager2 사용시 해제
     *  implementation 'androidx.viewpager2:viewpager2:1.0.0-beta03'
     */
    //flex box
    val flexVersion = "3.0.0"
    implementation("com.google.android.flexbox:flexbox:$flexVersion")

    // https://developers.google.com/android/guides/opensource?hl=ko#kotlin-dsl
    val oss_version = "17.0.1"
    implementation("com.google.android.gms:play-services-oss-licenses:$oss_version")

    // admobs
    val admob_version = "22.6.0"
    implementation("com.google.android.gms:play-services-ads:$admob_version")

    val app_update_version = "2.1.0"
    implementation("com.google.android.play:app-update:$app_update_version")
    implementation("com.google.android.play:app-update-ktx:$app_update_version") // for kotlin

    // for xml
    val swiperefresh_version = "0.24.7-alpha"
    implementation("com.google.accompanist:accompanist-swiperefresh:$swiperefresh_version")


}
