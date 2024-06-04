package core;

public class CharScanner {
    private char[] charArr;
    private int pos = 0;

    public CharScanner(String jsonString) {
        charArr = jsonString.toCharArray();
    }

    // 获取下标对应的字符
    public char nextChar(int pos){

        // 判断越界
        if (pos > charArr.length){
            return (char) -1;
        }

        int p = pos;
        pos++;
        return charArr[p];
    }

}
