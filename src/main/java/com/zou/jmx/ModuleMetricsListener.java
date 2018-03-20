package com.zou.jmx;

import com.zou.event.AbstractInvokeEventBus;

import javax.management.AttributeChangeNotification;
import javax.management.JMException;
import javax.management.Notification;
import javax.management.NotificationListener;

public class ModuleMetricsListener implements NotificationListener {


    @Override
    public void handleNotification(Notification notification, Object handback) {

        if (!(notification instanceof AttributeChangeNotification)) {
            return;
        }

        AttributeChangeNotification attributeChangeNotification = (AttributeChangeNotification) notification;
        AbstractInvokeEventBus.ModuleEvent event = Enum.valueOf(AbstractInvokeEventBus.ModuleEvent.class, attributeChangeNotification.getAttributeType());

        ModuleMetricsVisitor visitor = ModuleMetricsHandler.getInstance().visit(attributeChangeNotification.getMessage(), attributeChangeNotification.getAttributeName());


        switch (event) {

            case INVOKE_EVENT:
                visitor.setInvokeSuccessCount(((Long) attributeChangeNotification.getNewValue()).longValue());
                break;
            case INVOKE_SUCCESS_EVENT:
                visitor.setInvokeSuccessCount(((Long) attributeChangeNotification.getNewValue()).longValue());
                break;
            case INVOKE_FAIL_EVENT:
                visitor.setInvokeFailCount(((Long) attributeChangeNotification.getNewValue()).longValue());
                break;
            case INVOKE_FILTER_EVENT:
                visitor.setInvokeFilterCount(((Long) attributeChangeNotification.getNewValue()).longValue());
                break;
            case INVOKE_TIMESPAN_EVENT:
                visitor.setInvokeTimeSpan(((Long) attributeChangeNotification.getNewValue()).longValue());
                visitor.getHistogram().record(((Long) attributeChangeNotification.getNewValue()).longValue());
                break;
            case INVOKE_MAX_TIMESPAN_EVENT:
                if ((Long) attributeChangeNotification.getNewValue() > (Long) attributeChangeNotification.getOldValue()) {
                    visitor.setInvokeMaxTimespan(((Long) attributeChangeNotification.getNewValue()).longValue());
                }
                break;
            case INVOKE_MIN_TIMESPAN_EVENT:
                if ((Long) attributeChangeNotification.getNewValue() < (Long) attributeChangeNotification.getOldValue()) {
                    visitor.setInvokeMinTimesan(((Long) attributeChangeNotification.getNewValue()).longValue());
                }
                break;
            case INVOKE_FAIL_STACKTRACE_EVENT:
                try {
                    visitor.setLastStackTrace((Exception) attributeChangeNotification.getNewValue());
                    visitor.buildErrorCompositeData((Exception) attributeChangeNotification.getNewValue());
                } catch (JMException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;

        }


    }
}











































































