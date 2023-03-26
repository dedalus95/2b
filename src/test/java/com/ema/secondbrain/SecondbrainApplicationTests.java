package com.ema.secondbrain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.fail;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class SecondbrainApplicationTests {


    @Test
    void shouldFail() {
        fail("This test should fail");
    }

}
