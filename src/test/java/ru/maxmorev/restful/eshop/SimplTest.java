package ru.maxmorev.restful.eshop;

import ru.maxmorev.restful.eshop.annotation.AttributeDataType;

public class SimplTest {

    public static void main(String... args) throws ClassNotFoundException {

        Float f = 3.5f;
        Float f2 = new Float(3.5f);

        Object o1 = f;
        Object o2 = f2;
        if(o1.equals(o2)){
            System.out.println(o1);
            System.out.println(o2);
            System.out.println("O1 equqls O2");
        }

        AttributeDataType dt = AttributeDataType.valueOf("Text");
        System.out.println(dt);
        System.out.println( Class.forName("java.lang.String") );

    }
}
