/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivek.packetprocessor.packet.util;

/**
 * 
 * @author Vivek
 */
public interface GetterInterface {
    public String getString(int key);
    public Integer getInt(int key);
    public Long getLong(int key);
    public Long getLongLong(int key);
    public Byte getByte(int key);
    public byte[] getByteArray(int key);
}
