package com.devsmart.mailbannana;


import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

public class CRLFReaderTest {


    @Test
    public void testReadLine() throws Exception {
        CRLFReader reader = new CRLFReader(new StringReader("hello world\r\n"));
        String line = reader.readLine();
        assertEquals("hello world", line);

        line = reader.readLine();
        assertNull(line);
    }

    @Test
    public void testCRWithoutLF() throws Exception {

        CRLFReader reader = new CRLFReader(new StringReader("hello\rworld\r\n"));
        String line = reader.readLine();
        assertEquals("hello\rworld", line);

    }

    @Test
    public void testLFWithoutCR() throws Exception {

        CRLFReader reader = new CRLFReader(new StringReader("hello\nworld\r\n"));
        String line = reader.readLine();
        assertEquals("hello\nworld", line);

    }
}
