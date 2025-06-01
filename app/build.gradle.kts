import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

fun getGeminiApiKey(project: Project): String {
    val properties = Properties()
    val localPropsFile = project.rootProject.file("local.properties")
    if (localPropsFile.exists() && localPropsFile.canRead()) {
        try {
            properties.load(FileInputStream(localPropsFile))
            val apiKey = properties.getProperty("GEMINI_API_KEY")
            if (apiKey != null && apiKey.isNotBlank()) {
                return "\"$apiKey\""
            }
            println("Atenção: Chave GEMINI_API_KEY não encontrada ou vazia no local.properties.")
        } catch (e: Exception) {
            println("Erro ao carregar local.properties para Gemini: ${e.message}")
        }
    }
    println("Atenção: Ficheiro local.properties não encontrado ou não pode ser lido. GEMINI_API_KEY não será definida.")
    return "\"\""
}

android {
    namespace = "com.example.uniforte"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.uniforte"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GEMINI_API_KEY", getGeminiApiKey(project))
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

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    implementation("io.coil-kt:coil:2.2.2")
    implementation("io.coil-kt:coil-svg:2.2.2")

    // Retrofit e Gson para chamadas de API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Coroutines para chamadas assíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}

