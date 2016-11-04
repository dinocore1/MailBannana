package com.devsmart.mailbannana;

import java.io.IOException;
import java.io.Reader;


public class CRLFReader extends Reader {

    private final int EOF = -1;
    private final int CR = 13;
    private final int LF = 10;


    private final Reader mReader;
    private final StringBuilder mBuilder = new StringBuilder();

    public CRLFReader(Reader reader) {
        mReader = reader;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return mReader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        mReader.close();
    }

    public String readLine() throws IOException {
        int state = 0;
        mBuilder.setLength(0);

        while(true) {
            int value = mReader.read();
            mBuilder.append((char)value);

            switch(value) {
                case EOF:
                    return null;

                case CR:
                    state = 1;
                    break;

                case LF:
                    if(state == 1) {
                        return mBuilder.substring(0, mBuilder.length()-2);
                    }
                    state = 0;
                    break;
            }
        }
    }
}
