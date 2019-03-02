	@5
	D = A		//D = 5
	@i			//Declare an intial addend, which doubles as a counter
	M = D		//i = 5
	@sum		//Declare some memory to hold the sum 
	M = 0		//sum = 0
(LOOP)	
	@i	
	D = M		//D = i
	@END	
	D;JEQ		//If i = 0, jump to end
	
	@sum	
	M = D+M		//sum = i+sum

	@i
	M = M - 1	//i = i - 1

	@LOOP
	0;JMP		//Jump to beginning of LOOP
(END)
	@END
	0;JMP		//Terminate by infinite Loop