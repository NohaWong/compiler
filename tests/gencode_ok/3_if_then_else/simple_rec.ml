let rec f x = 
	if x = 0 then 42 else f (x - 1)
in
print_int (f 23)
