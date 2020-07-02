/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geekvivek.serializer.packetprocessor.packet.util;

/**
 * 
 * @author Vivek
 */
public interface GetterInterface {
    String getString(int key);
    Integer getInt(int key);
    Long getLong(int key);
    Long getLongLong(int key);
    Byte getByte(int key);
    byte[] getByteArray(int key);
}
