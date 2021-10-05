package com.flashsale.seckill;

class ParameterMystery {
    public static void main(String[] args) {
        String literal = "8";
        String brace = "semi";
        String paren = brace;
        String semi = "brace";
        String java = "42";

        param(java, brace, semi);
        param(literal, paren, java);
        param(brace, semi, "literal");
        param("cse", literal + 4, "1");
    }

    public static void param(String semi, String java, String brace) {
        System.out.println(java + " missing a " + brace + " and " + semi);
    }
}
