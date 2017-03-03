let rec f x =
	let rec g y =
		let rec h z =
			print_int (z + 1)
		in
		h (y + 1) ; print_int (y + 2)
	in
	g (x - 1) ; print_int (x - 2)
in
f (-42)
