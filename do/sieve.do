# Write out all prime numbers among the first n natural numbers
# using the Sieve of Eratosthenes

let x; show x! x write " " write x;

n 100;

let composites; allprime n vector composites!;
composite true composites item!;
composite? composites item ?; prime? composite? not;

let i j; iinc i 1 + i!; jinc j 1 + j!;
loop i j * n < & composite drop jinc ...;
loop i n < drop & 2 j! loop drop iinc ...;
sieve allprime 2 i! loop;

try prime? & show; loop n < & try 1 + ...;
primes 2 loop drop newline;

main sieve primes;
