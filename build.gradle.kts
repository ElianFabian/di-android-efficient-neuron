buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath("com.google.dagger:hilt-android-gradle-plugin:2.42")

		// Firebase
		classpath("com.google.gms:google-services:4.3.15")

		classpath("com.android.tools.build:gradle:7.2.2")
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
	}
}

plugins {
	id("com.google.dagger.hilt.android") version "2.41" apply false
	id("org.jetbrains.kotlin.android") version "1.6.21" apply false
}


tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}