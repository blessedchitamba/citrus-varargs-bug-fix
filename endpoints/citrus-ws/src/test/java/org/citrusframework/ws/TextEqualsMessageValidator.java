package org.citrusframework.ws;

import org.citrusframework.context.TestContext;
import org.citrusframework.message.Message;
import org.citrusframework.message.MessageType;
import org.citrusframework.validation.DefaultMessageValidator;
import org.citrusframework.validation.context.ValidationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Basic message validator performs String equals on received message payloads. We add this validator in order to have a
 * matching message validation strategy for integration tests in this module.
 * @author Christoph Deppisch
 */
public class TextEqualsMessageValidator extends DefaultMessageValidator {

    private static final Logger logger = LoggerFactory.getLogger(TextEqualsMessageValidator.class);

    @Override
    public void validateMessage(Message receivedMessage, Message controlMessage, TestContext context, ValidationContext validationContext) {
        if (controlMessage.getPayload(String.class).isBlank()) {
            logger.info("Skip text validation as no control message payload specified");
            return;
        }

        Assert.assertEquals(receivedMessage.getPayload(String.class), controlMessage.getPayload(String.class), "Validation failed - " +
                "expected message contents not equal!");

        logger.info("Text validation successful: All values OK");
    }

    @Override
    public boolean supportsMessageType(String messageType, Message message) {
        return messageType.equalsIgnoreCase(MessageType.PLAINTEXT.toString());
    }
}
