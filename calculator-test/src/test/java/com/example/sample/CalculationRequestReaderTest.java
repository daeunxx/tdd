package com.example.sample;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculationRequestReaderTest {

  @Test
  public void 데이터_읽기() {
    //given
    CalculationRequestReader requestReader = new CalculationRequestReader();

    //when
    System.setIn(new ByteArrayInputStream("2 + 3".getBytes()));
    CalculationRequest request = requestReader.read();

    //then
    assertThat(request.getNum1()).isEqualTo(2);
    assertThat(request.getNum2()).isEqualTo(3);
    assertThat(request.getOperator()).isEqualTo("+");
  }
}