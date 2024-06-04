package core;

public class Token {
    // 词类型
    private TokenType tokenType;
    // token的值
    private String tokenValue;

    public Token(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.tokenValue = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return tokenValue;
    }

    public void setValue(String value) {
        this.tokenValue = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value='" + tokenValue + '\'' +
                '}';
    }

}
