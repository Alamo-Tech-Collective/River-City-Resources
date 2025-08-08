# Troubleshooting 

## Can't Push My Feature Branch
Try the below command to ensure your SSH key is setup correctly:

```sh
ssh -T git@github.com
```

You should receive the following output:
`Hi USERNAME! You've successfully authenticated, but GitHub does not provide shell access.`

If not, then you will need to create an ssh key in GitHub: [GitHub SSH key](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/generating-a-new-ssh-key-and-adding-it-to-the-ssh-agent)

If you still can't push the feature branch try the below command to ensure you are have forked the repository.

```sh
git remote -v
```

If you have the following for your push url, then you have **not** forked the repository: `https://github.com/Alamo-Tech-Collective/River-City-Resources.git`

How to fork and sync the repository is mentioned in the [CONTRIBUTING.md](CONTRIBUTING.md) file.