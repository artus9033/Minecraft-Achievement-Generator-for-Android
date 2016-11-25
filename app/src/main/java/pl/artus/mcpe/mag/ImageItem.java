package pl.artus.mcpe.mag;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;
	private String desc;
	private int res;
	
    public ImageItem(Bitmap image, String title, String desc, int res) {
        super();
        this.image = image;
        this.title = title;
		this.desc = desc;
		this.res = res;
    }

    public Bitmap getImage() {
        return image;
    }
	
	public int getResource(){
		return res;
	}

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
