# Define download URL and installer location
$java21Url = "https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.exe"
$installer = "$env:TEMP\jdk-21_windows-x64_bin.exe"

$scriptPath = split-path -parent $MyInvocation.MyCommand.Definition

Write-Output "Go to: $scriptPath"
Set-Location $scriptPath

if((Get-Command java | Select-Object -ExpandProperty Version).Major -eq	"21") {
    Write-Host "java installed"
} else {
    Write-Host "jdk-21 not installed yet, installing jdk-21"
    Invoke-WebRequest -Uri $java21Url -OutFile $installer
    Start-Process -FilePath $installer -ArgumentList "/s" -Wait
    Remove-Item $installer

    Write-Host "Set enviroment"
    [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-21")
    [System.Environment]::SetEnvironmentVariable("Path", [System.Environment]::GetEnvironmentVariable('Path', [System.EnvironmentVariableTarget]::Machine) + ";$($env:JAVA_HOME)\bin")
    Set-ExecutionPolicy -ExecutionPolicy Unrestricted -Scope CurrentUser
}

Write-Host "Running main.jar..."
java -jar ./export/hw4_advance_algorithm.jar