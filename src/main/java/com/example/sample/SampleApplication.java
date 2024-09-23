package com.example.sample;

public class SampleApplication {

  public static void main(String[] args) {
    CalculationRequest request = new CalculationRequestReader().read();
    long answer = new Calculator().calculate(
        request.getNum1(),
        request.getNum2(),
        request.getOperator());
    System.out.println(answer);
  }
}
