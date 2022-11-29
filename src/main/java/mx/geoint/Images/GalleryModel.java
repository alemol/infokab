package mx.geoint.Images;

public class GalleryModel {
    public int lastIndex;
    public int totalImages;

    public String[] imageList;

    public int getLastIndex() {return lastIndex;}
    public void setLastIndex(int lastIndex){this.lastIndex = lastIndex;}

    public int getTotalImages() {return totalImages;}
    public void setTotalImages(int totalImages){this.totalImages = totalImages;}

    public String[] getimageList(){return imageList;}
    public void setimageList(String[] imageList) { this.imageList = imageList; }
}
