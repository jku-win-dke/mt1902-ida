package at.jku.dke.ida.web.controllers;

import at.jku.dke.ida.scxml.exceptions.*;
import at.jku.dke.ida.scxml.session.SessionManager;
import at.jku.dke.ida.shared.Event;
import at.jku.dke.ida.shared.ResourceBundleHelper;
import at.jku.dke.ida.shared.display.ErrorDisplay;
import at.jku.dke.ida.web.models.InputMessage;
import at.jku.dke.ida.web.models.StartDialogMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * This controller defines the websocket endpoints.
 */
@Controller
public class WebsocketController {

    private static final Logger LOGGER = LogManager.getLogger(WebsocketController.class);

    /**
     * Instantiates a new instance of class {@linkplain WebsocketController}.
     */
    @Autowired
    public WebsocketController() {
    }

    /**
     * Starts a new dialogue state machine.
     *
     * @param message        The message.
     * @param headerAccessor The header accessor used to get the session id.
     * @throws StateMachineInstantiationException If the state machine could not be instantiated.
     * @throws SessionExpiredException            If the session does not exist (This should never happen).
     */
    @MessageMapping("/start")
    public void start(@Payload StartDialogMessage message, SimpMessageHeaderAccessor headerAccessor) throws StateMachineInstantiationException, SessionExpiredException {
        LOGGER.info("{} - Start message received", headerAccessor.getSessionId());
        SessionManager.getInstance().createSession(headerAccessor.getSessionId(), message.getLocale());
        SessionManager.getInstance().initiateStateMachine(headerAccessor.getSessionId());
    }

    /**
     * Triggers a user-input event on the state machine of the current session.
     *
     * @param message        The message with the user input.
     * @param headerAccessor The header accessor used to get the session id.
     * @throws StateMachineExecutionException If an error occurred while executing a state machine.
     * @throws SessionExpiredException        If the state chart session does not exist or is already expired (or state chart has finished).
     * @see Event#USER_INPUT
     */
    @MessageMapping("/input")
    public void userInput(@Payload InputMessage message, SimpMessageHeaderAccessor headerAccessor) throws StateMachineExecutionException, SessionExpiredException {
        LOGGER.info("{} - Input message received", headerAccessor.getSessionId());
        SessionManager.getInstance().triggerUserInput(headerAccessor.getSessionId(), message.getUserInput());
    }

    /**
     * Triggers a revise query event on the state machine of the current session.
     *
     * @param headerAccessor The header accessor used to get the session id.
     * @throws StateMachineExecutionException If an error occurred while executing a state machine.
     * @throws SessionExpiredException        If the session does not exist (This should never happen).
     */
    @MessageMapping("/reviseQuery")
    public void reviseQuery(SimpMessageHeaderAccessor headerAccessor) throws StateMachineExecutionException, SessionExpiredException {
        LOGGER.info("{} - Revise query message received", headerAccessor.getSessionId());
        SessionManager.getInstance().triggerReviseQuery(headerAccessor.getSessionId());
    }

    // region --- Exception Handlers ---

    /**
     * Handles {@link StateMachineInstantiationException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(StateMachineInstantiationException ex) {
        LOGGER.error("An error occurred while instantiating a state machine.", ex);
        return new ErrorDisplay(ResourceBundleHelper.getResourceString("web.ErrorMessages", "ExecuteStateMachine") + ex.getLocalizedMessage());
    }

    /**
     * Handles {@link StateMachineExecutionException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(StateMachineExecutionException ex) {
        LOGGER.error("An error occurred while executing a state machine.", ex);
        return new ErrorDisplay(ResourceBundleHelper.getResourceString("web.ErrorMessages", "InitiateStateMachine") + ex.getLocalizedMessage());
    }

    /**
     * Handles {@link SessionExpiredException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(SessionExpiredException ex) {
        LOGGER.error("The state chart session does not exist or is already expired (or state chart has finished).", ex);
        return new ErrorDisplay(ResourceBundleHelper.getResourceString("web.ErrorMessages", "SessionExpired") + ex.getLocalizedMessage());
    }

    /**
     * Handles {@link IllegalArgumentException}s.
     *
     * @param ex The exception.
     * @return The error display with an error message.
     */
    @MessageExceptionHandler
    public ErrorDisplay handleException(IllegalArgumentException ex) {
        LOGGER.fatal("Illegal Argument Exception caught. This should not have happened.", ex);
        return new ErrorDisplay(ResourceBundleHelper.getResourceString("web.ErrorMessages", "ExecuteStateMachine") + ex.getLocalizedMessage());
    }
    // endregion
}
