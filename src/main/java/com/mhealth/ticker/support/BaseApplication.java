package com.mhealth.ticker.support;

import com.mhealth.ticker.support.rest.DefaultRestService;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by luhonghai on 3/28/17.
 */
public class BaseApplication extends Application {

    private Set<Object> singletons = new HashSet<>();

    public BaseApplication() {
        singletons.add(new DefaultRestService());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}