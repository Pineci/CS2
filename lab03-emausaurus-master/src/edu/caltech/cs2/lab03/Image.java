package edu.caltech.cs2.lab03;

import edu.caltech.cs2.libraries.Pixel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Image {
    private Pixel[][] pixels;

    public Image(File imageFile) throws IOException {
        BufferedImage img = ImageIO.read(imageFile);
        this.pixels = new Pixel[img.getWidth()][img.getHeight()];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                this.pixels[i][j] = Pixel.fromInt(img.getRGB(i, j));
            }
        }
    }

    private Image(Pixel[][] pixels) {
        this.pixels = pixels;
    }

    public Image transpose() {
        Pixel[][] toret = new Pixel[pixels[0].length][pixels.length];
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                toret[j][i] = pixels[i][j];
            }
        }
        return new Image(toret);
    }

    private String bitToChar(int[] bits, String builder){
        int toret = 0;
        for(int i = 0; i < bits.length; i++){
            toret += Math.pow(2, 7-i) * bits[i];
        }
        if(toret != 0){
            builder += (char)toret;
        }
        return builder;
    }

    public String decodeText() {
        int[] bits = new int[8];
        int ctr = 7;
        String toret = "";
        for(int i = 0; i < pixels.length; i++){
            for(int j = 0; j < pixels[i].length; j++){
                if(ctr < 0) {
                    toret = bitToChar(bits, toret);
                    ctr = 7;
                }
                bits[ctr] = pixels[i][j].getLowestBitOfR();
                ctr--;
            }
        }
        toret = bitToChar(bits, toret);
        return toret;
    }

    private int[] decodeChar(char toDec){
        String bin = Integer.toBinaryString((toDec));
        while(bin.length() < 8){
            bin = "0" + bin;
        }
        bin = bin.substring(bin.length() - 8);
        int[] bits = new int[8];
        for(int i = 0; i < 8; i++){
            bits[i] = Integer.parseInt(bin.substring(i, i+1));
        }
        return bits;
    }

    private void setLowestBit(Pixel[][] arr, int bit){
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr[0].length; j++){
                arr[i][j] = arr[i][j].fixLowestBitOfR(bit);
            }
        }
    }

    public Image hideText(String text) {
        int i = 0, j = 0;
        Pixel[][] pixels = Arrays.copyOf(this.pixels, this.pixels.length);
        this.setLowestBit(pixels, 0);
        for(int c = 0; c < text.length(); c++){

            char toDec = text.charAt(c);
            int[] bits = decodeChar(toDec);

            for(int d = 7; d >= 0 ; d--) {
                if (j > pixels[0].length - 1) {
                    j = j % pixels[0].length;
                    i++;
                }
                if (i < pixels.length) {
                    pixels[i][j] = pixels[i][j].fixLowestBitOfR(bits[d]);
                    j++;
                }
            }

        }

        /*for(i = 0; i < 50; i++){
            for(j = 0; j < 50; j++){
                System.out.print(pixels[i][j].r + " ");
            }
            System.out.print("\n");
        }*/

        return new Image(pixels);
    }

    public BufferedImage toBufferedImage() {
        BufferedImage b = new BufferedImage(this.pixels.length, this.pixels[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < this.pixels.length; i++) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                b.setRGB(i, j, this.pixels[i][j].toInt());
            }
        }
        return b;
    }

    public void save(String filename) {
        File out = new File(filename);
        try {
            ImageIO.write(this.toBufferedImage(), filename.substring(filename.lastIndexOf(".") + 1, filename.length()), out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
