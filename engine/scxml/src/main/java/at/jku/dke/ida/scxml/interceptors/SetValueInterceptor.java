package at.jku.dke.ida.scxml.interceptors;

import at.jku.dke.ida.rules.interfaces.SetValueServiceModel;

/**
 * Interceptor for modifying the model and the result of the {@link at.jku.dke.ida.scxml.actions.DetermineValue} action.
 */
public interface SetValueInterceptor extends Interceptor<SetValueServiceModel, Void, Void> {
    @Override
    default Void modifyResult(SetValueServiceModel model, Void result) {
        return null;
    }
}
