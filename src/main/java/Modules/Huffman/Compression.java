package Modules.Huffman;

import Modules.Row;
import Modules.hash.HashMap;
import Modules.hash.HashNode;
import Modules.heap.HeapNode;
import Modules.heap.PriorityQueue;
import Modules.io.stream.BitInputStream;
import Modules.io.stream.BitOutputStream;

import java.io.File;
import java.util.ArrayList;

public class Compression {


    static int[] num;


    private int countDis;


    private BitInputStream inputStream;


    private BitOutputStream outputStream;

    private int charsInFile;

    private int charsInHeader;

    private int charsInEncoding;

    private final StringBuilder encodedOfHeader = new StringBuilder();


    private final ArrayList<Row> rows = new ArrayList<Row>();

    private String extension;


    public void compress(File file, File compressedFile) {

        charactersCount(file);
        HuffNode huffmanTree = createHuffTree(num);
        HashMap<Integer, String> huffmanTable = buildTable(huffmanTree);
        printHeaderOnFile(huffmanTree, compressedFile);


        outputStream.flush();
        outputStream.close();
        inputStream.close();

    }



    private void charactersCount(File original) {

        num = new int[256];

        // Initialize an in to original file
        inputStream = new BitInputStream(original);

        int Char;

        while ((Char = inputStream.readBits(8)) != -1) {

            num[Char]++;

            if (num[Char] == 1) {

                countDis++;

            }

            charsInFile++;

        }

        inputStream.reset();

        this.extension = original.getAbsolutePath().substring(original.getAbsolutePath().lastIndexOf('.') );

    }

    private HuffNode createHuffTree(int[] count) {

        PriorityQueue<HuffNode> huffTree = new PriorityQueue<>(countDis);

        for (int i = 0; i < count.length; i++) {

            if (count[i] > 0) {

                int frequency = count[i];
                HeapNode<HuffNode> heapNode = new HeapNode<HuffNode>();
                HuffNode huffNode = new HuffNode(i, frequency, null, null);
                heapNode.setKey(huffNode);
                huffTree.insert(heapNode);
            }

        }

        for (int i = 0; i < countDis - 1; i++) {

            HeapNode<HuffNode> heapNode1 = huffTree.delete();
            HuffNode huffNode1 = heapNode1.getKey();
            HeapNode<HuffNode> heapNode2 = huffTree.delete();
            HuffNode huffNode2 = heapNode2.getKey();

            HeapNode<HuffNode> heapNode3 = new HeapNode<HuffNode>();
            HuffNode huffNode3 = new HuffNode(huffNode1.getFrequency() + huffNode2.getFrequency(), huffNode1, huffNode2);
            heapNode3.setKey(huffNode3);

            huffTree.insert(heapNode3);

        }

        return huffTree.delete().getKey();

    }

    private void traverse(HuffNode root, String huffmanCode, HashMap<Integer, String> table) {

        if (root != null) {

            traverse(root.getLeft(), huffmanCode + "0", table);

            if (root.isLeaf()) {


                table.put(root.getCharacter(), huffmanCode);

            }

            traverse(root.getRight(), huffmanCode + "1", table);

        }

    }

    private HashMap<Integer, String> buildTable(HuffNode root) {

        HashMap<Integer, String> table = new HashMap<>(countDis);

        if (countDis != 1) {

            traverse(root, "", table);

        } else {

            table.put(root.getCharacter(), "0");
        }

        fillArrayList(table);

        return table;

    }


    private void fillArrayList(HashMap<Integer, String> table) {

        HashNode<Integer, String>[] tableTemp = table.getHashTable();

        for (int i = 0; i < table.sizeOfData(); i++) {

            Character character = (char) tableTemp[i].getKey().intValue();
            String code = tableTemp[i].getValue();
            Integer length = code.length();
            Integer frequency = num[tableTemp[i].getKey()];

            Row row = new Row(character, code, length, frequency);

            rows.add(row);

        }

    }



    private void encodingHeader(HuffNode huffmanTree) {

        if (huffmanTree != null) {


            if (huffmanTree.isLeaf()) {

                outputStream.writeBits(1, 1);
                charsInHeader++;
                outputStream.writeBits(8, huffmanTree.getCharacter());
                charsInHeader += 8;
                encodedOfHeader.append('1');
                encodedOfHeader.append((char) huffmanTree.getCharacter());

            } else {

                outputStream.writeBits(1, 0);
                charsInHeader++;
                encodedOfHeader.append('0');

            }

            encodingHeader(huffmanTree.getLeft());

            encodingHeader(huffmanTree.getRight());
        }

    }



    private void printHeaderOnFile(HuffNode huffmanTree, File compreesedFile) {

        outputStream = new BitOutputStream(compreesedFile);

        outputStream.writeBits(32, charsInFile);

        outputStream.writeBits(8, this.extension.length());

        for (int i = 0; i < extension.length(); i++) {

            outputStream.writeBits(8, extension.charAt(i));
        }

        encodingHeader(huffmanTree);



    }


    public int getCharNum() {
        return charsInFile;
    }


    public StringBuilder getEncodedOfHeader() {
        return encodedOfHeader;
    }

    public ArrayList<Row> getData() {
        return rows;
    }

    public String getExtension() {
        return "." + extension;
    }

}
