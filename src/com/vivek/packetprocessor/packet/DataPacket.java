/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivek.packetprocessor.packet;

import com.vivek.packetprocessor.packet.util.Constants;
import java.util.ArrayList;
import com.vivek.packetprocessor.packet.util.SetterInterface;
import com.vivek.packetprocessor.packet.util.GetterInterface;
import com.vivek.packetprocessor.packet.util.DataLibrary;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class will work as a abstraction layer between server and client application.
 * It internally stores the Message Type as int, Message Total length as
 * int, and attributes in a HashMap&lt;Integer, byte[]&gt; .
 *      You can get the attributes in any data type you want just as ResultSet.
 * It will internally converts the byte array in the required data type when 
 * corresponding get function will be called and will return that.
 * But in the case of get byte array it will not perform any conversion.
 *      So please don't call its get functions frequently for a attribute rather call
 * that once and use that returned value further.
 *  
 * @author Vivek
 * 
 */
public class DataPacket implements GetterInterface,SetterInterface{
    private int messageType;
    private int messageLength;
    private HashMap<Integer, byte[]> attributes;
    
    /**
     * Initialize this object as empty.
     */
    public DataPacket() {
        messageLength=4;
        attributes = new HashMap<Integer, byte[]>();
    }

    /**
     * Returns the current message type of this DataPacket
     * @return messageType
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Sets the message type of this DataPacket
     * @param messageType
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * Sets the message type of this DataPacket by parsing the integer value
     * stored as byte array in buffer from index offset
     * @param buffer byte array from where the two byte will be read
     * @param offset index from where the two byte will be read
     */
    public void setMessageType(byte[] buffer, int offset) {
        this.messageType = DataLibrary.twoByteToInt(buffer, offset);
    }
    
    /**
     * Returns the total Message Length of this DataPacket if it will be converted
     * back in byte array packet
     * @return message length
     */
    public int getMessageLength() {
        return messageLength;
    }

    /**
     * Returns the HashMap of the attributes,  this should not be used by applications.
     *      This function is used by TCPProcessor for getting all attributes
     * and converting that into byte array packet.
     * @return HashMap of attributes
     */
    
    public HashMap<Integer, byte[]> getAttributes() {
        return attributes;
    }
    
    /**
     * This method will return a String representation of this DataPacket.
     * @return 
     */
    @Override
    public String toString() {
        return "DataPacket{" + "messageType=" + messageType + ", messageLength=" + messageLength + ", attributes=" + attributes + '}';
    }

    /**
     * Returns the data for key attribute as String
     * @param key attribute to get
     * @return null if key not found, else value for key attribute as String
     */
    @Override
    public String getString(int key) {
        byte[] t = attributes.get(key);
        if(t!=null)
            return new String(t,Constants.charset);
        return null;
    }

    /**
     * Returns the data for key as Int
     * @param key attribute to get
     * @return null if key not found , else value for key attribute as Int
     */
    @Override
    public Integer getInt(int key) {
        byte[] t = attributes.get(key);
        if(t!=null)
            return DataLibrary.twoByteToInt(t,0);
        return null;
    }

    /**
     * Returns the data for key as Long(just four byte)
     * @param key attribute to get
     * @return null if key not found , else value for key attribute as Long
     */
    @Override
    public Long getLong(int key) {
        byte[] t = attributes.get(key);
        if(t!=null)
            return DataLibrary.fourByteToLong(t,0);
        return null;
    }
    
    /**
     * Returns the data for key as Long(eight byte)
     * @param key attribute to get
     * @return null if key not found , else value for key attribute as Long
     */
    @Override
    public Long getLongLong(int key) {
        byte[] t = attributes.get(key);
        if(t!=null)
            return DataLibrary.eightByteToLong(t,0);
        return null;
    }

