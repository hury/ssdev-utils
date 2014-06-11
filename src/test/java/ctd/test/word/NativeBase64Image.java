package ctd.test.word;

import javax.activation.MimetypesFileTypeMap;

public class NativeBase64Image {
	private static final MimetypesFileTypeMap mimeTypeMap =  new MimetypesFileTypeMap();
	private String id;
	private String filename;
	private String base64;
	
	static{
		mimeTypeMap.addMimeTypes("image/png png");
	}
	
	public String getContentType(){
		return mimeTypeMap.getContentType(filename);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}
	
}
