package online.qiqiang.qim.shellclient;

import online.qiqiang.qim.shellclient.event.LoginEvent;
import online.qiqiang.qim.shellclient.event.LoginSource;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.event.EventListener;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * @author qiqiang
 */
@Component
public class QimShellClientPromptProvider implements PromptProvider {
    private LoginSource loginSource;

    @Override
    public AttributedString getPrompt() {
        if (loginSource != null) {
            return new AttributedString(loginSource.getUserId() + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        } else {
            return new AttributedString("server-unknown:>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
        }
    }

    @EventListener
    public void handle(LoginEvent event) {
        this.loginSource = event.getLoginSource();
    }
}