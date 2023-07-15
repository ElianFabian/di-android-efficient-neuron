plugins {
	id("dagger.hilt.android.plugin")

	// Firebase
	id("com.google.gms.google-services")

	id("com.google.firebase.crashlytics")

	id("com.android.application")
	id("kotlin-parcelize")
	kotlin("android")
	kotlin("kapt")
}

android {
	namespace = "com.elian.computeit"
	compileSdk = 33

	defaultConfig {
		applicationId = "com.elian.computeit"
		minSdk = 23
		targetSdk = 33
		versionCode = 1
		versionName = "0.5"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
		//freeCompilerArgs = listOf("-Xcontext-receivers")
	}

	buildFeatures {
		viewBinding = true
	}
}

apply(plugin = "kotlin-kapt")
apply(plugin = "com.android.application")
apply(plugin = "dagger.hilt.android.plugin")

dependencies {
	// MPAndroidChart: https://github.com/PhilJay/MPAndroidChart
	implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

	// SSP: https://github.com/intuit/ssp
	implementation("com.intuit.ssp:ssp-android:1.1.0")

	// SDP: https://github.com/intuit/sdp
	implementation("com.intuit.sdp:sdp-android:1.1.0")

	// DataStore
	implementation("androidx.datastore:datastore-preferences:1.0.0")

	// Dagger Hilt
	implementation("com.google.dagger:hilt-android:2.44")
	kapt("com.google.dagger:hilt-compiler:2.47")
	kapt("androidx.hilt:hilt-compiler:1.0.0")

	//Firebase Firestore
	implementation("com.google.firebase:firebase-firestore-ktx")

	// Firebase Storage
	implementation("com.google.firebase:firebase-storage-ktx:20.1.0") //DON'T UPDATE (20.1.0) error when upgrading (java.lang.NoClassDefFoundError: Failed resolution of: Lcom/google/firebase/appcheck/interop/InternalAppCheckTokenProvider;)

	// Import the Firebase BoM
	implementation(platform("com.google.firebase:firebase-bom:29.3.1"))

	// Add the dependencies for the Crashlytics and Analytics libraries
	implementation("com.google.firebase:firebase-crashlytics-ktx")
	implementation("com.google.firebase:firebase-analytics-ktx")

	// Async / Await
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

	// Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

	// Lifecycle
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
	implementation("androidx.lifecycle:lifecycle-common-java8:2.5.1")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")


	implementation("com.google.android.material:material:1.9.0")
	implementation("androidx.activity:activity-ktx:1.5.0") // DON'T UPDATE (1.5.0)  error when upgrading (conflict)
	implementation("androidx.core:core-ktx:1.8.0") // DON'T UPDATE (1.8.0) error when upgrading (conflict)
	implementation("androidx.appcompat:appcompat:1.6.1") // DON'T UPDATE (1.5.1)
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("androidx.navigation:navigation-fragment-ktx:2.5.3") // error when upgrading (conflict)
	implementation("androidx.navigation:navigation-ui-ktx:2.5.3") // error when upgrading (conflict)
	implementation("androidx.preference:preference-ktx:1.2.0")
	testImplementation("junit:junit:")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}