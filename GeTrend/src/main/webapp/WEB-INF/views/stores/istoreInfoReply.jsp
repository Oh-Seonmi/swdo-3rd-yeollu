<!-- 
/**
 * @File 	: istoreInfoReply.jsp
 * @Project : GeTrend
 * @Author	: 조은채, 오선미
 * @Since	: 2020. 3. 26.
 * @Version	: 1.0
*/
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>

<!-- Header Start -->
<head>
<meta charset="UTF-8">
<title>[ ${store_name} | GeTrend ]</title>
<!-- jQuery and Bootstrap -->
<script src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js'></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<link href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css'>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js'></script>
<!-- Fontawesome -->
<link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css'>
<!-- Reply CSS -->
<link rel="stylesheet" href='<c:url value="/resources/css/reply.css"/>'>
</head>
<!-- Header End -->

<!-- Body Start -->
<body>
	<header>
		<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	</header>
	
	<div id="wrap">
		<section id="banner">
			<form action="<c:url value='/reply/replyWrite'/>" method="post" onsubmit="return check()">
		    	<input type="hidden" id="store_no" name="store_no" value="${store_no}" />
				<h3>${sessionScope.loginname}님, <span style="color: #FF8A00;">${store_name}</span>에 댓글을 남겨주세요</h3>
				<div class="starRev">
					<input type='hidden' name='reply_star' id='reply_star' value="0">
					<span class="starR1">0.5</span>
					<span class="starR2">1</span>
					<span class="starR1">1.5</span>
					<span class="starR2">2</span>
					<span class="starR1">2.5</span>
					<span class="starR2">3</span>
					<span class="starR1">3.5</span>
					<span class="starR2">4</span>
					<span class="starR1">4.5</span>
					<span class="starR2">5</span>
				</div>
				<br>
				<div class="text_wrap">
					<textarea id="reply_contents" name="reply_contents" maxlength="10000" placeholder="좋아하는 가게를 친구들과 공유해 보세요"
						style="overflow: hidden; overflow-wrap: break-word; height: 150px; width : 700px; border:none; "></textarea>						
				</div>
				<div id="err">
				</div>
				<div class="btn_wrap">
					<input type="submit" id="writeBtn"  class="btn btn-outline-warning" value="등록">
					<a href="javascript:history.back();"><input type="button" class="btn btn-outline-secondary" value="돌아가기"></a>
				</div>
			</form>
		</section>
	</div>

<!-- Script Start -->
<script>
	function check(){
		var reply_contents = $("#reply_contents").val();
		var reply_star = $("#reply_star").val();
		if(reply_star == 0){
			$("#err").text("별점이 입력되지 않았습니다.");
			$("#err").css("color","black");
			return false;	
			}
		else if(reply_contents.length == 0){
			$("#err").text("글이 입력되지 않았습니다.");
			$("#err").css("color","black");
			return false;		
		}else{
			$("#err").text("");	
		}
	}
	$(function(){
		$('.starRev span').on("click",function(){
			console.log($(this));
			$(this).parent().children('span').removeClass('on');
			$(this).addClass('on').prevAll('span').addClass('on');
			console.log($(this).text());
			var re = $(this).text();
			$(".starRev").children('input').remove();
			$(".starRev").append("<input type='hidden' name='reply_star' id='reply_star' value="+ re + ">");
			return false;
		});
	});
</script>
<!-- Script End -->
</body>
<!-- Body End -->
</html>