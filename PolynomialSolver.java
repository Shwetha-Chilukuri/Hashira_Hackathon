import java.io.*;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class PolynomialSolver {

  public static void main(String[] args) {
    try {
      // 1. Parse JSON
      JSONParser parser = new JSONParser();
      JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("input.json"));
      JSONObject keys = (JSONObject) jsonObject.get("keys");

      int k = Integer.parseInt((String) keys.get("k").toString());

      List<BigInteger> xList = new ArrayList<>();
      List<BigInteger> yList = new ArrayList<>();

      for (Object keyObj : jsonObject.keySet()) {
        String keyStr = keyObj.toString();
        if (keyStr.equals("keys"))
          continue;

        int x = Integer.parseInt(keyStr);
        JSONObject point = (JSONObject) jsonObject.get(keyStr);

        int base = Integer.parseInt((String) point.get("base"));
        String value = (String) point.get("value");

        BigInteger y = new BigInteger(value, base);

        xList.add(BigInteger.valueOf(x));
        yList.add(y);

        if (xList.size() == k)
          break;
      }

      // 2. Compute constant term using Lagrange interpolation at x=0
      BigInteger result = lagrangeInterpolationAtZero(xList, yList);
      System.out.println("The constant c is: " + result);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static BigInteger lagrangeInterpolationAtZero(List<BigInteger> x, List<BigInteger> y) {
    int k = x.size();
    BigInteger result = BigInteger.ZERO;

    for (int i = 0; i < k; i++) {
      BigInteger xi = x.get(i);
      BigInteger yi = y.get(i);

      BigInteger numerator = BigInteger.ONE;
      BigInteger denominator = BigInteger.ONE;

      for (int j = 0; j < k; j++) {
        if (j == i)
          continue;
        BigInteger xj = x.get(j);

        numerator = numerator.multiply(xj.negate()); // -xj
        denominator = denominator.multiply(xi.subtract(xj)); // (xi - xj)
      }

      BigInteger term = yi.multiply(numerator).divide(denominator);
      result = result.add(term);
    }

    return result;
  }
}
