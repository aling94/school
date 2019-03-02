//Division Program
//Designed by Alvin Ling
//R0 Mod R1 -->R2


	@R0	//Dividend
	D=M	//D=R0
	@R2	//Remainder
	M=D	//R2=R0; R1 will be subtracted from R2, which holds R0's value
	@R3	//Quotient
	M=0

(LOOP)		//Repeatedly subtract R1 from R0 and store in R2
	@R2
	D=M	//D=R2
	@RMD
	D;JLT	//Check if R2<0, meaning remainder was greater than 0
	@END
	D;JEQ	//Check if R2=0, meaning remainder is 0

	@R1
	D=M	//D=R1

	@R2
	M=M-D	//R2=R0-R1
	
	@R3
	M=M+1

	@LOOP
	0;JMP	//Jump to LOOP


(RMD)		//Will jump here to get remainder by adding R1 back to R2 if negative value attained
	@R1
	D=M	//D=R1

	@R2
	M=D+M	//R2=R2+R1

	@R3
	M=M-1

	@END
	0;JMP	//Jump to END


(END)
	@END
	0;JMP	//Loop till halt