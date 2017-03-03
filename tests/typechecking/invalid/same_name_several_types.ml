(* Variables de même nom mais de types différents *)
print_int
(let rec f x =
	(let y = 2. in x+y+1)
	in
	(f (let x = true in 12)) + (f (let x = 1. in 22))
)
