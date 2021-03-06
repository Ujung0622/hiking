package project.admin.e_p003.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import project.admin.e_p003.vo.E_p003VO;



public interface E_p003Controller {
	
	public ModelAndView addCSpost(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView viewDetaList(int csPostNum, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView upDateCsBoard(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String removeCsboard(int csPostNum,HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String sendMail(Map<String, String> map,HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String mainPageMail(Map<String, String> map,HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView searchCsBoard( String searchOption, String key_word,  HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String countBoard(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String countBoard300(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
