n 10;

let input;
let minf; min minf fieldint;
let maxf; max maxf fieldint;
let output;
let gocmd backcmd;

initialize
 "Input"  newform input!
 "Min"    newintfield input addctl minf!
 "Max"    newintfield input addctl maxf!
 "Output" newform output!
 "Go"     newokcmd   input addcmd output addcmd gocmd!
 "Back"   newbackcmd input addcmd output addcmd backcmd!;

count max min - 1 +;
random count randint< min +;
addran random intstr output addctl;
loop n < & addran 1 + ...;
repopulate output clearform 0 loop drop;
go gocmd is & output curform! repopulate;
iback backcmd is & input curform is drop & exit;
oback backcmd is & output curform is drop & input curform!;
command go | iback | oback;
main initialize input curform!;
