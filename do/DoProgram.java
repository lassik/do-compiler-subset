public class DoProgram {
public static void main(String[] args) throws Exception { _main0(); }
static Object _x0;
static Object _composites0;
static Object _i0;
static Object _j0;
static void _show0() throws Exception {
for(;;) {
_x0 = DoPrim.pop();
DoPrim.push(_x0);
DoPrim.p_write();
DoPrim.push(" ");
DoPrim.p_write();
DoPrim.push(_x0);
break;
} }
static void _n0() throws Exception {
for(;;) {
DoPrim.push(new Integer(100));
break;
} }
static void _allprime0() throws Exception {
for(;;) {
_n0();
DoPrim.p_vector();
_composites0 = DoPrim.pop();
break;
} }
static void _composite0() throws Exception {
for(;;) {
DoPrim.p_true();
DoPrim.push(_composites0);
DoPrim.p_s_itemstore();
break;
} }
static void _composite_0() throws Exception {
for(;;) {
DoPrim.push(_composites0);
DoPrim.p_item();
DoPrim.p_s_ask();
break;
} }
static void _prime_0() throws Exception {
for(;;) {
_composite_0();
DoPrim.p_not();
break;
} }
static void _iinc0() throws Exception {
for(;;) {
DoPrim.push(_i0);
DoPrim.push(new Integer(1));
DoPrim.p_s_add();
_i0 = DoPrim.pop();
break;
} }
static void _jinc0() throws Exception {
for(;;) {
DoPrim.push(_j0);
DoPrim.push(new Integer(1));
DoPrim.p_s_add();
_j0 = DoPrim.pop();
break;
} }
static void _loop0() throws Exception {
for(;;) {
DoPrim.push(_i0);
DoPrim.push(_j0);
DoPrim.p_s_mul();
_n0();
DoPrim.p_s_lt();
if(!DoPrim.flag) break;
_composite0();
DoPrim.p_drop();
_jinc0();
continue;
} }
static void _loop1() throws Exception {
for(;;) {
DoPrim.push(_i0);
_n0();
DoPrim.p_s_lt();
DoPrim.p_drop();
if(!DoPrim.flag) break;
DoPrim.push(new Integer(2));
_j0 = DoPrim.pop();
_loop0();
DoPrim.p_drop();
_iinc0();
continue;
} }
static void _sieve0() throws Exception {
for(;;) {
_allprime0();
DoPrim.push(new Integer(2));
_i0 = DoPrim.pop();
_loop1();
break;
} }
static void _try0() throws Exception {
for(;;) {
_prime_0();
if(!DoPrim.flag) break;
_show0();
break;
} }
static void _loop2() throws Exception {
for(;;) {
_n0();
DoPrim.p_s_lt();
if(!DoPrim.flag) break;
_try0();
DoPrim.push(new Integer(1));
DoPrim.p_s_add();
continue;
} }
static void _primes0() throws Exception {
for(;;) {
DoPrim.push(new Integer(2));
_loop2();
DoPrim.p_drop();
DoPrim.p_newline();
break;
} }
static void _main0() throws Exception {
for(;;) {
_sieve0();
_primes0();
break;
} }
}
