cp C:/programs/fiji-win64_peter/Fiji.app/ImageJ-win64.exe C:/programs/fiji-win64_peter/Fiji.app/debug.exe

mvn -Denforcer.skip -Dmaven.test.skip=true -Dimagej.app.directory=C:/programs/fiji-win64_peter/Fiji.app install

