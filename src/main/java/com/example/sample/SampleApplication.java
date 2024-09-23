package com.example.sample;

import java.util.Scanner;

public class SampleApplication {

  public static void main(String[] args) {
    String[] parts = new CalculationRequestReader().readRequest();
    long num1 = Long.parseLong(parts[0]);
    long num2 = Long.parseLong(parts[2]);
    String operator = parts[1];

    long answer = new Calculator().calculate(num1, num2, operator);
    System.out.println(answer);
  }
}
