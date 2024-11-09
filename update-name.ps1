# Define the old email, new name, and new email
$oldEmail = "juancawool@outlook.com"
$newName = "Code with Juanda"
$newEmail = "codwithjuanda@gmail.com"

# Run the git filter-branch command
git filter-branch --env-filter (
    "if [ `"$GIT_COMMITTER_EMAIL`" = `"$oldEmail`" ]; then" +
    " export GIT_COMMITTER_NAME=`"$newName`";" +
    " export GIT_COMMITTER_EMAIL=`"$newEmail`";" +
    " fi;" +
    "if [ `"$GIT_AUTHOR_EMAIL`" = `"$oldEmail`" ]; then" +
    " export GIT_AUTHOR_NAME=`"$newName`";" +
    " export GIT_AUTHOR_EMAIL=`"$newEmail`";" +
    " fi"
) --tag-name-filter cat -- --branches --tags