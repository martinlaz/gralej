del dist\README.txt
del dist\plugin.jar
copy LICENSE.txt dist
ren dist gralej
tar cvf gralej_v%1.tar gralej
bzip2 gralej_v%1.tar
move gralej_v%1.tar.bz2 ..
ren gralej dist
