import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.*;
import java.nio.file.*;
import java.io.FileWriter;


/**
 * The type Huffman.
 */
public class Huffman {

    private static HashMap<Character, String> codeHashMap = new HashMap<>();

    private static HashMap<Character, Integer> frequencyHashMap = new HashMap<>();

    private static HuffmanNode root;

    /**
     * turns string into bit array.
     *
     * @param s the string
     * @return the bit array.
     */
    static byte[] getBinary(String s) {
        StringBuilder sBuilder = new StringBuilder(s);
        while (sBuilder.length() % 8 != 0) {
            sBuilder.append('0');
        }
        s = sBuilder.toString();

        byte[] data = new byte[s.length() / 8];

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            }
        }
        return data;
    }

    /**
     * turns bit array to string.
     *
     * @param bytes the bit array
     * @return the string
     */
    static String getString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }


    /**
     * Read the lines in the file and append to a string.
     *
     * @param path the file path
     * @return string with file content
     */
//read file
    public static String fileReader(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }

    /**
     * File size long.
     *
     * @param filePath the file path
     * @return the file size
     */
    public static long fileSize(String filePath) {
        Path path = Paths.get(filePath);
        long bytes = 0;

        try {

            // size of a file (in bytes)
            bytes = Files.size(path);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return bytes;
    }

    /**
     * Calculates size reduction after compression.
     *
     * @param originalSize   the original size
     * @param compressedSize the compressed size
     * @return Percentage decrease in file size
     */
    public static double percentageDecrease(double originalSize, double compressedSize) {
        double percentage;
        percentage = (originalSize - compressedSize) / originalSize * 100;
        return percentage;
    }


    /**
     * Make huffman tree huffman node.
     *
     * @param frequencyHashMap the frequency hash map
     * @return the huffman node
     */
//make tree
    public static HuffmanNode makeHuffmanTree(HashMap<Character, Integer> frequencyHashMap) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyHashMap.entrySet()) {
            HuffmanNode huffmanNode = new HuffmanNode(entry.getValue(), entry.getKey(), null, null);
            queue.offer(huffmanNode);   // insert node into queue.

        }
        while (queue.size() > 1) {
            HuffmanNode x = queue.poll();


            HuffmanNode y = queue.poll();


            HuffmanNode z = new HuffmanNode(x.getFrequency() + y.getFrequency(), '-', x, y);
            root = z;

            queue.offer(z);


        }
        return queue.poll();

    }

    /**
     * Assign code.
     *
     * @param root the root of Huffman tree
     * @param code empty string which will be appended by traversing through tree.
     */
//assign code
    public static void assignCode(HuffmanNode root, String code) {
        if (root != null) {
            if (root.getX() == null && root.getY() == null) {
                codeHashMap.put(root.getCharacter(), code);

            }
            assignCode(root.getX(), code + "0");
            assignCode(root.getY(), code + "1");

        }

    }


    /**
     * Sets up the encoding for the compression.
     *
     * @param text the text for which the encoding is going to be generated for
     */
    public static void setup(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (!frequencyHashMap.containsKey(text.charAt(i))) {
                frequencyHashMap.put(text.charAt(i), 1);
            } else {
                frequencyHashMap.put(text.charAt(i), frequencyHashMap.get(text.charAt(i)) + 1);
            }
        }

        System.out.println("Frequency Map = " + frequencyHashMap);
        // makes Huffman tree using the frequency hashmap.
        root = makeHuffmanTree(frequencyHashMap);

        assignCode(root, "");
        System.out.println("Code Map = " + codeHashMap);
    }

    /**
     * Compress.
     *
     * @param encodingPath the file path of the file for which the encoding is going to be generated for
     * @param convertPath  the file path of the file which will be compressed
     */
