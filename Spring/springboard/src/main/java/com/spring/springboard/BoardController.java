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

	// 게시글 리스트
	@RequestMapping("/list.bo")
	public String list(HttpServletRequest request, Model model) throws Exception {

		return "list";

	}

	// 게시글 작성폼 이동
	@RequestMapping("/writeform.bo")
	public String writeForm() throws Exception {
		return "writeForm";
	}

	// 게시글 작성프로세스
	@RequestMapping("/writeprocess.bo")
	public String writeProcess(BoardVO boardVO) throws Exception {
		boardService.insertArticle(boardVO);

		return "list";
	}

	// 게시글 내용 들어가기
	@RequestMapping("/content.bo")
	public String content() throws Exception {

		return "content";
	}

	// 게시글 수정 폼 이동
	@RequestMapping("/updateform.bo")
	public String updateForm() throws Exception {
		return "updateForm";
	}

	// 게시글 수정프로세스
	@RequestMapping("/updateprocess.bo")
	public String updateProcess(BoardVO article, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		int res = boardService.updateArticle(article);
		String pageNum = request.getParameter("pageNum");
		
		if (res != 0) {
			writer.write("<script>alert('수정 성공!!');location.href='./list.bo';</script>");
		}else {
			writer.write("<script>alert('수정 실패!!');location.href='./list.bo';</script>");
		}
		return null;
	}
	

	// 게시글 삭제 폼 이동
	@RequestMapping("/deleteform.bo")
	public String deleteForm() throws Exception {
		return "deleteForm";
	}

	// 게시글 삭제프로세스
	@RequestMapping("/deleteprocess.bo")
	public String deleteProcess(int num, String passwd, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();

		int res = boardService.deleteArticle(num, passwd);
		if (res != 0) {
			writer.write("<script>alert('삭제 성공!!');location.href='./list.bo';</script>");
		} else {
			writer.write("<script>alert('삭제 실패!!');location.href='./list.bo';</script>");
		}
		return null;
	}

}
