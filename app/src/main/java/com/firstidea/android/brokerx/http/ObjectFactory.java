package com.firstidea.android.brokerx.http;

import com.firstidea.android.brokerx.BrokerxApplication;
import com.firstidea.android.brokerx.http.service.AnalysisService;
import com.firstidea.android.brokerx.http.service.LeadService;
import com.firstidea.android.brokerx.http.service.UserService;

/**
 * Created by Govind on 20-Nov-16.
 */

public class ObjectFactory {
    private static ObjectFactory mObjectFactory;
    private UserService userService;
    private LeadService leadService;
    private AnalysisService analysisService;

    public static synchronized ObjectFactory getInstance() {
        if (null == mObjectFactory) {
            synchronized (ObjectFactory.class) {
                // Double checked singleton lazy initialization.
                mObjectFactory = new ObjectFactory();
            }
        }
        return mObjectFactory;
    }


    public synchronized UserService getUserServiceInstance() {
        if (null == this.userService) {
            this.userService = SingletonRestClient.createService(UserService.class, BrokerxApplication.getContext());
        }

        return this.userService;
    }

    public synchronized LeadService getLeadServiceInstance() {
        if (null == this.leadService) {
            this.leadService = SingletonRestClient.createService(LeadService.class, BrokerxApplication.getContext());
        }

        return this.leadService;
    }


    public synchronized AnalysisService getAnalysisServiceInstance() {
        if (null == this.analysisService) {
            this.analysisService = SingletonRestClient.createService(AnalysisService.class, BrokerxApplication.getContext());
        }

        return this.analysisService;
    }
}
