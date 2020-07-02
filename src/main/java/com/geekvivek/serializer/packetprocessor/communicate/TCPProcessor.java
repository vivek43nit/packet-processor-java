/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geekvivek.serializer.packetprocessor.communicate;

import com.geekvivek.serializer.packetprocessor.packet.DataPacket;
import com.geekvivek.serializer.packetprocessor.packet.exception.InvalidPacketException;
import com.geekvivek.serializer.packetprocessor.packet.util.Constants;
import com.geekvivek.serializer.packetprocessor.packet.util.DataLibrary;
import com.geekvivek.serializer.packetprocessor.packet.util.Messages;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Vivek
 */
@Slf4j
public class TCPProcessor {

    public static DataPacket receive(InputStream in) throws IOException, InvalidPacketException
    {
        DataPacket data = new DataPacket();
        byte[] buffer = new byte[Constants.MAX_DATA_SIZE];
        int tmpAttr = 0;
        int tmpLen = 0;
        int totalRead = 0;
        String tmp;
        int readPacket = 0;

        /**
         * ***Reading Message Type and Message Length from Stream******
         */
        log.debug( "Reading Message Type and Message Length...");
        readNByte(in, buffer, 0, 4, "Error in reading Message Type and Length.");
        log.debug( "Packet Read Length = 4");

        data.setMessageType(buffer, 0);
        int length = DataLibrary.twoByteToInt(buffer, 2);

        log.info( "Message Type : {}", data.getMessageType());
        log.info( "Message Length : {}", length);
        /**
         * ***********************************************************
         */

        /**
         * *******Reading Payloads***********************************
         */
        length -= 4;      //Due to Message Header
        log.debug( "Reading Attributes");

        while (length > 0)
        {
            /**
             * ***** Reading Attribute Type and Length***************
             */
            log.debug( "Reading Next Attrbite :");
            readNByte(in, buffer, 0, 4, "Error in reading Attribute Type and Length.");
            log.debug( "Read Size = {}", readPacket);
            tmpAttr = DataLibrary.twoByteToInt(buffer, 0);
            tmpLen = DataLibrary.twoByteToInt(buffer, 2);
            log.debug( "Attribute Code : {}", tmpAttr);
            log.debug( "Attribute Length : {}", tmpLen);
            /**
             * *****************************************************
             */
            length -= 4;

            /**
             * *****Reading Attribute ******************************
             */
            if (tmpLen > 0)
            {
                byte[] t;
                if (tmpLen > Constants.MAX_DATA_SIZE)
                {
                    t = new byte[tmpLen];
                }
                else
                {
                    t = buffer;
                }
                readNByte(in, t, 0, tmpLen, "Error in reading Attribute value for Attribute Type=" + tmpAttr);
                log.debug( "Attribute Value Received Length = {}", totalRead);
                data.setByteArray(tmpAttr, t, tmpLen);
            }
            else
            {
                data.setByteArray(tmpAttr, new byte[0]);
            }
            /**
             * ****************************************************
             */
            length -= tmpLen;
            log.debug( "Remaining Packet Length = {}", length);
        }
        return data;
    }

    public static void send(OutputStream out, DataPacket data) throws IOException
    {
        System.out.println(data);
        if (data.getMessageType() == Messages._ERROR)
        {
            log.info( "Error Code : {}", data.getInt(Messages.ERROR_CODE));
        }
        byte[] tmp = new byte[2];
        getBytes(tmp, data.getMessageType());
        out.write(tmp, 0, 2);
        getBytes(tmp, data.getMessageLength());
        out.write(tmp, 0, 2);

        HashMap<Integer, byte[]> attr = data.getAttributes();
        for (Map.Entry<Integer, byte[]> entrySet : attr.entrySet())
        {
            Integer key = entrySet.getKey();
            byte[] value = entrySet.getValue();
            getBytes(tmp, key);
            out.write(tmp, 0, 2);
            if (value == null || value.length == 0)
            {
                tmp[0] = 0;
                tmp[1] = 0;
                out.write(tmp, 0, 2);
            }
            else
            {
                getBytes(tmp, value.length);
                out.write(tmp, 0, 2);
                out.write(value, 0, value.length);
            }
        }
        out.flush();
    }

    private static void readNByte(InputStream in, byte[] buffer, int offset, int length, String exceptionMessage) throws IOException, InvalidPacketException
    {
        int totalRead = 0;
        int readPacket;
        while (totalRead != length)
        {
            readPacket = in.read(buffer, offset+totalRead, length - totalRead);
            if (readPacket <= 0)
            {
                throw new InvalidPacketException(exceptionMessage);
            }
            totalRead += readPacket;
        }
    }

    private static void getBytes(byte[] res, int key)
    {
        res[0] = (byte) ((key >>> 8) & 0x00FF);
        res[1] = (byte) (key & 0x00FF);
    }

}
