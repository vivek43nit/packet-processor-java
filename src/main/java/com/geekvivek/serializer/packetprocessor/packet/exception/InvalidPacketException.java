/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geekvivek.serializer.packetprocessor.packet.exception;

/**
 * This Exception is thrown by TCPProcessor while sending and receiving
 * if the packet don't follow our defined format.
 * @author Vivek
 */
public class InvalidPacketException extends Exception{
    public InvalidPacketException() {
        super("Invalid Packet");
    }
    public InvalidPacketException(String message) {
        super(message);
    }
    public InvalidPacketException(Throwable cause) {
        super(cause);
    }
    public InvalidPacketException(String message, Throwable cause) {
        super(message, cause);
    }
}
