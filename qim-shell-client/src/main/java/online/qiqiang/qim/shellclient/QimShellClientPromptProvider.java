package online.qiqiang.qim.shellclient;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * @author qiqiang
 */
@Component
public class QimShellClientPromptProvider implements PromptProvider {
    @Autowired
    private LoginContext loginContext;

    @Override
    public AttributedString getPrompt() {
        if (loginContext.isLogin()) {
            return new AttributedString(loginContext.getUserId() + "@online" + ":>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        } else {
            return new AttributedString("guest@outline:>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.RED));
        }
    }
}