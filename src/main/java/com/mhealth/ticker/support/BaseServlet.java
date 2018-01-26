package com.mhealth.ticker.support;

import com.mhealth.ticker.support.data.Config;
import com.mhealth.ticker.support.data.Version;
import com.mhealth.ticker.support.job.BaseJob;
import com.mhealth.ticker.support.service.SlackHookService;
import org.apache.http.util.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/3/16.
 */
public class BaseServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(BaseServlet.class.getName());


    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("Register all job");
        Version version = Version.current();
        if (!Config.getInstance().isDebug()) {
            try {
                if (Config.getInstance().getSlack() != null && !TextUtils.isEmpty(Config.getInstance().getSlack().getHookHealthCheck())) {
                    SlackHookService.getInstance().api().hook(Config.getInstance().getSlack().getHookHealthCheck(),
                            new SlackHookService.HookBody(version.getName() + " " + version.getVersion() + " is started")).execute();
                }
            } catch (Exception e) {
            }
        }
        try {
            for (Class<? extends BaseJob> jobClazz : BaseJob.REGISTERED_JOBS) {
                BaseJob job = jobClazz.newInstance();
                job.register();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not register all job" ,e);
        }
    }
}
