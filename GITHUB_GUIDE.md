# 🚀 GitHub Setup & Push Guide - FinS Project

> **Complete step-by-step guide from creating a GitHub repository to pushing your code professionally**

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Create GitHub Repository](#create-github-repository)
3. [Initialize Local Git Repository](#initialize-local-git-repository)
4. [Prepare Project for Push](#prepare-project-for-push)
5. [First Push to GitHub](#first-push-to-github)
6. [Best Practices](#best-practices)
7. [Troubleshooting](#troubleshooting)

---

## ✅ Prerequisites

### 1. Install Git
```bash
# Check if Git is installed
git --version

# If not installed:
# Windows: Download from https://git-scm.com/
# Mac: brew install git
# Linux: sudo apt install git
```

### 2. Configure Git (First Time Only)
```bash
# Set your name and email (will appear in commits)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Verify configuration
git config --global --list
```

### 3. Create GitHub Account
- Go to https://github.com/
- Sign up if you don't have an account
- Verify your email address

### 4. Setup GitHub Authentication

#### Option A: Personal Access Token (Recommended)
1. Go to https://github.com/settings/tokens
2. Click **"Generate new token (classic)"**
3. Name: `FinS-Project`
4. Expiration: `No expiration` (or your preference)
5. Scopes: Check ✅ **repo** (full control of private repositories)
6. Click **"Generate token"**
7. **COPY THE TOKEN** - you won't see it again!
8. Save it securely (e.g., password manager)

#### Option B: SSH Key (Advanced)
```bash
# Generate SSH key
ssh-keygen -t ed25519 -C "your.email@example.com"

# Copy public key
cat ~/.ssh/id_ed25519.pub

# Add to GitHub: https://github.com/settings/ssh/new
```

---

## 🆕 Create GitHub Repository

### Step 1: Go to GitHub
1. Navigate to https://github.com/new
2. Or click **"+"** → **"New repository"** in top-right corner

### Step 2: Configure Repository

```
Repository name: FinS
                 (or your preferred name)

Description: 📈 Full-stack microservices platform for stock 
             market analysis with AI-powered predictions

Visibility: ○ Public
            ● Private  (recommended for development)

Initialize:
  ☐ Add a README file      (we already have one)
  ☐ Add .gitignore         (we already have one)
  ☐ Choose a license       (optional: MIT License)
```

### Step 3: Create Repository
- Click **"Create repository"** button
- **DO NOT** add README or .gitignore (we have them locally)
- You'll see the empty repository page with setup instructions

---

## 🔧 Initialize Local Git Repository

### Navigate to Project Root
```bash
cd d:/HOC_DAI/DATN2025/FinS
```

### Initialize Git
```bash
# Initialize git repository
git init

# Verify .git folder created
ls -la .git
```

---

## 🎯 Prepare Project for Push

### Step 1: Check .gitignore
```bash
# Verify .gitignore exists
cat .gitignore

# Make sure these patterns are included:
# - .env
# - node_modules/
# - target/
# - __pycache__/
# - .idea/
```

### Step 2: Remove Sensitive Files (Critical!)
```bash
# Find all .env files (make sure they're ignored)
find . -name ".env" -type f

# Check if any are tracked (should return nothing)
git ls-files | grep ".env$"

# If .env files are listed, remove from tracking:
git rm --cached **/.env
```

### Step 3: Stage All Files
```bash
# Check what will be committed
git status

# Add all files
git add .

# Verify staged files (check for sensitive data)
git status

# Review staged files list
git diff --cached --name-only

# If you see .env or credentials, unstage them:
git reset HEAD path/to/sensitive/file
```

### Step 4: First Commit
```bash
# Create initial commit
git commit -m "🎉 Initial commit: FinS Stock Trading Platform

- ✅ Complete microservices architecture (7 services)
- ✅ AI-powered predictions with Prophet
- ✅ Next.js frontend with TypeScript
- ✅ Docker Compose orchestration
- ✅ MongoDB + Kafka + Consul infrastructure
- ✅ Comprehensive documentation
"

# Verify commit
git log --oneline
```

---

## 📤 First Push to GitHub

### Step 1: Add Remote Repository
```bash
# Replace YOUR_USERNAME with your actual GitHub username
git remote add origin https://github.com/YOUR_USERNAME/FinS.git

# Verify remote
git remote -v
```

### Step 2: Rename Default Branch (if needed)
```bash
# GitHub uses 'main', older Git uses 'master'
git branch -M main
```

### Step 3: Push to GitHub
```bash
# Push to GitHub (first time)
git push -u origin main

# Enter credentials when prompted:
# Username: your_github_username
# Password: YOUR_PERSONAL_ACCESS_TOKEN (not your GitHub password!)
```

### Step 4: Verify on GitHub
1. Go to https://github.com/YOUR_USERNAME/FinS
2. You should see:
   - ✅ README.md displayed
   - ✅ All project folders
   - ✅ Commit message
   - ✅ File count (should be 100+)

---

## 🎨 Best Practices

### Commit Message Convention
```bash
# Format: <emoji> <type>: <description>

# Examples:
git commit -m "✨ feat: Add portfolio management feature"
git commit -m "🐛 fix: Resolve Kafka connection timeout"
git commit -m "📝 docs: Update API documentation"
git commit -m "♻️ refactor: Optimize Prophet prediction logic"
git commit -m "🔧 chore: Update dependencies"
git commit -m "🚀 deploy: Configure production environment"
```

### Emoji Guide
| Emoji | Type | Description |
|-------|------|-------------|
| 🎉 | init | Initial commit |
| ✨ | feat | New feature |
| 🐛 | fix | Bug fix |
| 📝 | docs | Documentation |
| ♻️ | refactor | Code refactoring |
| 🔧 | chore | Maintenance |
| 🚀 | deploy | Deployment |
| ✅ | test | Tests |
| 🔒 | security | Security fixes |

### Branch Strategy
```bash
# Create feature branch
git checkout -b feature/portfolio-management

# Work on feature...
git add .
git commit -m "✨ feat: Add portfolio tracking"

# Push feature branch
git push -u origin feature/portfolio-management

# Merge via Pull Request on GitHub
```

---

## 🔐 Security Checklist

### Before Every Push
```bash
# 1. Check for secrets
git diff --cached | grep -i "password\|secret\|key\|token"

# 2. Verify .env files are ignored
git status | grep ".env"

# 3. Review staged files
git diff --cached --name-only

# 4. Use git-secrets (optional tool)
# Install: brew install git-secrets
# Scan: git secrets --scan
```

### After First Push
- [ ] Verify no `.env` files on GitHub
- [ ] Check no credentials in code
- [ ] Review commit history for sensitive data
- [ ] Enable branch protection rules (Settings → Branches)
- [ ] Add collaborators (if team project)

---

## 🛠️ Troubleshooting

### Error: "Support for password authentication was removed"
**Solution**: Use Personal Access Token instead of password
```bash
# When prompted for password, use your Personal Access Token
Username: your_username
Password: ghp_xxxxxxxxxxxxxxxxxxxx  (your token)
```

### Error: "Remote origin already exists"
```bash
# Remove existing remote
git remote remove origin

# Add correct remote
git remote add origin https://github.com/YOUR_USERNAME/FinS.git
```

### Error: "Failed to push some refs"
```bash
# Pull remote changes first
git pull origin main --rebase

# Then push
git push -u origin main
```

### Error: "Large files detected"
```bash
# Find large files
find . -type f -size +50M

# Remove from git history
git filter-branch --force --index-filter \
  'git rm --cached --ignore-unmatch path/to/large/file' \
  --prune-empty --tag-name-filter cat -- --all

# Use Git LFS for large files
git lfs install
git lfs track "*.pdf"
git add .gitattributes
```

### Accidentally Committed Secrets
```bash
# 🚨 CRITICAL: Remove from history
git filter-branch --force --index-filter \
  'git rm --cached --ignore-unmatch .env' \
  --prune-empty --tag-name-filter cat -- --all

# Force push (⚠️ dangerous if collaborators exist)
git push origin --force --all

# Better: Use BFG Repo-Cleaner
# https://rtyley.github.io/bfg-repo-cleaner/
```

---

## 📚 Additional Resources

### Official Documentation
- **Git**: https://git-scm.com/doc
- **GitHub Docs**: https://docs.github.com/
- **Git Cheat Sheet**: https://education.github.com/git-cheat-sheet-education.pdf

### Useful Tools
- **GitHub Desktop**: https://desktop.github.com/ (GUI alternative)
- **GitKraken**: https://www.gitkraken.com/ (Advanced GUI)
- **git-secrets**: Prevent committing secrets
- **pre-commit**: Git hooks for code quality

### Learning Resources
- **GitHub Skills**: https://skills.github.com/
- **Git Branching Game**: https://learngitbranching.js.org/
- **Conventional Commits**: https://www.conventionalcommits.org/

---

## 🎯 Next Steps After Push

### 1. Add Repository Description
- Go to your repo on GitHub
- Click **"⚙️ Settings"**
- Add **Topics**: `stock-trading`, `microservices`, `ai-ml`, `prophet`, `spring-boot`, `nextjs`

### 2. Create Issues & Projects
- Use **Issues** for tracking bugs/features
- Use **Projects** for kanban boards
- Add labels (bug, enhancement, documentation)

### 3. Setup CI/CD (Optional)
- GitHub Actions for automated testing
- Deploy to cloud (AWS, Azure, GCP)

### 4. Invite Collaborators
- Settings → Collaborators
- Add team members

### 5. Enable GitHub Pages (Optional)
- For project documentation
- Settings → Pages → Deploy from `docs/` folder

---

## ✅ Final Checklist

Before sharing repository:
- [ ] README.md is complete and professional
- [ ] .gitignore excludes sensitive files
- [ ] .env.example templates are provided
- [ ] No secrets or credentials committed
- [ ] All services documented
- [ ] Setup instructions tested on clean machine
- [ ] License file added (if open source)
- [ ] Code of Conduct added (if open source)
- [ ] Contributing guide added (if accepting PRs)

---

## 🎉 Success!

Your FinS project is now professionally hosted on GitHub! 

**Repository URL**: `https://github.com/YOUR_USERNAME/FinS`

Share with:
```markdown
Check out my project: https://github.com/YOUR_USERNAME/FinS
```

---

**Questions?** Open an issue or contact the maintainers!

**Happy Coding! 🚀**
