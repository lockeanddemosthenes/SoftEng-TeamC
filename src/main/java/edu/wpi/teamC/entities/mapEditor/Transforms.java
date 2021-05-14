package edu.wpi.teamC.entities.mapEditor;

public class Transforms {


    private int originalImageHeight;
    private int originalImageWidth;
    private double offsetX;
    private double offsetY;
    private double imageWidth;
    private double imageHeight;
    public static Transforms transforms_instance=null;
    private Transforms(int originalImageWidth, int originalImageHeight, double offsetX, double offsetY, double imageWidth, double imageHeight) {
        this.originalImageWidth = originalImageWidth;
        this.originalImageHeight = originalImageHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }
    public static Transforms get_Instance(int originalImageWidth, int originalImageHeight, double offsetX, double offsetY, double imageWidth, double imageHeight){
        if (transforms_instance==null){
            transforms_instance=new Transforms(originalImageWidth,originalImageHeight,offsetX,offsetY,imageWidth,imageHeight);
        }
        return transforms_instance;
    }
    public static Transforms get_Instance(){
        return transforms_instance;
    }

    public int transformX(double location){
        return transform(location,getOriginalImageWidth(),getOffsetX(),getImageWidth());
    }
    public int transformY(double location){
        return transform(location,getOriginalImageHeight(),getOffsetY(),getImageHeight());
    }
    public int transform(double location,double originalLength,double offset, double currentLength){
        return (int)(location*(currentLength/originalLength)+offset);
    }
    public int inverseTransformX(double location){
        return inverseTransform(location,getOriginalImageWidth(),getOffsetX(),getImageWidth());
    }
    public int inverseTransformY(double location){
        return inverseTransform(location,getOriginalImageHeight(),getOffsetY(),getImageHeight());
    }
    public int inverseTransform(double location,double originalLength,double offset, double currentLength){
        return (int)((location-offset)*(originalLength/currentLength));
    }
    public int getOriginalImageHeight() {
        return originalImageHeight;
    }

    public int getOriginalImageWidth() {
        return originalImageWidth;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getImageWidth() {
        return imageWidth;
    }

    public double getImageHeight() {
        return imageHeight;
    }
}
