package Auto_pack;
import java.util.HashMap;

public class ErrorHandler {
	
	@SuppressWarnings("rawtypes")
	private HashMap<Class , String> errors;
	private Class<?> class_;
	
	public ErrorHandler() {
		this.errors = new HashMap<>();
	}

	public HashMap<Class , String> getErrors(){
		return this.errors;
	}
	
	public Class<?> getClass_() {
		return this.class_;
	}
}
