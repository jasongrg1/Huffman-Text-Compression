/**
 * The type Huffman node.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    private int frequency;
    private char character;
    private HuffmanNode x, y;

    /**
     * Constructor for instantiating a HuffmanNode
     *
     * @param frequency frequency of the character
     * @param character character of the node
     * @param x         x node
     * @param y         y node
     */
    public HuffmanNode(int frequency, char character, HuffmanNode x, HuffmanNode y) {
        this.frequency = frequency;
        this.character = character;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets x.
     *
     * @param x the x
     */
    public void setX(HuffmanNode x) {
        this.x = x;
    }

    /**
     * Sets y.
     *
     * @param y the y
     */
    public void setY(HuffmanNode y) {
        this.y = y;
    }

    /**
     * Gets frequency.
     *
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;

    }

    /**
     * Gets character.
     *
     * @return the character
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public HuffmanNode getX() {
        return x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public HuffmanNode getY() {
        return y;
    }

    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}