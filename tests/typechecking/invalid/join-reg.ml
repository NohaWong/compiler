let rec f _ = 123 in
let rec g _ = 456 in
let rec h _ = 789 in

let x = f 1 2 in
let y = g () in
print_int ((if h () = 0 then x - y else y - x) + x + y)
