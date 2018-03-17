#!/bin/bash
if [ -f inferences_result.txt ]; then
    rm inferences_result.txt
fi
for dir in $(ls | sort)
do
    for file in $(ls ${dir}/*.txt |sort)
    do
        cat $file >> inferences_result.txt
    done
done