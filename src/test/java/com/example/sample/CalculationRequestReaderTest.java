package com.example.sample;

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
    String[] result = requestReader.readRequest();

    //then
    Assertions.assertThat(result[0]).isEqualTo("2");
    Assertions.assertThat(result[1]).isEqualTo("+");
    Assertions.assertThat(result[2]).isEqualTo("3");
  }
}