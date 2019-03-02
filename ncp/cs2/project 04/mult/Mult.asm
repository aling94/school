// Multiplication Program
// Designed by Alvin Ling

	@R2			// R2 will store product
	M = 0		// Make sure R2 does not already have a value stored
				   
(LOOP)			// Loop till R0 is added to R2 R1 times	
	@R1     	// R1 will count how many times R0 has been added to R2   
	D = M		// Store R1's value in D
	               
	@END
	D;JEQ		// End the loop once R1 reaches zero (finished adding)
                   
	@R0            
	D = M		// Begin multiplying; store R0's value in D
                   
	@R2            
	M = D + M	// Add R0's value to R2; will loop back until added R1 times
                   
	@R1         
	M = M - 1	// Decrement R1, signifying one round has passed
                   
	@LOOP          
	0;JMP		// Force a jump back to begining of LOOP
	               
(END)			// Will jump here when R0 has been added to R2, R1 times
	@END	       
	0;JMP		// Force an infinite loop to terminate
