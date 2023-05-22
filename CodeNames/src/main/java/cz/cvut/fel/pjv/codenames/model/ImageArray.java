package cz.cvut.fel.pjv.codenames.model;
import java.util.Arrays;
public class ImageArray {
    private String[] filePaths;
    private int currentIndex;

    /**
     * Constructor
     * @param filePaths paths to the images
     */
    public ImageArray(String... filePaths) {
        this.filePaths = filePaths;
        this.currentIndex = 0;
    }

    /**
     * Returns the next image in the array
     * @return next image
     */
    public String getNext() {
        if (filePaths.length == 0) {
            System.err.println("No images in array");
            return null;
        }

        String filePath = filePaths[currentIndex];
        currentIndex = (currentIndex + 1) % filePaths.length;
        return filePath;
    }

    /**
     * Adds an image to the array
     * @param filePath path to the image
     */
    public void addImage(String filePath) {
        filePaths = Arrays.copyOf(filePaths, filePaths.length + 1);
        filePaths[filePaths.length - 1] = filePath;
    }

    /**
     * Shuffles the array
     */
    public void shuffle() {
        for (int i = 0; i < filePaths.length; i++) {
            int randomIndex = (int) (Math.random() * filePaths.length);
            String temp = filePaths[i];
            filePaths[i] = filePaths[randomIndex];
            filePaths[randomIndex] = temp;
        }
    }
}
