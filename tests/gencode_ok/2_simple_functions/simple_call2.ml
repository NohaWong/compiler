let rec f a b c d e =
	let y = a + b + c + d + e in
	let x = y + y in
	x + y
in
print_int (f 1 2 3 4 5)
