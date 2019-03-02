//I/O Handling Program: Fill.hack
//Designed by Alvin Ling

(FILL)			// Fills each pixel with black when a key is pressed from left to right
	@KBD	       
	D = M		// D = KBD, for Jump condition
		           
	@CLEAR	       
	D;JEQ		// If KBD = 0 (No key pressed), jump to CLEAR
		           
	@24575		// Set address to Screen's last pixel
	D = M		// Assign the last pixel's value to D
		           
	@CLEAR		   
	D;JNE		// If the last pixel is filled, jump to CLEAR to keep FILL from going past the last screen register
	               
	@inc		// Declare an incrementer for the Screen location to move on to next register
	D = M		// Assign value to D (D = inc), will be used to move to next pixel
                   
	@SCREEN        
	A = A + D	// Set address to @SCREEN + inc (inc's value stored in D)
	M = -1		// Set Screen register to - 1 (All 1's in 16 bit) to fill pixels
	               
	@inc           
	M = M + 1	// Increment the incrementer by 1 so FILL fills next Screen register
	               
	@FILL	       
	0;JMP		// Force a jump to beginning of FILL
	               
(CLEAR)			// Clears filled pixels starting from the last filled pixel going backwards
	@KBD	       
	D = M		// Assign KBD value to D (D = KBD) for Jump condition
	               
	@FILL	       
	D;JNE		// If KBD != 0 (Key is pressed), jump to FILL
		           
	@SCREEN	       
	D = M		// D = SCREEN (first register), for Jump condition
	@FILL	       
	D;JEQ		// If first pixel is already cleared, jump to FILL
	               
	@inc	       
	D = M		// Store the incrementer's value to D
		           
	@SCREEN	       
	A = A + D	// Set address to @SCREEN + inc, CLEAR will start clearing at this address
	M = 0		// Set register at @SCREEN + inc to 0, clearing the pixels
                   
	@inc           
	M = M - 1	// Decrement inc by 1 so CLEAR will move to the previous register
                   
	@CLEAR         
	0;JMP		// Force a jump back to beginning of CLEAR
                   