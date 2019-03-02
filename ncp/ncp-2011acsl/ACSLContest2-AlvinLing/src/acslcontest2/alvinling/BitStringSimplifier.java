package acslcontest2.alvinling;

public class BitStringSimplifier
{
    private String[] bits;
    private int lengths, num;
    
    public static void main(String[] args)
    {
        // Sample Data
        String [] sample_input1 = {"10", "11"};
        String [] sample_input2 = {"001", "110"};
        String [] sample_input3 = {"000", "001"};
        String [] sample_input4 = {"00", "01", "10", "11"};
        String [] sample_input5 = {"000", "101", "000", "100"};

        BitStringSimplifier bbs = new BitStringSimplifier(2, sample_input1);
        System.out.println("SAMPLE TEST: 1. " + bbs.compare());
        bbs = new BitStringSimplifier(2, sample_input2);
        System.out.println("SAMPLE TEST: 2. " + bbs.compare());
        bbs = new BitStringSimplifier(2, sample_input3);
        System.out.println("SAMPLE TEST: 3. " + bbs.compare());
        bbs = new BitStringSimplifier(4, sample_input4);
        System.out.println("SAMPLE TEST: 4. " + bbs.compare());
        bbs = new BitStringSimplifier(4, sample_input5);
        System.out.println("SAMPLE TEST: 5. " + bbs.compare() + "\n\n");
        
        //Official Test Data
        String [] test_input1 = {"1", "0"};
        String [] test_input2 = {"01", "00"};
        String [] test_input3 = {"001", "010"};
        String [] test_input4 = {"100", "110", "101", "111"};
        String [] test_input5 = {"000", "001", "010", "011", "100", "101", "110", "111"};

        bbs = new BitStringSimplifier(2, test_input1);
        System.out.println("OFFICIAL TEST: 1. " + bbs.compare());
        bbs = new BitStringSimplifier(2, test_input2);
        System.out.println("OFFICIAL TEST: 2. " + bbs.compare());
        bbs = new BitStringSimplifier(2, test_input3);
        System.out.println("OFFICIAL TEST: 3. " + bbs.compare());
        bbs = new BitStringSimplifier(4, test_input4);
        System.out.println("OFFICIAL TEST: 4. " + bbs.compare());
        bbs = new BitStringSimplifier(8, test_input5);
        System.out.println("OFFICIAL TEST: 5. " + bbs.compare());
    }
    
    public BitStringSimplifier(int num, String[] bits)
    {
        boolean valid = !(bits == null || bits.length > 8 || bits.length != num);
        if (valid)
        {
            checkValidity:
            for (String temp : bits)
            {
                if (!temp.matches("[01]{1,3}") || temp.length() != bits[0].length())
                {
                    valid = false;
                    break checkValidity;
                }
            }
            if (valid)
            {
                this.bits = bits;
                this.num = num;
                this.lengths = bits[0].length();
            } else
                this.bits = null;
        } else
            this.bits = null;
    }
    
    public String compare()
    {
        if(this.bits == null)
            return "Comparison failed. Please check that parameters are correct.";
        else
        {
            switch (lengths)
            {
                default: return "Comparison failed. Please check that parameters are correct.";
                case 1: return this.compare1();
                case 2: return this.compare2();
                case 3: return this.compare3();
            }
        }
    }
    
    private String compare1()
    {
        int numZeros = 0;
        int numOnes = 0;
        for(String bitstring : bits)
        {
            if(bitstring.matches("0"))
                numZeros++;
            else numOnes++;
        }
        if(numZeros > 0 && numOnes > 0)
            return "*";
        else if(numZeros > 0 && numOnes == 0)
            return "0";
        else
            return "1";
    }
    
    public String compare2()
    {
        String first, second;
        int[] zerosCounter = {0, 0};
        int[] onesCounter = {0, 0};
        for(String bitstring: bits)
        {
            if(bitstring.matches(".0"))
                zerosCounter[0]++;
            else
               onesCounter[0]++;
            if(bitstring.matches("0."))
                zerosCounter[1]++;
            else
                onesCounter[1]++;
        }
        if(onesCounter[0] == 1 && zerosCounter[0] == 1 && onesCounter[1] == 1 && zerosCounter[1] == 1)
            return "NONE";
        else
        {
            if(zerosCounter[0] == this.num)
                first = "0";
            else if(onesCounter[0] == this.num)
                first = "1";
            else first = "*";
            if(zerosCounter[1] == this.num)
                second = "0";
            else if(onesCounter[1] == this.num)
                second = "1";
            else second = "*";
        }
        return second + first;
    }
    
    private String compare3()
    {
        String first, second, third;
        int[] zerosCounter = {0, 0, 0};
        int[] onesCounter = {0, 0, 0};
        for(String bitstring: bits)
        {
            if(bitstring.matches("..0"))
                zerosCounter[0]++;
            else
               onesCounter[0]++;
            if(bitstring.matches(".0."))
                zerosCounter[1]++;
            else
                onesCounter[1]++;
            if(bitstring.matches("0.."))
                zerosCounter[2]++;
            else
                onesCounter[2]++;
        }
        int checkNone = 0;
        for(int i = 0; i < 3; i++)
        {
            if(zerosCounter[i] == 1)    checkNone++;
            if(onesCounter[i] == 1)     checkNone++;
            if(checkNone >= 4)
                return "NONE";
        }
        if (zerosCounter[0] == this.num)
            first = "0";
        else if (onesCounter[0] == this.num)
            first = "1";
        else
            first = "*";
        if (zerosCounter[1] == this.num)
            second = "0";
        else if (onesCounter[1] == this.num)
            second = "1";
        else
            second = "*";
        if (zerosCounter[2] == this.num)
            third = "0";
        else if (onesCounter[2] == this.num)
            third = "1";
        else
            third = "*";
        return third + second + first;
    }
    
}
