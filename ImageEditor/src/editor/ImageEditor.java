package editor;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class ImageEditor {
    private static int height;
    private static int width;

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File(args[0]));
        FileWriter out = new FileWriter(new File(args[1]));
        String word;

        word = in.next(); // "P3"
        out.write(word + " ");

        word = in.next(); // width
        out.write(word + " ");
        width = Integer.parseInt(word);

        word = in.next(); // height
        out.write(word + " ");
        height = Integer.parseInt(word);

        word = in.next(); // "255"
        out.write(word + " ");

        int red;
        int green;
        int blue;
        Pixel[][] newPixelImage = new Pixel[height][width];
        for (int i = 0; i < height; i++) {
            Pixel[] newPixelRow = new Pixel[width];
            for (int j = 0; j < width; j++) {
                word = in.next();
                red = Integer.parseInt(word);

                word = in.next();
                green = Integer.parseInt(word);

                word = in.next();
                blue = Integer.parseInt(word);

                Pixel newPixel = new Pixel(red, green, blue);
                newPixelRow[j] = newPixel;
            }
            newPixelImage[i] = newPixelRow;
        }

        Pixel[][] finalPixelImage = new Pixel[height][width];
        if (args[2].equals("invert")) finalPixelImage = invert(newPixelImage);
        else if (args[2].equals("grayscale")) finalPixelImage = grayscale(newPixelImage);
        else if (args[2].equals("emboss")) finalPixelImage = emboss(newPixelImage);
        else if (args[2].equals("motionblur")) finalPixelImage = motionBlur(newPixelImage, args[3]);

        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                finalString.append(finalPixelImage[i][j].toString() + " ");
            }
        }
        out.write(String.valueOf(finalString));
        out.close();
    }

    public static Pixel[][] invert(Pixel[][] newPixelImage) {
        Pixel[][] finalPixelImage = new Pixel[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                finalPixelImage[i][j] = newPixelImage[i][j].invert();
            }
        }

        return finalPixelImage;
    }

    public static Pixel[][] grayscale(Pixel[][] newPixelImage) {
        Pixel[][] finalPixelImage = new Pixel[height][width];

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                finalPixelImage[i][j] = newPixelImage[i][j].grayscale();
            }
        }

        return finalPixelImage;
    }

    public static Pixel[][] emboss(Pixel[][] newPixelImage) {
        Pixel[][] finalPixelImage = new Pixel[height][width];

        for (int i = 0; i < width; i++){
            finalPixelImage[0][i] = newPixelImage[0][i].emboss();
        }
        for (int i = 1; i < height; i++){
            finalPixelImage[i][0] = newPixelImage[i][0].emboss();
        }

        for (int i = 1; i < height; i++){
            for (int j = 1; j < width; j++){
                finalPixelImage[i][j] = newPixelImage[i][j].emboss(newPixelImage[i-1][j-1]);
            }
        }

        return finalPixelImage;
    }

    public static Pixel[][] motionBlur(Pixel[][] newPixelImage, String blurStr) {
        Pixel[][] finalPixelImage = new Pixel[height][width];
        int blurNum = Integer.parseInt(blurStr);
        int newBlurNum;

        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                if ((j + blurNum) > width) newBlurNum = (width - j);
                else newBlurNum = blurNum;
                Pixel[] pixelBlurArray = new Pixel[newBlurNum];
                for (int k = 0; k < newBlurNum; k++) {
                    pixelBlurArray[k] = newPixelImage[i][j+k];
                }
                finalPixelImage[i][j] = Pixel.motionBlur(pixelBlurArray);
            }
        }

        return finalPixelImage;
    }
}
