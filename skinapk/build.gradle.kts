plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
// 打包的路径
val outputPathName = "../../SkinDemo/app/src/main/assets/skinapk"
android {
    namespace = "com.example.skinapk"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.skinapk"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // 报错不用管，api过时了不影响打包
    applicationVariants.all {
        outputs.all {
            packageApplication.outputDirectory.set(File("$outputPathName"))

        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")

}