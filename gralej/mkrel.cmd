setlocal
set DIR=dist\gralej

rd /q /s %DIR%

md %DIR%
copy LICENSE.txt %DIR%
copy dist\gralej.jar %DIR%

md %DIR%\lib
copy lib\*jar %DIR%\lib

pushd %DIR%\..
tar cvf gralej_v%1.tar gralej
bzip2 gralej_v%1.tar
popd

move %DIR%\..\gralej_v%1.tar.bz2 ..
