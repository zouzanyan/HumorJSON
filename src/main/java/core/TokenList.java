package core;

import java.util.ArrayList;
import java.util.List;

public class TokenList {

    private List<Token> tokens = new ArrayList<>();

    // 记录当前指针位置
    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }

    private List<Token> tokenizer(String jsonStr){
        CharScanner charScanner = new CharScanner(jsonStr);



    }

}
