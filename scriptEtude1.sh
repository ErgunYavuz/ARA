#!/bin/bash

javac -Xldeprecation -d bin -classpath lib/djep-1.0.0.jar:lib/jep-2.3.0.jar:lib/peersim-1.0.5.jar:lib/peersim-doclet.jar src/ara/util/*.java src/ara/projet/mutex/*.java

FILE_PATH=cfg
OUTPUT_FILE=test.csv

N=10
SEED=20
ENDTIME=10000
MINDELAY=0
MAXDELAY=5
TIMECS_ALPHA=50

for i in {10..100..10}
  do
    echo "network.size $N" >> $FILE_PATH
    echo "random.seed $SEED" >> $FILE_PATH
    echo "" >> $FILE_PATH
    echo "simulation.endtime $ENDTIME" >> $FILE_PATH
    echo "" >> $FILE_PATH
    echo "protocol.transport UniformRandomTransport" >> $FILE_PATH
    echo "" >> $FILE_PATH
    echo "protocol.transport.mindelay $MINDELAY" >> $FILE_PATH
    echo "protocol.transport.maxdelay $MAXDELAY" >> $FILE_PATH
    echo "" >> $FILE_PATH
    echo "protocol.applicative NaimiTrehelAlgo" >> $FILE_PATH
    echo "" >> $FILE_PATH
    echo "protocol.applicative.transport transport" >> $FILE_PATH
    echo "protocol.applicative.timeCS $TIMECS_ALPHA" >> $FILE_PATH
    echo "protocol.applicative.timeBetweenCS $i" >> $FILE_PATH
    echo "" >> $FILE_PATH

    echo "control.endcontroler EndControl" >> $FILE_PATH
    echo "" >> $FILE_PATH
    echo "control.endcontroler.applicative applicative" >> $FILE_PATH
    echo "control.endcontroler.at -1" >> $FILE_PATH
    echo "control.endcontroler.FINAL" >> $FILE_PATH
    java -cp bin/:lib/*: peersim.Simulator $FILE_PATH
    rm $FILE_PATH
  done

#nb msg a discriminer 
