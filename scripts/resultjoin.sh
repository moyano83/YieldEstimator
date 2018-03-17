#!/bin/bash
RESULT_FILE='inferences_result.csv'
if [ -f $RESULT_FILE ]; then
    rm $RESULT_FILE
fi
for dir in $(ls | sort)
do
    for file in $(ls ${dir}/*.txt |sort)
    do
        cat $file >> $RESULT_FILE
    done
done