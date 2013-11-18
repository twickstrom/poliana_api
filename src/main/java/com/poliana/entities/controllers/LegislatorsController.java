package com.poliana.entities.controllers;

import com.poliana.entities.jobs.LegislatorJobs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.stereotype.Controller;

/**
 * @author David Gilmore
 * @date 11/17/13
 */
@Controller
public class LegislatorsController implements CommandMarker {

    @Autowired
    private LegislatorJobs legislatorJobs;

    @CliCommand(value = "populateLegislators")
    public void populateLegislators() {
        legislatorJobs.loadLegislatorsToMongo();
    }
}
