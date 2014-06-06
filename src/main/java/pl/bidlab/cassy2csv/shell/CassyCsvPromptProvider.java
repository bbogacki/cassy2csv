package pl.bidlab.cassy2csv.shell;

import org.springframework.shell.plugin.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CassyCsvPromptProvider implements PromptProvider {

	public String name() {
		return null;
	}

	public String getPrompt() {
		return "cassy2csv> ";
	}

}
