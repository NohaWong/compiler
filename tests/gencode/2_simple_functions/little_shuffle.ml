let rec foo a b =
  print_int a;
  print_int b in
let rec bar a b =
  foo b a in
bar 1 2
