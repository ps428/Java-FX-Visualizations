package Utility;

import java.io.*;

public class nodeCopy {

    public static <S extends Serializable> S clone(S node) {
        S copyofnode = null;
        try {
            ByteArrayOutputStream arrayOutstream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutstream);
            objectOutputStream.writeObject(node);
            objectOutputStream.close();

            ByteArrayInputStream arrayInstream = new ByteArrayInputStream(arrayOutstream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(arrayInstream);
            copyofnode = (S) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return copyofnode;
    }
}
