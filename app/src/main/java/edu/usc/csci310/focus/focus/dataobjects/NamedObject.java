package edu.usc.csci310.focus.focus.dataobjects;

import java.io.Serializable;
import java.util.UUID;

/**
 * An object with a name and unique identifier.
 */

public class NamedObject implements Serializable {
    private String name = null;
    private String identifier = null;

    /**
     * Create a new NamedObject with a specific name and identifier.
     * @param name The string name of the object.
     * @param identifier The string identifier of the object.
     */
    public NamedObject(String name, String identifier) {
        this.name = name;
        this.identifier = identifier;
    }

    /**
     * Create a new NamedObject with a name and automatically generate an identifier.
     * @param name The string name of the object.
     */
    public NamedObject(String name) {
        this.name = name;
        UUID ident = UUID.randomUUID();
        this.identifier = "" + ident;
    }

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    /* CodeableObject implementation. */
    /**
     * Parse the object into a bytewise representation.
     * @return A bytewise representation of the object.
     */
    public byte[] getBinaryRepresentation() {
        return new byte[50];
    }

    /**
     * Convert a binary representation into the internal object fields.
     * @param binaryRepresentation The bytewise representation to parse.
     */
    public void NamedObject(byte[] binaryRepresentation) {
        // unsure what to do
    }
}