//convert to huffman using frequency hashmap
    public static void compress(String encodingPath, String convertPath) {
        if (encodingPath.equals("")) {
            encodingPath = convertPath;
        }
        if (convertPath.equals("")) {
            System.out.println("No file to be compressed.");
        } else {

            // Timing the compression.
            long startTime = System.nanoTime();
            String encodingText = fileReader(encodingPath);
            setup(encodingText);
            String convertText = fileReader(convertPath);
            StringBuilder convertedString = new StringBuilder();

            // encoding each character using the code hashmap.
            for (int i = 0; i < convertText.length(); i++) {
                char c = convertText.charAt(i);
                convertedString.append(codeHashMap.get(c));
            }

            long fileSize = fileSize(convertPath);
            // output the file size of original file.
            System.out.printf("File size before compression = %,d bytes%n", fileSize);


            String binaryString = convertedString.toString();
            int characterCount = binaryString.length();

            byte[] converted = getBinary(binaryString);
            // Below is the indicator string.
            byte[] indicator = getBinary("00000000000000000000000000000000000000000000000000000001");

            //name of the outputted file
            String compressedPath = "";
            boolean var = convertPath.endsWith(".txt");
            if (var) {
                compressedPath += convertPath.substring(0, convertPath.length() - 4) + ".bin";
            } else {
                compressedPath = convertPath + ".bin";
            }

            // Save bit array to file
            try {
                OutputStream outputStream = new FileOutputStream(compressedPath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                // Writing the frequency
                objectOutputStream.writeObject(frequencyHashMap);

                // Writing the character count.
                objectOutputStream.writeObject(characterCount);

                // Writing the indicator to the compressed file.
                // The indicator separates necessary objects to decompress the file and the encoded content.
                outputStream.write(indicator);
                outputStream.write(converted);

                objectOutputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            long timeTaken = endTime - startTime;
            // Output compression time in ms.
            System.out.println("Compressing time = " + timeTaken / 1000000 + "ms");

            long compressedFileSize = fileSize(compressedPath);
            System.out.printf("File size after compression = %,d bytes%n", compressedFileSize);
            System.out.println("Percentage size reduction = " + percentageDecrease(fileSize, compressedFileSize) + "%");
        }
    }


    /**
     * Decode the encoded string.
     *
     * @param string the encoded string.
     * @return the decoded string.
     */

    public static String decode(String string) {
        StringBuilder decodedString = new StringBuilder();
        // make temp equal to Huffman tree root.
        HuffmanNode temp = root;

        for (int i = 0; i < string.length(); i++) {
            // Get value at position i.
            int j = Integer.parseInt(String.valueOf(string.charAt(i)));

            // if value = 0, temp is equal to the x node.
            if (j == 0) {
                temp = temp.getX();
                // if we get to a node with no children node then the character in the node is appended to the string.
                if (temp.getX() == null && temp.getY() == null) {
                    decodedString.append(temp.getCharacter());
                    // make temp equal to the root again.
                    temp = root;
                }

            }
            // if value = 1, temp is equal to the y node.
            if (j == 1) {
                temp = temp.getY();
                if (temp.getX() == null && temp.getY() == null) {
                    decodedString.append(temp.getCharacter());
                    temp = root;
                }
            }
        }
        return decodedString.toString();

    }

    /**
     * Decompress file.
     *
     * @param decompressPath the file path of the file that is decompressed.
     */

    @SuppressWarnings("unchecked")
    public static void decompress(String decompressPath) {
        if (decompressPath.equals("")) {
            System.out.println("No file to be decompressed.");
        } else {
            try {
                // Load bit array from file
                byte[] allBytes = Files.readAllBytes(Paths.get(decompressPath));
                // turns bit array to a string.
                String fileString = getString(allBytes);
                String fileContent = fileString.substring(fileString.indexOf("00000000000000000000000000000000000000000000000000000001") + 56);


                FileInputStream inputStream = new FileInputStream(decompressPath);
                // Creating an object input stream
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                HashMap<Character, Integer> hashMap = (HashMap<Character, Integer>) objectInputStream.readObject();
                int characterCount2 = (int) objectInputStream.readObject();


                inputStream.close();
                objectInputStream.close();
                //using character count from file to not include the extra 0s in the string for the content of the file.
                String onlyFileContent = fileContent.substring(0, characterCount2);



                // make root equal to huffman tree made from hashmap
                root = makeHuffmanTree(hashMap);
                String newFileName = decompressPath.substring(0, decompressPath.length() - 4) + " decompressed.txt";
                File file = new File(newFileName);
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                    FileWriter writer = new FileWriter(newFileName);
                    // write the decoded content to get the decompressed file.
                    writer.write(decode(onlyFileContent));
                    writer.close();
                    System.out.println("Successfully created decompressed file");
                } else {
                    System.out.println("Compression of this file already exists.");
                }
            } catch (IOException |
                    ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // encodingPath is the file that the encoding will be generated for.
        // convertPath is file that will be compressed using the encoding of the encodingPath file.
        String encodingPath = "Hamlet.txt";
        String convertPath = "";
        compress(encodingPath, convertPath);

        // decompressPath is the file that will be decompressed.
        String decompressPath = "Hamlet.bin";
        decompress(decompressPath);

    }
}



