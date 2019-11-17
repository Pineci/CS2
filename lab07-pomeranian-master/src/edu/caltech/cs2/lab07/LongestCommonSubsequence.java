package edu.caltech.cs2.lab07;

public class LongestCommonSubsequence {

    public static int findLCS(String string1, String string2) {
        int[][] toRet = new int[string1.length() +1 ][string2.length() +1];

        for(int i = 0; i < toRet.length; i++){
            toRet[i][0] = 0;
        }
        for(int j = 0; j < toRet[0].length; j++){
            toRet[0][j] = 0;
        }
        for(int i = 1; i < toRet.length; i++) {
            for (int j = 1; j < toRet[0].length; j++) {
                if(string1.charAt(i-1) == string2.charAt(j-1)){
                    toRet[i][j] = toRet[i-1][j-1] + 1;
                }
                else{
                    toRet[i][j] = Math.max(toRet[i-1][j], toRet[i][j-1]);
                }
            }
        }

        return toRet[string1.length()][string2.length()];
    }

}