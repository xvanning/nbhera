package com.general;

import static org.junit.Assert.assertTrue;

import com.alibaba.fastjson.JSON;
import com.general.common.adapter.ExceptionResultProcessor;
import com.general.common.util.SpiUtils;
import org.junit.Test;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        List<ExceptionResultProcessor> exceptionResultProcessors = SpiUtils.load(ExceptionResultProcessor.class);
        for (ExceptionResultProcessor exceptionResultProcessor : exceptionResultProcessors) {
            Object convert = exceptionResultProcessor.convert("test", "測試", new RuntimeException("hhhh"));
            String json = JSON.toJSONString(convert);
            System.out.println(json);
        }

    }
}
