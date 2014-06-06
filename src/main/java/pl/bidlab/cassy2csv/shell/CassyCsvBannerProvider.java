package pl.bidlab.cassy2csv.shell;

import org.springframework.shell.plugin.BannerProvider;
import org.springframework.stereotype.Component;

@Component
public class CassyCsvBannerProvider implements BannerProvider {
	public static final String BANNER =                                                                                                                                                                                                                                                                                                            
"\n ██████╗ █████╗ ███████╗███████╗██╗   ██╗██████╗  ██████╗███████╗██╗   ██╗\n" +
"██╔════╝██╔══██╗██╔════╝██╔════╝╚██╗ ██╔╝╚════██╗██╔════╝██╔════╝██║   ██║\n" +
"██║     ███████║███████╗███████╗ ╚████╔╝  █████╔╝██║     ███████╗██║   ██║\n" +
"██║     ██╔══██║╚════██║╚════██║  ╚██╔╝  ██╔═══╝ ██║     ╚════██║╚██╗ ██╔╝\n" +
"╚██████╗██║  ██║███████║███████║   ██║   ███████╗╚██████╗███████║ ╚████╔╝ \n" +
" ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚══════╝  ╚═══╝  \n";
                                                                                                                                                                                                                                                                                                 
	public String name() {
		return "Cassy2CSV by Bartosz Bogacki";
	}

	public String getBanner() {
		return BANNER;
	}

	public String getVersion() {
		return "1.0";
	}

	public String getWelcomeMessage() {
		return "Welcome to Cassy2CSV. Have a nice exporting!\n\nUse \"help\" command to see your options.\n";
	}


}
