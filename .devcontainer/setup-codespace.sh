#!/bin/bash

echo "🚀 Setting up Java Banking Application in Codespace..."

# Install OpenJDK and OpenJFX
sudo apt update
sudo apt install -y openjdk-21-jdk openjfx

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

# Compile all Java files with JavaFX modules
echo "📦 Compiling Java files..."
javac --module-path=/usr/share/openjfx/lib --add-modules=javafx.controls,javafx.fxml *.java

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "🎯 To run the application:"
    echo "   java --module-path=/usr/share/openjfx/lib --add-modules=javafx.controls,javafx.fxml Main"
    echo ""
    echo "🔧 Or run the integration test:"
    echo "   java --module-path=/usr/share/openjfx/lib --add-modules=javafx.controls,javafx.fxml BankingApplication"
    echo ""
    echo "🧪 Or run the text-based test:"
    echo "   java BankingAppIntegrationTest"
else
    echo "❌ Compilation failed. Please check for errors."
fi
