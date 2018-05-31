package com.mhealth.ticker.support.rest;

import com.mhealth.ticker.support.data.Version;
import com.mhealth.ticker.support.report.AttendanceReport;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 3/28/17.
 */
@Path("/")
public class DefaultRestService {

    private static final Logger logger = Logger.getLogger(DefaultRestService.class.getName());

    @Path("/version")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response version() {
        return Response.ok(Version.current()).build();
    }

    @Path("/report")
    @GET
    @Produces("text/csv")
    public Response report(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("full") Boolean isFull,
                           @QueryParam("notify") Boolean isNotify) {
        if (isFull == null) isFull = false;
        if (isNotify == null) isNotify = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AttendanceReport attendanceReport = new AttendanceReport();
        Date fromDate = null;
        Date toDate = null;
        try {
            if (to != null && to.length() > 0)
                toDate = sdf.parse(to);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not parse to date", e);
        }
        try {
            if (from != null && from.length() > 0)
                fromDate = sdf.parse(from);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not parse from date", e);
        }
        Calendar fromCal = Calendar.getInstance();
        Calendar toCal = Calendar.getInstance();
        if (toDate == null) {
            toDate = new Date(System.currentTimeMillis());
        }
        toCal.setTime(toDate);
        toCal.set(Calendar.HOUR, 23);
        toCal.set(Calendar.MINUTE, 59);
        toCal.set(Calendar.SECOND, 59);
        if (fromDate == null) {
            fromDate = new Date(System.currentTimeMillis());
            fromCal.setTime(fromDate);
            fromCal.set(Calendar.DAY_OF_MONTH, 0);
        } else {
            fromCal.setTime(fromDate);
        }
        fromCal.set(Calendar.HOUR, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        try {
            return Response.ok(attendanceReport.collect(fromCal.getTime(), toCal.getTime(), !isFull, isNotify)).build();
        } catch (Exception e) {
            return Response.ok("error: " + e.getMessage()).build();
        }
    }
}
