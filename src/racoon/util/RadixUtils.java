package racoon.util;

public class RadixUtils {
	
	/**
	 * 判断一个数二进制位每一位都大于等于另一个数的二进制的每一位
	 * @param int1 数字1
	 * @param int2 数字2
	 * @return true|false
	 */
	public static boolean allBitsGteq(int int1,int int2){
		
		for(int i=31 ;i >= 0 ;i --){
			byte bit1 = (byte) (((1<<i)&int1)>>i);
			byte bit2 = (byte) (((1<<i)&int2)>>i);
			if(bit1 - bit2 <0) return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		
		System.out.println(allBitsGteq(21,5));
		
	}
}

