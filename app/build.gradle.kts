plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.testeando.botonreconoceraudio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.testeando.botonreconoceraudio"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Añade esta sección al final
tasks.register("stage") {
    dependsOn("build")
}

// Tarea para generar el archivo local.properties dinámicamente
tasks.register("createLocalProperties") {
    doLast {
        file("local.properties").writeText("sdk.dir=C:/Users/juana/AppData/Local/Android/Sdk")
    }
}

// Asegúrate de que se ejecute antes de compilar
tasks.getByName("preBuild").dependsOn("createLocalProperties")
