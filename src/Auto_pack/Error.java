package Auto_pack;

public class Error extends Throwable {
	
	private static final long serialVersionUID = 5346558218248980828L;
	private String text;
	private Class<?> class_;
	
	Error(String text , Class<?> class_){
		this.text = text;
		this.class_ = class_;
	}
	
	public String getText() {
		return this.text;
	}
	
	public Class<?> getClass_() {
		return this.class_;
	}
}
