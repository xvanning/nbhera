package com.general;

import static org.junit.Assert.assertTrue;

import com.general.common.adapter.ExceptionResultProcessor;
import com.general.common.util.SpiUtils;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        SpiUtils.load(ExceptionResultProcessor.class);

    }
}
