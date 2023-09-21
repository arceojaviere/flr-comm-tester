package org.tests.flr.flrcommtester.util;

import java.util.LinkedList;

public class LinkedByteBuffer {
    
    private LinkedList<byte[]> buffer;
    
    public LinkedByteBuffer(){
        this.buffer = new LinkedList<>();
    }

    public void put(byte[] src, int start, int length) {
        byte[] newBuff = new byte[length - start];

        for(int pointer = start; pointer < length; pointer ++){
            newBuff[pointer - start] = src[pointer];
        }
        this.buffer.add(newBuff);
    }

    public byte[] array() {
        byte[] newArray = new byte[this.calculateLength()];
        int pointer = 0;
        for(byte[] buff : this.buffer){
            for(byte b : buff){
                newArray[pointer] = b;
                pointer ++;
            }
        }
        return newArray;
    }

    public int calculateLength(){
        int totalLength = 0;
        for(byte[] buff : this.buffer){
            totalLength += buff.length;
        }

        return totalLength;
    }


    
}
