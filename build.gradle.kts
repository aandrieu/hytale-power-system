plugins {
    id("java")
}

group = "com.pix"
version = "1.0-SNAPSHOT"

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
}
