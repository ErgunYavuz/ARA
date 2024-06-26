#!/bin/bash

javac -Xldeprecation -d bin -classpath lib/djep-1.0.0.jar:lib/jep-2.3.0.jar:lib/peersim-1.0.5.jar:lib/peersim-doclet.jar src/ara/util/*.java src/ara/projet/mutex/*.java

FILE_PATH=cfg
OUTPUT_FILE=test.csv

N=10
SEED=20
ENDTIME=10000
MINDELAY1=0
MAXDELAY1=5
MINDELAY2=45
MAXDELAY2=55
MINDELAY3=100
MAXDELAY3=100
TIMECS_ALPHA=50

for cas in {1..3}
do  
    if [[ $cas -eq 1 ]]; then
        echo "Beta;M1_cas1;M2_cas1;M3_cas1;U;T;N" >> $OUTPUT_FILE
    elif [[ $cas -eq 2 ]]; then
        echo "Beta;M1_cas2;M2_cas2;M3_cas2;U;T;N" >> $OUTPUT_FILE
    else
        echo "Beta;M1_cas3;M2_cas3;M3_cas3;U;T;N" >> $OUTPUT_FILE
    fi
    for i in {10..100..10}
    do
        echo "network.size $N" >> $FILE_PATH
        echo "random.seed $SEED" >> $FILE_PATH
        echo "" >> $FILE_PATH
        echo "simulation.endtime $ENDTIME" >> $FILE_PATH
        echo "" >> $FILE_PATH
        echo "protocol.transport UniformRandomTransport" >> $FILE_PATH
        echo "" >> $FILE_PATH
        if [[ $cas -eq 1 ]]; then
            echo "protocol.transport.mindelay $MINDELAY1" >> $FILE_PATH
            echo "protocol.transport.maxdelay $MAXDELAY1" >> $FILE_PATH
        elif [[ $cas -eq 2 ]]; then
            echo "protocol.transport.mindelay $MINDELAY2" >> $FILE_PATH
            echo "protocol.transport.maxdelay $MAXDELAY2" >> $FILE_PATH
        else
            echo "protocol.transport.mindelay $MINDELAY3" >> $FILE_PATH
            echo "protocol.transport.maxdelay $MAXDELAY3" >> $FILE_PATH
        fi
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
done

#nb msg a discriminer 
