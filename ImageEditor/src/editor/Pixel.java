package editor;

import static java.lang.Math.abs;

public class Pixel {
    private int redValue;
    private int greenValue;
    private int blueValue;
    public Pixel(int newRed, int newGreen, int newBlue){
        redValue = newRed;
        greenValue = newGreen;
        blueValue = newBlue;
    }

    @Override
    public String toString() {
        return String.valueOf(redValue) + " " + String.valueOf(greenValue) + " " + String.valueOf(blueValue);
    }

    public Pixel invert() {
        Pixel newPixel = new Pixel(255-redValue, 255-greenValue, 255-blueValue);
        return newPixel;
    }

    public Pixel grayscale() {
        int average = (redValue + greenValue + blueValue)/3;
        Pixel newPixel = new Pixel(average, average, average);
        return newPixel;
    }

    public Pixel emboss() {
        Pixel newPixel = new Pixel(128,128,128);
        return newPixel;
    }

    public Pixel emboss(Pixel cornerPixel) {
        int redDiff = redValue - cornerPixel.redValue;
        int greenDiff = greenValue - cornerPixel.greenValue;
        int blueDiff = blueValue - cornerPixel.blueValue;
        int maxDiff = redDiff;
        if (abs(greenDiff) > abs(maxDiff)) maxDiff = greenDiff;
        if (abs(blueDiff) > abs(maxDiff)) maxDiff = blueDiff;
        int v = maxDiff + 128;
        if (v < 0) v = 0;
        if (v > 255) v = 255;
        Pixel newPixel = new Pixel(v, v, v);
        return newPixel;
    }

    public static Pixel motionBlur(Pixel[] pixelBlurArray) {
        int redAverage = 0;
        int greenAverage = 0;
        int blueAverage = 0;
        for (int i = 0; i < pixelBlurArray.length; i++) {
            redAverage += pixelBlurArray[i].redValue;
            greenAverage += pixelBlurArray[i].greenValue;
            blueAverage += pixelBlurArray[i].blueValue;
        }
        redAverage /= pixelBlurArray.length;
        greenAverage /= pixelBlurArray.length;
        blueAverage /= pixelBlurArray.length;
        Pixel newPixel = new Pixel(redAverage, greenAverage, blueAverage);
        return newPixel;
    }
}
