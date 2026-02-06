plugins {
    id("java")
}

group = "com.example"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
}
