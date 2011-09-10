cd dist
mv QPad.apk QPad_unaligned.apk
cd /Applications/android-sdk-mac_86/tools/
./zipalign -f -v 4 /Users/burke/NetBeansProjects/QPad/dist/QPad_unaligned.apk /Users/burke/NetBeansProjects/QPad/dist/QPad.apk
rm /Users/burke/NetBeansProjects/QPad/dist/QPad_unaligned.apk
