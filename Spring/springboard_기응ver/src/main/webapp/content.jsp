<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="color.jsp" %>	
<%@ page import = "com.spring.springboard.BoardVO" %>
<%@ page import = "com.spring.springboard.BoardDAO" %>
<%@ page import = "java.text.SimpleDateFormat" %>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<% int num = Integer.parseInt(request.getParameter("num"));
	String pageNum = request.getParameter("pageNum");
	int number = Integer.parseInt(request.getParameter("number"));

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	try{
		BoardDAO dbPro = BoardDAO.getInstance();
		
		BoardVO article = dbPro.getArticle(num);

		//4가지 정보가 필요 
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
%>
<body bgcolor="<%=bodyback_c %>">
		<b> 글 내용 보기 </b>
		<form>
			<table width="555" border="1" cellspacing="0" cellpadding="0" bgcolor="<%=bodyback_c %>" align="center">
				<tr height="30" aling="center" width="125">
					<td bgcolor="<%=value_c %>">글 번호</td>
					<td><%=number %></td>
					<td bgcolor="<%=value_c %>">조회수</td>
					<td width="180">
					<%=article.getReadcount() %>
					</td>
				</tr>
				<tr height="30" align="center" width="125">
					<td bgcolor="<%=value_c %>">작성자</td>
					<td><%=article.getWriter() %></td>
					<td bgcolor="<%=value_c %>">작성일</td>
					<td width="180">
					<%=sdf.format(article.getReg_date()) %>
					</td>
				</tr>
				<tr height="30">
					<td align="center" width="125" bgcolor="<%=value_c %>">글제목</td>
					<td align="left" colspan="3">
					&nbsp;
					<%=article.getSubject() %>
					</td>
				</tr>
				<tr>
					<td align="center" width="125" bgcolor="<%=value_c %>">글내용</td>
					<td align="left" colspan="3">
						<pre>
						<%=article.getContent() %>
						</pre>
					</td>
				</tr>
				<tr height="30">
					<td colspan="4" bgcolor="<%=value_c %>" align="center">
						<input type="button" value="글수정" onclick=
							"document.location.href='updateform.me?num=<%=article.getNum() %>&pageNum=<%=pageNum %>'">&nbsp;&nbsp;		
						<input type="button" value="글삭제" onclick=
							"document.location.href='deleteform.me?num=<%=article.getNum() %>&pageNum=<%=pageNum %>'">&nbsp;&nbsp;	
						<input type="button" value="답글쓰기" onclick=
							"document.location.href='writeform.me?num=<%=num %>&ref=<%=ref %>&re_step=<%=re_step%>&re_level=<%=re_level%>'">&nbsp;&nbsp;	
						<input type="button" value="글목록" onclick="document.location.href='mainPage.me?pageNum=<%=pageNum %>'">
					</td>			
				</tr>				
			</table>
		</form>
	</body>
<%} catch(Exception ex){} %>	
</html>