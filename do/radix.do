# Convert a nonnegative integer from one radix to others

let sub seq; try & 0 = drop; prefix? seq! sub! sub seq find try sub;
let x; show x! x write " " write x;

# Radixes

digits "0123456789abcdef";
0b 2 "0b"; 0o 8 "0o"; 0x 16 "0x"; decimal 10 "";

# Input a nonnegative integer in any radix

let str idx val radix;
digit digits find & radix <; check | "Bad digit" die;
tally val radix * + val!; remain str count <;
loop remain & str item digit check tally 1 + ...;
some remain | "No digits" die;
parseat idx! radix! idx some loop drop val;
try str prefix? & count parseat true ?;
parse 0b try | 0o try | 0x try | decimal try;
input "Input: " write readline str! 0 val! parse;

# Output a nonnegative integer in all radixes

let radix x odigs;
loop radix /mod digits item odigs push drop 0 <> & ...;
emitrev 0 > & 1 - odigs item write ...;
emit write radix! 0 vector odigs! loop drop odigs count emitrev newline;
output x! x 0b emit x 0o emit x 0x emit x decimal emit;

# Main

main input output;
