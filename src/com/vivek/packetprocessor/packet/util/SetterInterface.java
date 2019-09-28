/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivek.packetprocessor.packet.util;

import java.util.ArrayList;

/**
 *
 * @author Vivek
 */
public interface SetterInterface {
    public void setString(int key, String s);
    public void setInt(int key, int value);
    public void setLong(int key, long value);
    public void setLongLong(int key, long value);
    public void setByteArray(int key, byte[] data, int length);
    public void setByteArray(int key, byte[] data);
    public void setByteArray(int key, Byte[] data, int length);
    public void setByteArray(int key, Byte[] data);
    public void setByteArrayList(int key, ArrayList<Byte> data, int length);
    public void setByteArrayList(int key, ArrayList<Byte> data);
    public void setByte(int key, byte data);
}
