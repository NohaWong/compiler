(* Test du constant folding / inline *)
let rec g n =
	let rec h i = if i>0 then h (i-1) else i
	in
	h n
in
print_int (g 2)