    /**
     *
     * @param key
     * @param s
     */
    @Override
    public void setString(int key, String s) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        messageLength+= 4+s.length();
        attributes.put(key, s.getBytes(Constants.charset));
    }

    /**
     *
     * @param key
     * @param value
     */
    @Override
    public void setInt(int key, int value) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        messageLength+=6;
        byte[] t = new byte[2];
        t[0] = (byte) ((value >>> 8) & 0x00FF);
        t[1] = (byte) (value & 0x00FF);
        attributes.put(key, t);
    }

    /**
     *
     * @param key
     * @param value
     */
    @Override
    public void setLong(int key, long value) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        messageLength+=8;
        byte[] t = new byte[4];
        t[0] = (byte) ((value >>> 24) & 0x00FF);
        t[1] = (byte) ((value >>> 16) & 0x00FF);
        t[2] = (byte) ((value >>> 8) & 0x00FF);
        t[3] = (byte) (value & 0x00FF);
        attributes.put(key, t);
    }

    /**
     *
     * @param key
     * @param value
     */
    @Override
    public void setLongLong(int key, long value) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        messageLength+=12;
        byte[] t = new byte[8];
        t[0] = (byte) ((value >>> 56) & 0x00FF);
        t[1] = (byte) ((value >>> 48) & 0x00FF);
        t[2] = (byte) ((value >>> 40) & 0x00FF);
        t[3] = (byte) ((value >>> 32) & 0x00FF);
        t[4] = (byte) ((value >>> 24) & 0x00FF);
        t[5] = (byte) ((value >>> 16) & 0x00FF);
        t[6] = (byte) ((value >>> 8) & 0x00FF);
        t[7] = (byte) (value & 0x00FF);
        attributes.put(key, t);
    }

    /**
     *
     * @param key
     * @param data
     * @param length
     */
    @Override
    public void setByteArray(int key, byte[] data,int length) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        if(length==0){
            messageLength+=4;
            attributes.put(key, new byte[0]);
            return;
        }
        messageLength+=4+length;
        byte[] t = new byte[length];
        for(int i=0; i<length && i<data.length; i++)
            t[i] = data[i];
        attributes.put(key,t);
    }

    /**
     * Returns the data for key as Byte
     * @param key attribute to get
     * @return null if key not found , else value for key attribute as Byte
     */
    @Override
    public Byte getByte(int key) {
        byte[] t = attributes.get(key);
        if(t != null)
            return t[0];
        return null;
    }

    /**
     *
     * @param key
     * @param data
     */
    @Override
    public void setByte(int key, byte data) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        messageLength+=5;
        byte[] t = new byte[1];
        t[0] = data;
        attributes.put(key, t);
    }

    /**
     *
     * @param key
     * @param data
     * @param length
     */
    @Override
    public void setByteArray(int key, Byte[] data, int length) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        if(length==0){
            messageLength+=4;
            attributes.put(key, null);
            return;
        }
        messageLength+=4+length;
        byte[] t = new byte[length];
        for(int i=0; i<length && i<data.length; i++)
            t[i] = data[i];
        attributes.put(key,t);
    }

    /**
     *
     * @param key
     * @param data
     * @param length
     */
    @Override
    public void setByteArrayList(int key, ArrayList<Byte> data, int length) {
        if(attributes.containsKey(key)){
            messageLength-= 4+attributes.get(key).length;
        }
        if(length==0){
            messageLength+=4;
            attributes.put(key, null);
            return;
        }
        messageLength+=4+length;
        byte[] t = new byte[length];
        Iterator<Byte> it = data.iterator();
        for(int i=0; i<length && i<data.size(); i++)
            t[i] = it.next();
        attributes.put(key,t);
    }
    
    /**
     * return a reference of the actual byte array that is read for the key
     * attribute if key exist other wise null. Please be careful while changing
     * the returned byte array by this method, because it will also change the byte[] of this
     * DataPacket for the key.
     * @param key key attribute to get
     * @return a reference of byte[] for the key attribute if exist, or null
     */
    @Override
    public byte[] getByteArray(int key) {
        return attributes.get(key);
    }

    /**
     *
     * @param key
     * @param data
     */
    @Override
    public void setByteArray(int key, byte[] data) {
        setByteArray(key, data, data.length);
    }

    /**
     *
     * @param key
     * @param data
     */
    @Override
    public void setByteArray(int key, Byte[] data) {
        setByteArray(key, data, data.length);
    }

    /**
     *
     * @param key
     * @param data
     */
    @Override
    public void setByteArrayList(int key, ArrayList<Byte> data) {
        setByteArrayList(key, data, data.size());
    }
    
}
