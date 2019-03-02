// This file is part of the materials accompanying the book 
// "The Elements of Computing Systems" by Nisan and Schocken, 
// MIT Press. Book site: www.idc.ac.il/tecs
// File name: GateQuiz/Quiz1.tst

load Quiz1.hdl,
output-file Quiz1.out,
compare-to Quiz1.cmp,
output-list x%B3.1.3 y%B3.1.3 z%B3.1.3 out%B3.1.3;

set x 0,
set y 0,
set z 0,
eval,
output;

set x 0,
set y 0,
set z 1,
eval,
output;

set x 0,
set y 1,
set z 0,
eval,
output;

set x 0,
set y 1,
set z 1,
eval,
output;

set x 1,
set y 0,
set z 0,
eval,
output;

set x 1,
set y 0,
set z 1,
eval,
output;

set x 1,
set y 1,
set z 0,
eval,
output;

set x 1,
set y 1,
set z 1,
eval,
output;