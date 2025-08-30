PolynomialSolver.java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PolynomialSolver {
    public static void main(String[] var0) {
        Scanner var1 = new Scanner(System.in);
        StringBuilder var2 = new StringBuilder();

        while(var1.hasNextLine()) {
            String var3 = var1.nextLine();
            var2.append(var3).append("\n");
            if (var3.trim().equals("}")) {
                break;
            }
        }

        solvePolynomial(var2.toString());
    }

    public static void solvePolynomial(String var0) {
        try {
            HashMap var1 = new HashMap();
            int var2 = 0;
            int var3 = 0;
            String[] var4 = var0.split("\n");

            for(int var5 = 0; var5 < var4.length; ++var5) {
                String var6 = var4[var5].trim();
                if (var6.contains("\"n\":")) {
                    String var7 = var6.split("\"n\":")[1];
                    var2 = Integer.parseInt(var7.split(",")[0].trim());
                }

                if (var6.contains("\"k\":")) {
                    String var19 = var6.split("\"k\":")[1];
                    var3 = Integer.parseInt(var19.split("}")[0].trim());
                }

                if (var6.matches("\"\\d+\":\\s*\\{")) {
                    int var20 = Integer.parseInt(var6.split("\"")[1]);
                    int var8 = 0;
                    String var9 = "";

                    for(int var10 = 1; var10 <= 3 && var5 + var10 < var4.length; ++var10) {
                        String var11 = var4[var5 + var10].trim();
                        if (var11.startsWith("\"base\":")) {
                            String var12 = var11.split("\"base\":")[1];
                            var8 = Integer.parseInt(var12.split(",")[0].trim().replace("\"", ""));
                        } else if (var11.startsWith("\"value\":")) {
                            String var28 = var11.split("\"value\":")[1];
                            var9 = var28.split("}")[0].trim().replace("\"", "");
                        }
                    }

                    if (var8 > 0 && !var9.isEmpty()) {
                        var1.put(var20, new Root(var8, var9));
                    }
                }
            }

            System.out.println("Parsed input:");
            System.out.println("n = " + var2 + ", k = " + var3);
            System.out.println("Roots:");

            for(Map.Entry var16 : var1.entrySet()) {
                System.out.println("Root " + var16.getKey() + ": base=" + ((Root)var16.getValue()).base + ", value=" + ((Root)var16.getValue()).value);
            }

            ArrayList var15 = new ArrayList();

            for(int var17 = 1; var17 <= var2; ++var17) {
                if (var1.containsKey(var17)) {
                    Root var21 = (Root)var1.get(var17);
                    BigInteger var24 = convertToDecimal(var21.value, var21.base);
                    var15.add(var24);
                    System.out.println("Root " + var17 + " in decimal: " + var24);
                }
            }

            if (var15.size() >= var3 && var3 > 0) {
                List var18 = var15.subList(0, var3);
                System.out.println("\nUsing first " + var3 + " roots for interpolation:");

                for(int var22 = 0; var22 < var18.size(); ++var22) {
                    System.out.println("Root " + (var22 + 1) + ": " + var18.get(var22));
                }

                List var23 = lagrangeInterpolation(var18);
                System.out.println("\nPolynomial coefficients (from highest degree to constant):");

                for(int var25 = 0; var25 < var23.size(); ++var25) {
                    System.out.println("Coefficient of x^" + (var23.size() - 1 - var25) + ": " + var23.get(var25));
                }

                System.out.println("\nVerification:");

                for(int var26 = 0; var26 < var18.size(); ++var26) {
                    BigInteger var27 = evaluatePolynomial(var23, (BigInteger)var18.get(var26));
                    System.out.println("P(" + var18.get(var26) + ") = " + var27 + " (should be 0)");
                }
            } else {
                System.out.println("Error: Not enough roots or invalid k value");
            }
        } catch (Exception var13) {
            System.err.println("Error parsing input: " + var13.getMessage());
            var13.printStackTrace();
        }

    }

    public static BigInteger convertToDecimal(String var0, int var1) {
        if (var1 == 10) {
            return new BigInteger(var0);
        } else {
            BigInteger var2 = BigInteger.ZERO;
            BigInteger var3 = BigInteger.valueOf((long)var1);
            BigInteger var4 = BigInteger.ONE;

            for(int var5 = var0.length() - 1; var5 >= 0; --var5) {
                char var6 = var0.charAt(var5);
                int var7;
                if (var6 >= '0' && var6 <= '9') {
                    var7 = var6 - 48;
                } else if (var6 >= 'a' && var6 <= 'f') {
                    var7 = var6 - 97 + 10;
                } else {
                    if (var6 < 'A' || var6 > 'F') {
                        throw new IllegalArgumentException("Invalid character: " + var6);
                    }

                    var7 = var6 - 65 + 10;
                }

                if (var7 >= var1) {
                    throw new IllegalArgumentException("Digit " + var7 + " is invalid for base " + var1);
                }

                var2 = var2.add(BigInteger.valueOf((long)var7).multiply(var4));
                var4 = var4.multiply(var3);
            }

            return var2;
        }
    }

    public static List<BigInteger> lagrangeInterpolation(List<BigInteger> var0) {
        int var1 = var0.size();
        ArrayList var2 = new ArrayList();

        for(int var3 = 0; var3 <= var1; ++var3) {
            var2.add(BigInteger.ZERO);
        }

        var2.set(0, BigInteger.ONE);

        for(int var6 = 0; var6 < var1; ++var6) {
            ArrayList var4 = new ArrayList();

            for(int var5 = 0; var5 <= var1; ++var5) {
                var4.add(BigInteger.ZERO);
            }

            for(int var7 = 0; var7 < var2.size(); ++var7) {
                if (var7 < var1) {
                    var4.set(var7 + 1, ((BigInteger)var4.get(var7 + 1)).add((BigInteger)var2.get(var7)));
                }

                var4.set(var7, ((BigInteger)var4.get(var7)).add(((BigInteger)var2.get(var7)).multiply(((BigInteger)var0.get(var6)).negate())));
            }

            var2 = var4;
        }

        return var2;
    }

    public static BigInteger evaluatePolynomial(List<BigInteger> var0, BigInteger var1) {
        BigInteger var2 = BigInteger.ZERO;
        BigInteger var3 = BigInteger.ONE;

        for(int var4 = 0; var4 < var0.size(); ++var4) {
            var2 = var2.add(((BigInteger)var0.get(var4)).multiply(var3));
            var3 = var3.multiply(var1);
        }

        return var2;
    }

    static class Root {
        int base;
        String value;

        Root(int var1, String var2) {
            this.base = var1;
            this.value = var2;
        }
    }
}