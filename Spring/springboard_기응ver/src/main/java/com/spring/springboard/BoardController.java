package com.spring.springboard;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	@RequestMapping("mainPage.me")
	public String mainPage(HttpServletRequest request, Model model) throws Exception {

		int pageSize = 10;

		String pageNum = request.getParameter("pageNum");

		if (pageNum == null)
			pageNum = "1";

		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage - 1) * pageSize + 1;
		int endRow = startRow + pageSize - 1;
		int count = 0;
		int number = 0;
		
		ArrayList<BoardVO> articleList = null;	
		count = boardService.getArticleCount();

		// 맨 마지막 페이지의 마지막글을 삭제하여 마지막 페이지가 없어졌을때 필요
		if (count < startRow) {
			currentPage = currentPage - 1;
			startRow = (currentPage - 1) * pageSize + 1;
			endRow = startRow + pageSize - 1;
		}
		if (count > 0)
			articleList = boardService.getArticles(startRow, endRow);

		number = count - (currentPage - 1) * pageSize;

		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startRow", startRow);
		model.addAttribute("endRow", endRow);
		model.addAttribute("count", count);
		model.addAttribute("number", number);
		model.addAttribute("articleList", articleList);
		model.addAttribute("pageSize", pageSize);

		return "list";
	}
	
	@RequestMapping("/writeform.me")
	public String writeForm() throws Exception{
		
		return "writeform";
	}
	
	@RequestMapping("/writeProcess.me")
	public String insertArticle(BoardVO article, HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();

		article.setReg_date(new Timestamp(System.currentTimeMillis()));
		
		int res = boardService.insertArticle(article);
		if (res != 0) {
			writer.write("<script>alert('입력 성공!!');location.href='./mainPage.me';</script>");
		}else {
			writer.write("<script>alert('입력 실패!!');location.href='./mainPage.me';</script>");
		}
		return "redirect:/mainPage.me";
	}
	
	@RequestMapping("/content.me")
	public String selectArticle() {
		
		return "context";
	}
	
	@RequestMapping("/deleteProcess.me")
	public String deleteArticle(int num, String passwd, HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		int res = boardService.deleteArticle(num, passwd);
		
		if (res != 0) {
			writer.write("<script>alert('삭제 성공!!');location.href='./mainPage.me';</script>");
		}else {
			writer.write("<script>alert('삭제 실패!!');location.href='./deleteform.me';</script>");
		}
		
		
		
		return "redirect:/mainPage.me";
	}
	
	@RequestMapping("deleteform.me")
	public String deleteForm() {
		return "deleteform";
	}
	
	@RequestMapping("updateform.me")
	public String updateArticle() {
		
		return "updateform";
	}
	
	@RequestMapping("/updateProcess.me")
	public String updateArticle(BoardVO article, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		int res = boardService.updateArticle(article);
		String pageNum = request.getParameter("pageNum");
		
		if (res != 0) {
			writer.write("<script>alert('삭제 성공!!');location.href='./mainPage.me';</script>");
		}else {
			writer.write("<script>alert('삭제 실패!!');location.href='./updateform.me';</script>");
		}
		
		return "redirect:/mainPage.me";
	}
	
}
