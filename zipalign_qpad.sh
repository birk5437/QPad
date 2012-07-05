cd dist
dir=`pwd`
mv QPad.apk QPad_unaligned.apk
cd /Applications/android-sdk-mac_86/tools/
./zipalign -f -v 4 $dir/QPad_unaligned.apk $dir/QPad.apk
rm $dir/QPad_unaligned.apk
