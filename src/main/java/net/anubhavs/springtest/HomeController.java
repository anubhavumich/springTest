package net.anubhavs.springtest;

import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import Functional.DBOperations;
import net.anubhavs.springtest.model.*;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired private ServletContext servletContext;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	/*
	 * Test Page 
	 * */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Model model){
		String greetings = "Welcome to Spring MVC";
		model.addAttribute("greetings", greetings);
		try{
			String url = servletContext.getRealPath("/resources/DB/stable_db.db");
			DBOperations dbOp = new DBOperations(url);
			dbOp.Connect();
			ArrayList<TTRMarketTypeModel> ttrList = dbOp.GetTTRValues();
			ttrList.size();
		}catch(Exception ex){
			ex.toString();
		}
		
		return "test";
	}
	
	/*
	 * Returns JSON data for ajax based calls
	 * */
	@RequestMapping(value = "/getSearchModes")
	public @ResponseBody SearchModes getSearchModes(){
		SearchModes modes = new SearchModes(19008, 1487, 1598);
		return modes;
	} 
	
	/*
	 * Returns JSON data for ajax based calls
	 * */
	@RequestMapping(value = "/getTTRModes")
	public @ResponseBody ArrayList<TTRMarketTypeVM> getTTRModes(){
		ArrayList<TTRMarketTypeVM> ttrListVM = new ArrayList<TTRMarketTypeVM>();
		try{
			String url = servletContext.getRealPath("/resources/DB/stable_db.db");
			DBOperations dbOp = new DBOperations(url);
			dbOp.Connect();
			ArrayList<TTRMarketTypeModel> ttrList = dbOp.GetTTRValues();
			int listSize = ttrList.size();
			for (int i=0;i<listSize;i++){
				TTRMarketTypeVM model = new TTRMarketTypeVM();
				TTRMarketTypeModel original = ttrList.get(i);
				model.AccountCount = original.Count;
				model.AccountId = original.AccountId;
				model.MarketType = original.MarketType;
				model.MeanMarketTTR = original.TTRSumPerAccount/(float)original.Count;
				model.MinTTR = original.MinTTR;
				model.MaxTTR = original.MaxTTR;
				model.TTRSumPerAccount = original.TTRSumPerAccount;
				ttrListVM.add(model);
			}
		}catch(Exception ex){
			ex.toString();
		}
		return ttrListVM;
	}
	
}
