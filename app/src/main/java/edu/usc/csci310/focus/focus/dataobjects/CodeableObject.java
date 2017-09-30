package edu.usc.csci310.focus.focus.dataobjects;

import java.io.Serializable;

/**
 * Encodeable and decodeable binary objects.
 */

public interface CodeableObject extends Serializable {
    /**
     * Parse the object into a bytewise representation.
     * @return A bytewise representation of the object.
     */
    public byte[] getBinaryRepresentation();

    /**
     * Convert a binary representation into the internal object fields.
     * @param binaryRepresentation The bytewise representation to parse.
     */
    public void CodeableObject(byte[] binaryRepresentation);
}
