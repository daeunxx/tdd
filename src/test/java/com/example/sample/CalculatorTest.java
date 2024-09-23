package com.example.sample;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTest {

  @Test
  public void 덧셈_연산() {
    //given
    long num1 = 2;
    long num2 = 3;
    String operator = "+";
    Calculator calculator = new Calculator();

    //when
    long result = calculator.calculate(num1, num2, operator);

    //then
    assertThat(result).isEqualTo(5);
  }

  @Test
  public void 뺄셈_연산() {
    //given
    long num1 = 2;
    long num2 = 3;
    String operator = "-";
    Calculator calculator = new Calculator();

    //when
    long result = calculator.calculate(num1, num2, operator);

    //then
    assertThat(result).isEqualTo(-1);
  }

  @Test
  public void 곱셈_연산() {
    //given
    long num1 = 2;
    long num2 = 3;
    String operator = "*";
    Calculator calculator = new Calculator();

    //when
    long result = calculator.calculate(num1, num2, operator);

    //then
    assertThat(result).isEqualTo(6);
  }

  @Test
  public void 나눗셈_연산() {
    //given
    long num1 = 6;
    long num2 = 3;
    String operator = "/";
    Calculator calculator = new Calculator();

    //when
    long result = calculator.calculate(num1, num2, operator);

    //then
    assertThat(result).isEqualTo(2);
  }
}