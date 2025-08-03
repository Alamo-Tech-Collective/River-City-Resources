# Contribution Workflow:

## Prerequisites
Setup SSH key as mentioned in the [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

## Set up your copy of the repo
The first step is to set up a copy of the Git repository of the project you want to contribute to. ***River-City-Resources*** follows a "fork, feature-branch, and PR" model for contributions.

On GitHub, "Fork" the ***River-City-Resources*** repository, to your own user account using the "Fork" button.

Clone your fork to your local machine and enter the directory:
```
git clone git@github.com:yourusername/River-City-Resources
cd River-City-Resources/
```
Add the "upstream" remote, which allows you to pull down changes from the main project easily:
```
git remote add upstream git@github.com:Alamo-Tech-Collective/River-City-Resources.git
```
You will now be ready to begin building or modifying the project.

Make changes to the repo
Once you have your repository, you can get to work.

Rebase your local branches against upstream main so you are working off the latest changes:
```
git pull
git rebase upstream/main
```
Create a local feature branch off of main to make your changes:
```
git checkout -b my-feature main
```
Make your changes and commits to this local feature branch.

Perform the following commands on your local feature branch once you're done your work, to ensure you have no conflicts with other work done since you started.
```
git pull
git rebase upstream/main
```
Push up your local feature branch to your GitHub fork:
```
git push --set-upstream origin my-feature
```
On GitHub, create a new PR against the upstream main branch following the advice below.

Once your PR is merged, ensure you keep your local branches up-to-date:
```
git pull
git checkout main
git rebase upstream/main
git push -u origin main
```
Delete your local feature branch if you no longer need it:
```
git branch -d my-feature
```
## Pull Request Guidelines
When submitting a new PR, please ensure you do the following things. If you haven't, please read [How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/) as it is a great resource for writing useful commit messages.

Write a good title that quickly describes what has been changed.

Why the changes are being made. Reference specific issues with keywords (fixes, closes, addresses, etc.) if at all possible.