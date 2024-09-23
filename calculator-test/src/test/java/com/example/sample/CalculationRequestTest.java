package com.example.sample;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CalculationRequestTest {

  @Test
  public void 유효한_숫자_파싱() {
    //given
    String[] parts = {"2", "+", "3"};

    //when
    CalculationRequest request = new CalculationRequest(parts);

    //then
    assertThat(request.getNum1()).isEqualTo(2);
    assertThat(request.getNum2()).isEqualTo(3);
    assertThat(request.getOperator()).isEqualTo("+");
  }

  @Test
  public void 세자리_이상_유효한_숫자_파싱() {
    //given
    String[] parts = {"222", "+", "3333"};

    //when
    CalculationRequest request = new CalculationRequest(parts);

    //then
    assertThat(request.getNum1()).isEqualTo(222);
    assertThat(request.getNum2()).isEqualTo(3333);
    assertThat(request.getOperator()).isEqualTo("+");
  }

  @Test
  public void 숫자가_두개가_아닌_경우() {
    //given
    String[] parts = {"222", "+"};

    //when
    //then
    assertThatThrownBy(() -> new CalculationRequest(parts)).isInstanceOf(BadRequestException.class);
  }

  @Test
  public void 유효하지_않은_연산자() {
    //given
    String[] parts = {"222", "x", "2"};

    //when
    //then
    assertThatThrownBy(() -> new CalculationRequest(parts)).isInstanceOf(InvalidOperatorException.class);
  }

  @Test
  public void 유효하지_않은_길이의_연산자() {
    //given
    String[] parts = {"222", "+-", "2"};

    //when
    //then
    assertThatThrownBy(() -> new CalculationRequest(parts)).isInstanceOf(InvalidOperatorException.class);
  }
}