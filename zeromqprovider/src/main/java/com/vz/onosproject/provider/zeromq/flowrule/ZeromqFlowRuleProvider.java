package com.vz.onosproject.provider.zeromq.flowrule;

import com.vz.onosproject.provider.zeromq.api.AbstractZeromqProvider;
import org.onosproject.core.ApplicationId;
import org.onosproject.net.flow.FlowRule;
import org.onosproject.net.flow.FlowRuleBatchOperation;
import org.onosproject.net.flow.FlowRuleProvider;
/**
 * Created by kspviswa-onos-mcord on 2/12/16.
 */
public class ZeromqFlowRuleProvider extends AbstractZeromqProvider
        implements FlowRuleProvider {
    @Override
    public void applyFlowRule(FlowRule... flowRules) {

    }

    @Override
    public void removeFlowRule(FlowRule... flowRules) {

    }

    @Override
    public void removeRulesById(ApplicationId applicationId, FlowRule... flowRules) {

    }

    @Override
    public void executeBatch(FlowRuleBatchOperation flowRuleBatchOperation) {

    }
}
