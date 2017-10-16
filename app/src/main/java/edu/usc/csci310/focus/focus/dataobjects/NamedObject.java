package edu.usc.csci310.focus.focus.dataobjects;

import java.io.*;
import java.util.UUID;

/**
 * An object with a name and unique identifier.
 */

public class NamedObject implements Serializable {
    private static final long serialVersionUID = 2L;

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
        UUID id = UUID.randomUUID();
        this.identifier = "" + id;
    }

    public void setName(String name) {this.name =  name;}

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
        byte[] binaryRepresentation = new byte[50];

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(this);
            binaryRepresentation = baos.toByteArray();
            out.close();
            baos.close();
        }catch(IOException i) {
            i.printStackTrace();
        }

        return binaryRepresentation;
    }

    /**
     * Convert a binary representation into the internal object fields.
     * @param binaryRepresentation The bytewise representation to parse.
     */
    public void NamedObject(byte[] binaryRepresentation) {
        NamedObject no;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(binaryRepresentation);
            ObjectInputStream ois = new ObjectInputStream(bais);
            no = (NamedObject) ois.readObject();
            ois.close();
            bais.close();
        }catch(IOException i) {
            i.printStackTrace();
            return;
        }catch(ClassNotFoundException c) {
            System.out.println("NamedObject class not found.");
            c.printStackTrace();
            return;
        }

        this.name = no.getName();
        this.identifier = no.getIdentifier();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NamedObject)) {
            return false;
        }

        NamedObject other = (NamedObject)obj;
        return (other.getIdentifier().equals(this.getIdentifier()));
    }
}
