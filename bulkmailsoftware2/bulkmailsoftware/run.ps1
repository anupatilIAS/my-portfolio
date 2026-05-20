if (-not (Test-Path "apache-maven-3.8.8")) {
    Write-Host "Downloading Maven (required to run the app)..."
    Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.zip" -OutFile "maven.zip"
    Write-Host "Extracting Maven..."
    Expand-Archive -Path "maven.zip" -DestinationPath "." -Force
    Remove-Item "maven.zip"
}
Write-Host "Starting Application..."
.\apache-maven-3.8.8\bin\mvn.cmd spring-boot:run
pause
