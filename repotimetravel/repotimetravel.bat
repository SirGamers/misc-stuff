@ECHO OFF
set /p "var2=revert to which commit? (commit id): "
git reset --hard %var2%
echo "push back to github? (y/n)"
choice /C yn
if %errorlevel%==1 git push --force
