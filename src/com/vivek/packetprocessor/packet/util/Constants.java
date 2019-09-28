/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vivek.packetprocessor.packet.util;

import java.nio.charset.Charset;

/**
 * This class holds some necessary constants to be used in Projects
 * @author Vivek
 */
public class Constants {
    
    /**
     * This Constants defines the Charset object to be used while converting
     * a string to byte array or byte array to string, so that no data may loose
     * or changed, because this charset contains one to one mapping for all 
     * ASCII characters from 0-255.
     *      So please always use this charset while converting from string
     * to byte array or from byte array to string in java to make conversion 
     * without losing any data.
     */
    public static final Charset charset= Charset.forName("ISO-8859-1");

    /**
     * Maximum buffer size to be sent to server or maximum buffer size that 
     * you will receive from server
     */
    public static final int MAX_DATA_SIZE = 1024;
}
