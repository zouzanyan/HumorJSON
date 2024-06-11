package core;

public class CharScanner {
    private char[] charArr;
    private int pos = 0;

    public CharScanner(String jsonString) {
        charArr = jsonString.toCharArray();
    }

    // 获取下标对应的字符，并向后移动指针
    public char nextChar(){

        // 判断越界
        if (!hasMore()){
            return (char) -1;
        }
        return charArr[pos++];
    }

    // 向左移动指针，获取下标对应的值
    public char peekChar(){
        pos--;
        // 判断越界
        if (pos < 0){
            return (char) -1;
        }
        return charArr[pos];
    }

    public boolean hasMore(){
        return pos < charArr.length;
    }

}
