<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "com.spring.springboard.BoardVO" %>
<%@ page import = "com.spring.springboard.BoardDAO" %>
<%@ page import = "java.util.List" %>
<%@ page import = "java.text.SimpleDateFormat" %>
<%@ include file = "color.jsp" %>

<%
   int pageSize = 10;
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d HH:mm");
   
   String pageNum = request.getParameter("pageNum");
   
   if(pageNum == null)	// 처음엔 null
      pageNum = "1";
   
   int currentPage = Integer.parseInt(pageNum);
   int startRow = (currentPage - 1) * pageSize + 1;
   int endRow = startRow + pageSize - 1;
   int count = 0;
   int number = 0;
   
   List articleList = null;
   BoardDAO dao = new BoardDAO();
   count = dao.getArticleCount();

   // 맨 마지막 페이지의 마지막 글을 삭제하여 마지막 페이지가 없어졌을 때 필요
   if(count < startRow) {
      currentPage = currentPage - 1;	// 페이지수 줄여줌
      startRow = (currentPage - 1) * pageSize + 1;	// 그리고 다시 startRow endRow 계산	/ 그 페이지에서 처음 시작되는 글의 번호
      endRow = startRow + pageSize - 1;	// 그 페이지의 마지막 글 번호
   }
   
   if(count > 0) 
      articleList = dao.getArticles(startRow, endRow);	// null 아니면 arrayList객체가 저장됨
   
   number = count - (currentPage - 1) * pageSize;	// 게시판상 번호와 실제 게시판 번호가 다를 수 있음(실제로 글을 쓰고 지우는 경우가 있기 때문에)
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
</head>
<body bgcolor = "<%=bodyback_c%>">
   <center>
      <b>글 목록</b>
      <table width = "600">
         <tr>
            <td align = "right" bgcolor = "<%=value_c %>">
               <a href = "writeform.bo">글쓰기</a>
            </td>
         </tr>
      </table>
      
<%
   if(count == 0) {
%>
      <table width = "600" border = "1"  cellpadding = "0" cellspacing = "0">
         <tr>
            <td align = "center">게시판에 저장된 글이 없습니다. </td>
         </tr>
      </table>
<% 
   } else {
%>
   <table border = "1" width = "600" cellpadding = "0" cellspacing = "0" align = "center">
      <tr height = "30" bgcolor = "<%=value_c %>">
         <td align = "center" width = "50"> 번 호 </td>
         <td align = "center" width = "250"> 제 목 </td>
         <td align = "center" width = "100"> 작 성 자 </td>
         <td align = "center" width = "150"> 작 성 일 </td>
         <td align = "center" width = "50"> 조 회 </td>
      </tr>
<%
      for(int i = 0; i < articleList.size(); i++) {
         BoardVO article = (BoardVO)articleList.get(i);	// (BoardDataBean) 캐스트 생략가능
%>
      <tr height = "30">
         <td align = "center" width = "50"> <%=number %> </td>
         <td width = "250">
            <% if(article.getRe_level() > 0) {	// getRe_level이 0보다 크면 들여쓰기
                  for(int level = 0; level < article.getRe_level(); level++) {
            %>
                     &nbsp;
            <%
                  }
            %>
               <img src=${pageContext.request.contextPath}/resources/image/re.gif />
            <%}else { %>
                  &nbsp;
            <% } %>
               <a href = "content.bo?num=<%=article.getNum()%>&pageNum=<%=currentPage %>&number=<%=number %>">	<!-- 글에 대한 정보가 필요해서 파라미터들이 필요 / num = rownum / number = 게시판에 뜨는 글넘버-->
                  <%=article.getSubject() %>
               </a>
         <%
            if(article.getReadcount() >= 20) {
         %>
         		<img src=${pageContext.request.contextPath}/resources/image/hot.gif border="0" height="16"/>
         <%
            }
         %>
         </td>
         <td align = "center" width = "100">
            <a href = "mailto:<%=article.getEmail()%>"><%=article.getWriter() %></a>
         </td>
         <td align = "center" width = "150">
            <%=sdf.format(article.getReg_date()) %>
         </td>
         
         <td align = "center" width = "50">
            <%=article.getReadcount() %>
         </td>
      </tr>
<%
      number--;
      }
%>
   </table>
   <br>

<%
   }

   if(count > 0) {
      int pageCount = ((count - 1) / pageSize) + 1;
      int startPage = 1;
      int i;
      
      if(currentPage % 10 != 0)
         startPage = (int)(currentPage/10) * 10 + 1;
      else 
         startPage = currentPage - 9; 
      
      int pageBlock = 10;	// 페이지 1~10 열개 단위로 하고 그 다음은 다음누르면 11~20 이런식
      
      if(startPage > 10) {	// 10보다 크면 이전블럭이 존재한다는 소리
%>
         <a href = "list.jsp?pageNum=<%=startPage -10%>">[이전]</a>
<%
      }
      
      for(i = startPage; (i <= startPage + 9) && (i <= pageCount); i++) {
%>
         <a href = "list.jsp?pageNum=<%=i %>">[<%=i%>]</a>
<%      }
      
      if(i < pageCount) {
%>         
         <a href = "list.jsp?pageNum=<%=startPage+10%>">[다음]</a>
<%
      }
   }
%>
   </center>
</body>
</html>