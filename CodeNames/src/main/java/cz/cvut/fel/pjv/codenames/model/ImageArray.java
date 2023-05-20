package cz.cvut.fel.pjv.codenames.model;
import java.util.Arrays;
public class ImageArray {
    private String[] filePaths;
    private int currentIndex;

    public ImageArray(String... filePaths) {
        this.filePaths = filePaths;
        this.currentIndex = 0;
    }

    public String getNext() {
        if (filePaths.length == 0) {
            System.err.println("No images in array");
            return null;
        }

        String filePath = filePaths[currentIndex];
        currentIndex = (currentIndex + 1) % filePaths.length;
        return filePath;
    }

    public void addImage(String filePath) {
        filePaths = Arrays.copyOf(filePaths, filePaths.length + 1);
        filePaths[filePaths.length - 1] = filePath;
    }

    public void reset() {
        currentIndex = 0;
    }

    public void shuffle() {
        for (int i = 0; i < filePaths.length; i++) {
            int randomIndex = (int) (Math.random() * filePaths.length);
            String temp = filePaths[i];
            filePaths[i] = filePaths[randomIndex];
            filePaths[randomIndex] = temp;
        }
    }
}
