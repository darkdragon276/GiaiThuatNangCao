# Check if Git is installed
$gitVersion = Get-Command git -ErrorAction SilentlyContinue
if ($gitVersion -eq $null) {
    Write-Host "Git is not installed. Installing Git..."

    # Install Git using Winget
    winget install Git.Git -e

    # Check again after installation
    $gitVersion = Get-Command git -ErrorAction SilentlyContinue
    if ($gitVersion -eq $null) {
        Write-Host "Unable to install Git. Please install Git manually and run the script again."
        exit
    }

    Write-Host "Git has been successfully installed."
}

# Continue with the rest of the script
# Prompt for input
$name = Read-Host "Enter homework name"
$version = Read-Host "Number of homework"

# Check if a Git repository exists
if (-Not (Test-Path .git)) {
    Write-Host "Git repository not found. Please create a repository first."
    exit
}

# Update the master branch
git fetch --all
git checkout origin/master

# Create a new branch from master
$branchName = "homework_${version}/${name}"
git checkout -b $branchName