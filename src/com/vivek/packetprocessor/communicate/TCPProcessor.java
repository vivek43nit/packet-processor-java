/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivek.packetprocessor.communicate;

import com.vivek.packetprocessor.packet.DataPacket;
import com.vivek.packetprocessor.packet.exception.InvalidPacketException;
import com.vivek.packetprocessor.packet.util.Constants;
import com.vivek.packetprocessor.packet.util.DataLibrary;
import com.vivek.packetprocessor.packet.util.Messages;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vivek
 */
public class TCPProcessor {

    private static final Logger logger = Logger.getLogger(TCPProcessor.class.getName());

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
        logger.log(Level.FINE, "Reading Message Type and Message Length...");
        readNByte(in, buffer, 0, 4, "Error in reading Message Type and Length.");
        logger.log(Level.FINE, "Packet Read Length = 4");

        data.setMessageType(buffer, 0);
        int length = DataLibrary.twoByteToInt(buffer, 2);

        logger.log(Level.INFO, "Message Type : {0}", data.getMessageType());
        logger.log(Level.INFO, "Message Length : {0}", length);
        /**
         * ***********************************************************
         */

        /**
         * *******Reading Payloads***********************************
         */
        length -= 4;      //Due to Message Header
        logger.log(Level.FINE, "Reading Attributes");

        while (length > 0)
        {
            /**
             * ***** Reading Attribute Type and Length***************
             */
            logger.log(Level.FINE, "Reading Next Attrbite :");
            readNByte(in, buffer, 0, 4, "Error in reading Attribute Type and Length.");
            logger.log(Level.FINE, "Read Size = {0}", readPacket);
            tmpAttr = DataLibrary.twoByteToInt(buffer, 0);
            tmpLen = DataLibrary.twoByteToInt(buffer, 2);
            logger.log(Level.FINE, "Attribute Code : {0}", tmpAttr);
            logger.log(Level.FINE, "Attribute Length : {0}", tmpLen);
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
                logger.log(Level.FINE, "Attribute Value Received Length = {0}", totalRead);
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
            logger.log(Level.FINE, "Remaining Packet Length = {0}", length);
        }
        return data;
    }

    public static void send(OutputStream out, DataPacket data) throws IOException
    {
        System.out.println(data);
        if (data.getMessageType() == Messages._ERROR)
        {
            logger.log(Level.INFO, "Error Code : {0}", data.getInt(Messages.ERROR_CODE));
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
