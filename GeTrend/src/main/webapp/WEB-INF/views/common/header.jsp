<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>	

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" 
integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<link rel="stylesheet" href='<c:url value="/resources/css/header.css"/>'> 
<title>Header</title>
  
</head>	

<body>
<div class="header_container">
	<div class="header_row">
	  	<div class="header_logo">
	      <a href="<c:url value='/'/>" >
	    	<input type="image" class="header_img" src="/getrend/resources/img/headerlogo.png"> 	
	      </a>
	    </div>
	    <div class="header_search">
	    	<div class="search_border">  				
	  	 		<input type="text" id="searchInput" name="searchInput" placeholder="검색어를 입력하세요">
		 		<input type="image" id="searchInput_img" src="/getrend/resources/img/research.png">
	    	</div> 	
	    </div>
	    <div class="header_menu">
			<c:choose>
				<c:when test="${sessionScope.loginemail != null}">
					<a href="<c:url value="/mypage/mypageSession"/>">
			    		MYPAGE
		   		    </a>
			    	<a href="<c:url value="/users/logout"/>">
			        	LOGOUT
			        </a>
		               <a href="<c:url value='/users/userUpdate'/>">
		               	<img class="header_profile" src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/1200px-Circle-icons-profile.svg.png" >
			        </a>
			    </c:when>
				<c:otherwise>
			        <a href="<c:url value="/users/userJoin"/>">
			        	JOIN
			        </a>
			        <a href="<c:url value="/users/userLogin"/>">
			        	LOGIN
			        </a>
				</c:otherwise>
			</c:choose>
		  </div>
	</div>	 	  	
</div>	
    	 
	
	
<script type="text/javascript">
	$(function() {

		function highlightText(text, $node) {
			var searchText = $.trim(text).toLowerCase(), currentNode = $node.get(0).firstChild, matchIndex, newTextNode, newSpanNode;
			while ((matchIndex = currentNode.data.toLowerCase().indexOf(searchText)) >= 0) {
				newTextNode = currentNode.splitText(matchIndex);
				currentNode = newTextNode.splitText(searchText.length);
				newSpanNode = document.createElement("span");
				newSpanNode.className = "highlight";
				currentNode.parentNode.insertBefore(newSpanNode, currentNode);
				newSpanNode.appendChild(newTextNode);
			}
		}

		$("#searchInput").autocomplete({
	           source : function( request, response ) {
	               $.ajax({
	                   type: 'get',
	                   url: "<c:url value='/autocomplete/source'/>",
	                   dataType: "json",
	                   data: {"param" : $("#searchInput").val()},
	                   success: function(data) {
	                       response(
	                           $.map(data, function(item) {    
	                               return {
	                                   label: item,   
	                                   value: item,    
	                                   test : item+"test"   
	                               }
	                           })
	                       );
	                   }          
	              });
	           }, 
	           
	       focus : function(event, ui) {   
	           return false;
	       },
	       minLength: 1,
	       autoFocus: false
//	       delay: 500
	       }).data("ui-autocomplete")._renderItem = function(ul, item) {
			var $a = $("<b></b>").text(item.value);
			highlightText(this.term, $a);
			return $("<li></li>").append($a).appendTo(ul);
		};

		$("#searchInput").keydown(function (key) {    	 
	        if(key.keyCode == 13){
	        	event.preventDefault();
	        	$("#searchBar").submit();

	        };
	    });
	});
</script>
</body>
</html>
