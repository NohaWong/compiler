let rec main x =
    if x=7 then
        let n_1 = print_int 0 in
        let n_2 = print_int 0 in
        let n_3 = print_int 0 in
        let n_4 = print_int 0 in
        let n_5 = print_int 0 in
        let n_6 = print_int 0 in
        let n_7 = print_int 0 in
        let n_8 = print_newline () in
        main (x-1)
    else
        if x=6 then
			let n_1 = print_int 0 in
	        let n_2 = print_int 0 in
	        let n_3 = print_int 1 in
	        let n_4 = print_int 1 in
	        let n_5 = print_int 0 in
	        let n_6 = print_int 0 in
	        let n_7 = print_int 0 in
	        let n_8 = print_newline () in
        	main (x-1)
        else
            if x=5 then
                let n_1 = print_int 0 in
		        let n_2 = print_int 0 in
		        let n_3 = print_int 0 in
		        let n_4 = print_int 1 in
		        let n_5 = print_int 0 in
		        let n_6 = print_int 0 in
		        let n_7 = print_int 0 in
		        let n_8 = print_newline () in
        		main (x-1)
            else
                if x=4 then
                    let n_1 = print_int 0 in
			        let n_2 = print_int 0 in
			        let n_3 = print_int 0 in
			        let n_4 = print_int 1 in
			        let n_5 = print_int 0 in
			        let n_6 = print_int 0 in
			        let n_7 = print_int 0 in
			        let n_8 = print_newline () in
        			main (x-1)
                else
                    if x=3 then
						let n_1 = print_int 0 in
						let n_2 = print_int 0 in
						let n_3 = print_int 0 in
						let n_4 = print_int 1 in
						let n_5 = print_int 0 in
						let n_6 = print_int 0 in
						let n_7 = print_int 0 in
						let n_8 = print_newline () in
        				main (x-1)
                    else
                        if x=2 then
                            let n_1 = print_int 0 in
					        let n_2 = print_int 0 in
					        let n_3 = print_int 0 in
					        let n_4 = print_int 1 in
					        let n_5 = print_int 0 in
					        let n_6 = print_int 0 in
					        let n_7 = print_int 0 in
					        let n_8 = print_newline () in
        					main (x-1)
                        else
                            if x=1 then
                                let n_1 = print_int 0 in
						        let n_2 = print_int 0 in
						        let n_3 = print_int 1 in
						        let n_4 = print_int 1 in
						        let n_5 = print_int 1 in
						        let n_6 = print_int 0 in
						        let n_7 = print_int 0 in
						        let n_8 = print_newline () in
        						main (x-1)
                            else
                                if x=0 then
                                    let n_1 = print_int 0 in
							        let n_2 = print_int 0 in
							        let n_3 = print_int 0 in
							        let n_4 = print_int 0 in
							        let n_5 = print_int 0 in
							        let n_6 = print_int 0 in
							        let n_7 = print_int 0 in
							        let n_8 = print_newline () in
							        print_newline ()
                                else
                                    print_newline ()
    in main 7