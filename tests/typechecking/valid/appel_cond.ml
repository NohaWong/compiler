let rec f x = x in
let rec g a = a in
print_int ((if true then g else f) 1)
