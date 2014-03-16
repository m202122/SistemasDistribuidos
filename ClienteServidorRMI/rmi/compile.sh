mkdir bin
javac -cp lib/jdbm-1.0.jar src/*.java -d bin
cd bin
rmic AgendaImpl
rmiregistry &
cd ..
