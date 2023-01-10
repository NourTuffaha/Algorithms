package Modules.Huffman;

import Modules.Row;
import Modules.hash.HashMap;
import Modules.hash.HashNode;
import Modules.io.stream.BitInputStream;
import Modules.io.stream.BitOutputStream;

import java.io.File;
import java.util.ArrayList;

public class Decompression {

    /**
     * in : used to read from a file as bytes
     */
    private BitInputStream in;

    /**
     * out : is used to write on a file as bytes
     */
    private BitOutputStream out;

    private int numberOfCharInOriginalFile;

    private int countDistinctInOriginal;

    private int countHeader;

    private int countCharInCompressedFile;

    private int[] count = new int[256];

    private StringBuilder encodedOfHeader = new StringBuilder();

    private ArrayList<Row> rows = new ArrayList<Row>();

    private String extensionOfOriginalFile = new String("");

    private final File compressedFile;

    public Decompression(File compressedFile) {

        this.compressedFile = compressedFile;
        in = new BitInputStream(compressedFile);
        numberOfCharInOriginalFile = in.readBits(32);

        int numberOfExtensionCharacters = in.readBits(8);

        for (int i = 0; i < numberOfExtensionCharacters; i++) {

            char character = (char) in.readBits(8);
            this.extensionOfOriginalFile += character;
        }

    }


    public void decompress(File newFile) {

        HuffNode huffmanTree = decodingHeader();
        decompressMessage(huffmanTree, newFile);
        buildTable(huffmanTree);
        out.flush();
        out.close();
        in.close();

    }


    private HuffNode decodingHeader() {


        return decodingHelper();


    }


    private HuffNode decodingHelper() {

        int bit = in.readBits(1);

        if (bit == 0) {

            countHeader++;
            HuffNode internalNode = new HuffNode(256, 0, null, null);
            internalNode.setLeft(decodingHelper());
            internalNode.setRight(decodingHelper());
            return internalNode;

        } else {

            countHeader += 9;
            int character = in.readBits(8);
            return new HuffNode(character, 0, null, null);

        }

    }


    private void decompressMessage(HuffNode huffmanTree, File DecompressedFile) {

        HuffNode current = huffmanTree;
        int bit = 0;
        out = new BitOutputStream(DecompressedFile);

        int numberOfCharInOriginalFileTemp = this.numberOfCharInOriginalFile;
        int countNumberOfBits = 0;

        while (numberOfCharInOriginalFileTemp != 0) {

            bit = in.readBits(1);

            countNumberOfBits++;

            current = (bit != 0) ? current.getRight() : current.getLeft();

            if (current.isLeaf()) {

                int codePoint = current.getCharacter();
                out.writeBits(8, codePoint);
                current = huffmanTree;
                numberOfCharInOriginalFileTemp--;

                count[codePoint]++;
                if (count[codePoint] == 1) {

                    countDistinctInOriginal++;
                }
            }

        }

        this.countCharInCompressedFile = (countNumberOfBits / 8);

        if (countNumberOfBits % 8 > 0) {

            this.countCharInCompressedFile++;
        }

    }


    private void taversal(HuffNode root, String huffmanCode, HashMap<Integer, String> table) {

        if (root != null) {

            taversal(root.getLeft(), huffmanCode + "0", table);

            if (root.isLeaf()) {


                table.put(root.getCharacter(), huffmanCode);

            }

            taversal(root.getRight(), huffmanCode + "1", table);

        }

    }

    private void buildTable(HuffNode root) {

        HashMap<Integer, String> table = new HashMap<>(countDistinctInOriginal);

        if (countDistinctInOriginal != 1) {

            taversal(root, "", table);

        } else {

            table.put(root.getCharacter(), "0");
        }

        fillArrayList(table);

    }


    private void fillArrayList(HashMap<Integer, String> table) {

        HashNode<Integer, String>[] tableTemp = table.getHashTable();

        for (int i = 0; i < table.sizeOfData(); i++) {

            Character character = (char) tableTemp[i].getKey().intValue();
            String code = tableTemp[i].getValue();
            Integer length = code.length();
            Integer frequency = count[tableTemp[i].getKey()];

            Row row = new Row(character, code, length, frequency);

            rows.add(row);

        }

    }


    public int getNumberOfCharInOriginalFile() {
        return numberOfCharInOriginalFile;
    }


    public StringBuilder getEncodedOfHeader() {
        return encodedOfHeader;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public String getExtensionOfOriginalFile() {
        return "." + extensionOfOriginalFile;
    }

}
