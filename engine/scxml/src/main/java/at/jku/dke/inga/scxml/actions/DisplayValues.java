package at.jku.dke.inga.scxml.actions;

import at.jku.dke.inga.data.repositories.AggregateMeasureRepository;
import at.jku.dke.inga.data.repositories.CubeRepository;
import at.jku.dke.inga.data.repositories.GranularityLevelRepository;
import at.jku.dke.inga.rules.models.ValueDisplayServiceModel;
import at.jku.dke.inga.rules.services.ValueDisplayService;
import at.jku.dke.inga.scxml.interceptors.DisplayValuesInterceptor;
import at.jku.dke.inga.scxml.session.SessionContextModel;
import at.jku.dke.inga.shared.display.Display;
import at.jku.dke.inga.shared.spring.BeanUtil;
import org.apache.commons.scxml2.ActionExecutionContext;
import org.apache.commons.scxml2.model.ModelException;

/**
 * This action identifies values from which the user can select one.
 * Values can be cubes, measures, ...
 */
public class DisplayValues extends BaseAction {
    /**
     * Executes the action operations.
     *
     * @param ctx      The state chart application execution context.
     * @param ctxModel The context data.
     */
    @Override
    protected void execute(ActionExecutionContext ctx, SessionContextModel ctxModel) throws ModelException {
        // Get data
        var model = new ValueDisplayServiceModel(
                getCurrentState(),
                ctxModel.getAnalysisSituation(),
                ctxModel.getLocale(),
                ctxModel.getOperation(),
                BeanUtil.getBean(CubeRepository.class),
                BeanUtil.getBean(AggregateMeasureRepository.class),
                BeanUtil.getBean(GranularityLevelRepository.class)
        );
        var interceptor = BeanUtil.getOptionalBean(DisplayValuesInterceptor.class);
        if (interceptor != null)
            model = interceptor.modifyModel(model);

        // Determine display data
        Display display = new ValueDisplayService().executeRules(model);
        if (interceptor != null)
            display = interceptor.modifyResult(display);

        // Send to display
        ctxModel.setDisplayData(display);
    }
}