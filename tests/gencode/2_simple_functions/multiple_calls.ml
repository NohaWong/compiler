let rec f x = x + 1 in
let rec g y = f (y - 2) in
let rec h z = f (g (z + z - 1)) in
print_int (h 42)
