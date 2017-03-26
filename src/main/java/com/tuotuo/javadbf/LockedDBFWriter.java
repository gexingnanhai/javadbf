package com.tuotuo.javadbf;

import com.linuxense.javadbf.DBFWriter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;

/**
 * 通过代理方法给DBfWriter类增加文件锁定功能
 * @author tuotuo
 *         Date 2017/3/26 16:35
 */
public class LockedDBFWriter{
    private DBFWriter dbfWriter;
    private RandomAccessFile accessFile;

    public LockedDBFWriter(File dbfFile, Charset charset) throws NoSuchFieldException, IllegalAccessException {
        this.dbfWriter = new DBFWriter(dbfFile,charset);
        final Field rafField = dbfWriter.getClass().getDeclaredField("raf");
        rafField.setAccessible(true);
        this.accessFile = (RandomAccessFile)rafField.get(dbfWriter);
    }

    public void addRecord(Object[] vaules) throws IOException {
        FileLock fileLock = accessFile.getChannel().lock();
        dbfWriter.addRecord(vaules);
        fileLock.release();
    }
}
