package core;


/*
* 这种设计模式，即使用枚举（enum）结合整数值（通常为2的幂次方）来表示不同的令牌类型，是一种常见的、有效的方式来管理和操作多种互斥的状态或分类。在上述例子中，TokenType枚举被用于定义JSON解析过程中可能遇到的各种不同的令牌类型。这里的设计考虑了以下几个方面：

1. 位标志（Bit Flags）
效率：使用2的幂次方作为枚举值，可以利用位操作进行高效的比较和组合。例如，要检查一个整数是否包含了某个特定的枚举值，可以使用按位与（&）操作符。如果结果不为零，则说明该枚举值存在。

扩展性：当需要添加新的令牌类型时，只需添加一个新的枚举成员，并为其分配下一个2的幂次方的值即可。这不会影响到已有的逻辑，因为新值不会与其他任何现有的值冲突。

2. 枚举的封装性
安全性：枚举提供了类型安全的枚举值，防止了在代码中直接使用硬编码的整数值，从而减少了因错误的数值而导致的bug。

清晰性：使用枚举而不是纯数字，使得代码更具可读性。开发者可以很容易地理解TokenType.BEGIN_OBJECT这样的语句，而不需要查阅数字1代表什么含义。

3. 灵活性
组合使用：虽然在这个具体的例子中没有展示，但使用位标志的枚举非常适合于表示多选状态。例如，一个函数可以接受一个整数参数，该参数是多个枚举值的按位或（|）结果，表示它应该处理的令牌类型集合。
4. 减少内存占用
紧凑性：相比于使用字符串或其他复杂类型来表示枚举值，整型值更加紧凑，占用的存储空间少。
综上所述，这种设计不仅提高了代码的可读性和维护性，还优化了运行时的性能和内存使用。在处理大量互斥的分类或状态时，使用枚举结合位标志是一种非常实用和高效的做法。
* */

//TODO 枚举类型的排列顺利有待研究
public enum TokenType {

    BEGIN_OBJECT(1),
    END_OBJECT(2),
    BEGIN_ARRAY(4),
    END_ARRAY(8),
    NULL(16),
    NUMBER(32),
    STRING(64),
    BOOLEAN(128),
    SEP_COLON(256),
    SEP_COMMA(512);

    private final int code;

    TokenType(int code) {
        this.code = code;
    }

    public int getTokenCode() {
        return code;
    }

}
