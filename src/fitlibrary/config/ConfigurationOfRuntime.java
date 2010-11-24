package fitlibrary.config;

public class ConfigurationOfRuntime implements Configuration {
	private boolean keepUniCode = false;

	@Override
	public boolean keepingUniCode() {
		return keepUniCode ;
	}
	public void keepUnicode(boolean keep) {
		keepUniCode = keep;
	}
}
