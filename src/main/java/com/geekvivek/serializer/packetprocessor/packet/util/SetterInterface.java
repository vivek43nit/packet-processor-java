/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geekvivek.serializer.packetprocessor.packet.util;

import java.util.ArrayList;

/**
 *
 * @author Vivek
 */
public interface SetterInterface {
    void setString(int key, String s);
    void setInt(int key, int value);
    void setLong(int key, long value);
    void setLongLong(int key, long value);
    void setByteArray(int key, byte[] data, int length);
    void setByteArray(int key, byte[] data);
    void setByteArray(int key, Byte[] data, int length);
    void setByteArray(int key, Byte[] data);
    void setByteArrayList(int key, ArrayList<Byte> data, int length);
    void setByteArrayList(int key, ArrayList<Byte> data);
    void setByte(int key, byte data);
}
