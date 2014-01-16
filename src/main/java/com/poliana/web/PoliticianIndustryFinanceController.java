package com.poliana.web;

import com.poliana.core.politicianFinance.industries.PoliticianIndustryFinanceService;
import com.poliana.core.politicianFinance.industries.IndustryPoliticianContributionTotals;
import com.poliana.views.PoliticianContributionView;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import static com.poliana.core.time.TimeService.CURRENT_CONGRESS;

/**
 * @author David Gilmore
 * @date 1/6/14
 */
@Controller
@RequestMapping("/politicians/")
public class PoliticianIndustryFinanceController extends AbstractBaseController {

    private PoliticianIndustryFinanceService politicianIndustryFinanceService;

    private static final Logger logger = Logger.getLogger(PoliticianIndustryFinanceController.class);


    /**
     * Get all industry contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries", method = RequestMethod.GET)
    public @ResponseBody String getIndustryToPoliticianTotals (
            @PathVariable("bioguide_id") String bioguideId) {

        List<IndustryPoliticianContributionTotals> allTotals = politicianIndustryFinanceService.getIndustryToPoliticianTotals(bioguideId);
        return this.gson.toJson(allTotals);
    }

    /**
     * Get all industry category contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries/categories", method = RequestMethod.GET)
    public @ResponseBody String getIndustryCategoryToPoliticianTotals (
            @PathVariable("bioguide_id") String bioguideId) {

        List<IndustryPoliticianContributionTotals> allTotals =
                politicianIndustryFinanceService.getIndustryCategoryToPoliticianTotals(bioguideId);

        return this.gson.toJson(allTotals);
    }

    /**
     * Get all industry contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @param congress
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries", params = {"congress"}, method = RequestMethod.GET)
    public @ResponseBody String getIndustryToPoliticianTotals (
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "congress", required = false, defaultValue = CURRENT_CONGRESS) Integer congress) {

        List<IndustryPoliticianContributionTotals> allTotals = politicianIndustryFinanceService.getIndustryToPoliticianTotals(bioguideId, congress);
        return this.gson.toJson(allTotals);
    }

    /**
     * Get all industry category contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @param congress
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries/categories", params = {"congress"}, method = RequestMethod.GET)
    public @ResponseBody String getIndustryCategoryToPoliticianTotals (
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "congress", required = false, defaultValue = CURRENT_CONGRESS) Integer congress) {

        List<IndustryPoliticianContributionTotals> allTotals =
                politicianIndustryFinanceService.getIndustryCategoryToPoliticianTotals(bioguideId, congress);

        return this.gson.toJson(allTotals);
    }

    /**
     * Get industry contribution sums to a given politician over a given time range
     * @param bioguideId
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/{bioguide_id}/contributions/industries", params = {"start", "end"}, method = RequestMethod.GET)
    public @ResponseBody String getIndustryToPoliticianTotals(
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date start,
            @RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date end) {

        List<IndustryPoliticianContributionTotals> totals =
                politicianIndustryFinanceService.getIndustryToPoliticianTotals(bioguideId, start.getTime()/1000, end.getTime()/1000);

        return this.gson.toJson(totals);
    }

    /**
     * Get industry category contribution sums to a given politician over a given time range
     * @param bioguideId
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/{bioguide_id}/contributions/industries/categories", params = {"start", "end"}, method = RequestMethod.GET)
    public @ResponseBody String getIndustryCategoryToPoliticianTotals(
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date start,
            @RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date end) {

        List<IndustryPoliticianContributionTotals> totals =
                politicianIndustryFinanceService.getIndustryCategoryToPoliticianTotals(bioguideId, start.getTime() / 1000, end.getTime() / 1000);

        return this.gson.toJson(totals);
    }

    /**
     * Plot all industry contribution totals for all time
     * @param bioguideId
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries", params = {"plot"}, method = RequestMethod.GET)
    public void plotIndustryToPoliticianTotals (
            OutputStream stream,
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "plot", required = true) String plotType) {

        List<IndustryPoliticianContributionTotals> allTotals = politicianIndustryFinanceService.getIndustryToPoliticianTotals(bioguideId);

        PoliticianContributionView view = new PoliticianContributionView(allTotals);

        JFreeChart chart = view.generateChart(plotType);

        try {
            ChartUtilities.writeChartAsPNG(stream, chart, 1600, 1000);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Plot all industry category contribution totals for all time
     * @param bioguideId
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries/categories", params = {"plot"}, method = RequestMethod.GET)
    public void plotIndustryCategoryToPoliticianTotals (
            OutputStream stream,
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "plot", required = true) String plotType) {

        List<IndustryPoliticianContributionTotals> allTotals =
                politicianIndustryFinanceService.getIndustryCategoryToPoliticianTotals(bioguideId);

        PoliticianContributionView view = new PoliticianContributionView(allTotals);

        JFreeChart chart = view.generateChart(plotType);

        try {
            ChartUtilities.writeChartAsPNG(stream, chart, 1600, 1000);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Plot all industry contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @param congress
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries", params = {"congress", "plot"}, method = RequestMethod.GET)
    public void plotIndustryToPoliticianTotals (
            OutputStream stream,
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "congress", required = false, defaultValue = CURRENT_CONGRESS) Integer congress,
            @RequestParam(value = "plot", required = true) String plotType) {

        List<IndustryPoliticianContributionTotals> allTotals = politicianIndustryFinanceService.getIndustryToPoliticianTotals(bioguideId, congress);

        PoliticianContributionView view = new PoliticianContributionView(allTotals, congress);

        JFreeChart chart = view.generateChart(plotType);

        try {
            ChartUtilities.writeChartAsPNG(stream, chart, 1600, 1000);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Plot all industry category contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @param congress
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries/categories", params = {"congress", "plot"}, method = RequestMethod.GET)
    public void plotIndustryCategoryToPoliticianTotals (
            OutputStream stream,
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "congress", required = false, defaultValue = CURRENT_CONGRESS) Integer congress,
            @RequestParam(value = "plot", required = true) String plotType) {

        List<IndustryPoliticianContributionTotals> allTotals = politicianIndustryFinanceService.getIndustryCategoryToPoliticianTotals(bioguideId, congress);

        PoliticianContributionView view = new PoliticianContributionView(allTotals, congress);

        JFreeChart chart = view.generateChart(plotType);

        try {
            ChartUtilities.writeChartAsPNG(stream, chart, 1600, 1000);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Plot all industry contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param stream
     * @param bioguideId
     * @param start
     * @param end
     * @param plotType
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries", params = {"start", "end", "plot"}, method = RequestMethod.GET)
    public void plotIndustryToPoliticianTotals (
            OutputStream stream,
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date start,
            @RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date end,
            @RequestParam(value = "plot", required = true) String plotType) {

        List<IndustryPoliticianContributionTotals> allTotals =
                politicianIndustryFinanceService.getIndustryToPoliticianTotals(bioguideId, start.getTime()/1000, end.getTime()/1000);

        PoliticianContributionView view = new PoliticianContributionView(allTotals, start, end);

        JFreeChart chart = view.generateChart(plotType);

        try {
            ChartUtilities.writeChartAsPNG(stream, chart, 1600, 1000);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /**
     * Plot all industry category contribution totals for a given congressional cycle. The default congress cycle value is
     * the current congress.
     * @param bioguideId
     * @return
     */
    @RequestMapping(value="/{bioguide_id}/contributions/industries/categories", params = {"start", "end", "plot"}, method = RequestMethod.GET)
    public void plotIndustryCategoryToPoliticianTotals (
            OutputStream stream,
            @PathVariable("bioguide_id") String bioguideId,
            @RequestParam(value = "start", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date start,
            @RequestParam(value = "end", required = true) @DateTimeFormat(pattern = "MM-dd-yyyy") Date end,
            @RequestParam(value = "plot", required = true) String plotType) {

        List<IndustryPoliticianContributionTotals> allTotals =
                politicianIndustryFinanceService.getIndustryCategoryToPoliticianTotals(bioguideId, start.getTime()/1000, end.getTime()/1000);

        PoliticianContributionView view = new PoliticianContributionView(allTotals, start, end);

        JFreeChart chart = view.generateChart(plotType);

        try {
            ChartUtilities.writeChartAsPNG(stream, chart, 1600, 1000);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Autowired
    public void setPoliticianIndustryFinanceService(PoliticianIndustryFinanceService politicianIndustryFinanceService) {
        this.politicianIndustryFinanceService = politicianIndustryFinanceService;
    }
}
