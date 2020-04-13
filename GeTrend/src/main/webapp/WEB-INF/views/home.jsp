<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<!-- 반응형 웹 -->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>[ Home | GeTrend ]</title>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"
integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
  crossorigin="anonymous"></script>
<script type="text/javascript" src='//dapi.kakao.com/v2/maps/sdk.js?appkey=<spring:eval expression="@kakao['KAKAOMAP_APPKEY']" />&libraries=drawing,services'></script>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>	


<link rel="stylesheet" href='<c:url value="/resources/css/home.css"/>'> 
<link rel="stylesheet" href='<c:url value="/resources/css/loading.css"/>'>

<style type="text/css">

</style>
</head>
<body>
	<header>
		<jsp:include page="/WEB-INF/views/common/header.jsp"></jsp:include>
	</header>
	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-1">
			</div>
			<div class="col-md-6">
				<div class="row">
					<!-- map 시작 -->
					<div id="map">
						<div class="btn-modal">
							<!-- 지도 위의 버튼 1 음식 카테고리 -->
							<!-- Button trigger modal -->
							<button type="button" id="category" class="btn btn-dark" data-toggle="modal" data-target="#categoryModal">카테고리</button>
							
							<!-- CategoryModal -->
							<div class="modal fade" id="categoryModal">
								<div class="modal-dialog">
									<div class="modal-content">
										<!-- 음식 카테고리 모달 header -->
										<div class="modal-header">
											<h4 class="modal-title">음식 카테고리</h4>
	           								<button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<!-- 음식 카테고리 모달 body -->
	      								<div class="modal-body">
	      									<h3>음식 종류</h3>
	           								<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="한식" checked="checked">한식
				 								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="양식" checked="checked">양식
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="일식/수산물" checked="checked">일식/수산물
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="중식" checked="checked">중식
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="카페/디저트" checked="checked">카페/디저트
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="치킨/피자/패스트푸드" checked="checked">치킨/피자/패스트푸드
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="categotyChk" value="기타" checked="checked">기타
				  								</label>
											</div>
	      								</div>
	      								<!-- 음식 카테고리 모달 footer -->
	       								<div class="modal-footer">
	       									<button type="button" class="btn btn-dark" data-dismiss="modal">확인</button>
	        								<button type="button" class="btn btn-dark" data-dismiss="modal">취소</button>
	       								</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="btn-modal">
							<!-- 지도 위의 버튼 2 음식 카테고리 -->
							<!-- Button trigger modal -->
							<button type="button" id="opentime" class="btn btn-warning" data-toggle="modal" data-target="#opentimeModal">영업 확인</button>
							
							<!-- CategoryModal -->
							<div class="modal fade" id="opentimeModal">
								<div class="modal-dialog">
									<div class="modal-content">
										<!-- 영업 확인 모달 header -->
										<div class="modal-header">
											<h4 class="modal-title">영업 확인</h4>
	           								<button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<!-- 영업 확인 모달 body -->
	      								<div class="modal-body">
	      									<h3>식당 영업 요일별 확인</h3>
	           								<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="일" checked="checked">일
				 								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="월" checked="checked">월
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="화" checked="checked">화
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="수" checked="checked">수
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="목" checked="checked">목
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="금" checked="checked">금
				  								</label>
											</div>
											<div class="form-check">
				  								<label class="form-check-label">
				    								<input type="checkbox" class="form-check-input" name="opentimeChk" value="토" checked="checked">토
				  								</label>
											</div>
	      								</div>
	      								<!-- 영업 확인 모달 footer -->
	       								<div class="modal-footer">
	       									<button type="button" class="btn btn-dark" data-dismiss="modal">확인</button>
	        								<button type="button" class="btn btn-dark" data-dismiss="modal">취소</button>
	       								</div>
									</div>
								</div>
							</div>
						</div>
						
    	 				<button type="button" class="btn btn-dark" id="selectOverlay" onclick="selectOverlay('POLYGON')" value="on">
    	 				범위 선택</button>
    					<button type="button" class="btn btn-warning" id="drawingMap"  onclick="getDataFromDrawingMap()">	
    					조회 하기</button>
					</div>
					<!-- map 종료 -->
				</div>
				
				<div class="row">
					<!-- 추천 스토어 시작 -->
					<div id="carouselRecommendedStores" class="carousel slide" data-ride="carousel">
						<ol class="carousel-indicators">
					    	<li data-target="#carouselRecommendedStores" data-slide-to="0" class="active"></li>
					    	<li data-target="#carouselRecommendedStores" data-slide-to="1"></li>
					    	<li data-target="#carouselRecommendedStores" data-slide-to="2"></li>
					  	</ol>
					  	<div class="carousel-inner">
					    	<div class="carousel-item active">
					      		<img src="https://res.cloudinary.com/dw5oh4ebf/image/upload/v1586776397/brooke-lark-pGM4sjt_BdQ-unsplash_yiapqg.jpg" class="d-block img-fluid" alt="...">
				      			<div class="carousel-caption d-none d-md-block">
					        		<h5>First slide label</h5>
					        		<p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
					      		</div>
					    	</div>
				    		<div class="carousel-item">
					      		<img src="https://res.cloudinary.com/dw5oh4ebf/image/upload/v1586776563/dan-gold-4_jhDO54BYg-unsplash_qcxerf.jpg" class="d-block img-fluid" alt="...">
					      		<div class="carousel-caption d-none d-md-block">
					        		<h5>Second slide label</h5>
					        		<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</p>
					      		</div>
					    	</div>
					    	<div class="carousel-item">
					      		<img src="https://res.cloudinary.com/dw5oh4ebf/image/upload/v1586776555/ariel-kwon-OvqJ4LaLo20-unsplash_ebjrxe.jpg" class="d-block img-fluid" alt="...">
					      		<div class="carousel-caption d-none d-md-block">
					        		<h5>Third slide label</h5>
					        		<p>Praesent commodo cursus magna, vel scelerisque nisl consectetur.</p>
					      		</div>
					  		</div>
					  	</div>
					  	<a class="carousel-control-prev" href="#carouselRecommendedStores" role="button" data-slide="prev">
					  		<span class="carousel-control-prev-icon" aria-hidden="true"></span>
					  		<span class="sr-only">Previous</span>
					  	</a>
					  	<a class="carousel-control-next" href="#carouselRecommendedStores" role="button" data-slide="next">
					    	<span class="carousel-control-next-icon" aria-hidden="true"></span>
					    	<span class="sr-only">Next</span>
					  	</a>
					</div>
					<!-- 추천 스토어 종료 -->
				</div>
			</div>
			<div class="col-md-4">
				<div class="row">
					<div class="istore-container scrollbar scrollbar-warning">
						<c:choose>
							<c:when test="${sessionScope.loginemail != null && sessionScope.istores != null}">
								<c:forEach var="istore" items="${istores}">
									<div id="carouselSearchedStores_${istore.instaStore.store_no}" class="carousel slide" data-ride="carousel">
										<ol class="carousel-indicators">
											<c:forEach var="item" items="${istore.instaImage.postImgList}" varStatus="status">
												<c:choose>
													<c:when test="${status.first}">
														<li data-target="#carouselSearchedStores_${istore.instaStore.store_no}" data-slide-to="${status.index}" class="active" ></li>
													</c:when>
													<c:otherwise>
														<li data-target="#carouselSearchedStores_${istore.instaStore.store_no}" data-slide-to="${status.index}" ></li>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</ol>
										<div class="carousel-inner">
											<c:forEach var="item" items="${istore.instaImage.postImgList}" varStatus="status">
												<c:choose>
													<c:when test="${status.first}">
														<div class="carousel-item active">
												      		<img src="${item.imgUrl}" alt="${istore.instaStore.store_no}" class="d-block img-fluid" >
											      			<div class="carousel-caption d-none d-md-block">
												        		<h5>${istore.instaStore.store_name}</h5>
												        		<p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
												      		</div>
												    	</div>
													</c:when>
													<c:otherwise>
														<div class="carousel-item">
												      		<img src="${item.imgUrl}" alt="${istore.instaStore.store_no}" class="d-block img-fluid" >
											      			<div class="carousel-caption d-none d-md-block">
												        		<h5>${istore.instaStore.store_name}</h5>
												        		<p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>
												      		</div>
												    	</div>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</div>
										<a class="carousel-control-prev" href="#carouselSearchedStores_${istore.instaStore.store_no}" role="button" data-slide="prev">
									  		<span class="carousel-control-prev-icon" aria-hidden="true"></span>
									  		<span class="sr-only">Previous</span>
									  	</a>
									  	<a class="carousel-control-next" href="#carouselSearchedStores_${istore.instaStore.store_no}" role="button" data-slide="next">
									    	<span class="carousel-control-next-icon" aria-hidden="true"></span>
									    	<span class="sr-only">Next</span>
									  	</a>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
							
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<div class="col-md-1">
			</div>
		</div>
	</div>

	
	

	<footer>
		<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
	</footer>

	<!-- 로딩이미지 -->
	<div class="wrap-loading display-none">
	  	<div>
	  		<img src="https://res.cloudinary.com/dw5oh4ebf/image/upload/v1585986827/loader_sxmz3a.gif" />
	  	</div>
	</div>
	

	
<script type="text/javascript">
		$(function() {
			
			function init() {
				if(navigator.geolocation) {
					navigator.geolocation.getCurrentPosition(function(position) {
						const geocoder = new kakao.maps.services.Geocoder();
						const callback = function(result, status) {
							if(status === kakao.maps.services.Status.OK) {
								let adr = result[0].address.address_name;
								/* $.ajax({
									url: "<c:url value='/recommend' />",
									type: "get",
									contentType: "application/json; charset=utf-8",
									data: {
										"adr": adr
									},
									dataType: "json",
									success: function(result) {
										console.log(result);
									},
									error: function(request, status, error){
							            alert("code : " + request.status + "\n" + "message : " + request.responseText + "\n" + "error : " + error);
							        }
								}); */
							}
						}
						geocoder.coord2Address(position.coords.longitude, position.coords.latitude, callback);
						
					});
				}
			}
			function search(points) {
				let categoryValues = [];
				$("input[name=categotyChk]:checked").each(function() {
					categoryValues.push($(this).val());
				});
				console.log(categoryValues);
				let opentimeValues = [];
				$("input[name=opentimeChk]:checked").each(function() {
					opentimeValues.push($(this).val());
				});
				console.log(opentimeValues);
				let reqParm = {
					"points": points,
					"categoryValues": categoryValues,
					"opentimeValues": opentimeValues
				};
				$.ajax({
					url: "<c:url value='/search' />",
					type: "post",
					contentType: "application/json; charset=utf-8",
					data: JSON.stringify(reqParm),
					dataType: "json",
					beforeSend: function() {
						$('.wrap-loading').removeClass('display-none');
						$('.wrap-loading').on('scroll touchmove mousewheel', function(event) {
							event.preventDefault();
						  	event.stopPropagation();
						  	return false;
						});
					},
					complete: function() {
						$('.wrap-loading').addClass('display-none');
						$('.wrap-loading').off('scroll touchmove mousewheel');
					},
					success: function(result) {
						alert("성공");
						printMarker(result);

						$(".istore-container").empty();

						let str = "";
						$(result).each(function(index, item) {
							str += '<div id="carouselSearchedStores_' + item.instaStore.store_no + '" class="carousel slide" data-ride="carousel">';
							str += 		'<ol class="carousel-indicators">';
							for(let i = 0; i < item.instaImage.postImgList.length; i++) {
								if(i === 0) {
									str += '<li data-target="#carouselSearchedStores_' + item.instaStore.store_no + '" data-slide-to="' + i + '" class="active" ></li>';
								} else {
									str += '<li data-target="#carouselSearchedStores_' + item.instaStore.store_no + '" data-slide-to="' + i + '" ></li>';
								}
							}
							str += 		'</ol>';
							str += 		'<div class="carousel-inner">';
							for(let i = 0; i < item.instaImage.postImgList.length; i++) {
								if(i === 0) {
									str += '<div class="carousel-item active">';
									str +=		'<img src="' + item.instaImage.postImgList[i].imgUrl + '" alt="' + item.instaStore.store_no + '" class="d-block img-fluid" >';
									str +=		'<div class="carousel-caption d-none d-md-block">';
									str +=			'<h5>' + item.instaStore.store_name + '</h5>';
									str +=			'<p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>';
									str +=		'</div>';
						      		str += '</div>';
								} else {
									str += '<div class="carousel-item">';
									str +=		'<img src="' + item.instaImage.postImgList[i].imgUrl + '" alt="' + item.instaStore.store_no + '" class="d-block img-fluid" >';
									str +=		'<div class="carousel-caption d-none d-md-block">';
									str +=			'<h5>' + item.instaStore.store_name + '</h5>';
									str +=			'<p>Nulla vitae elit libero, a pharetra augue mollis interdum.</p>';
									str +=		'</div>';
						      		str += '</div>';
								}
							}
							str += 		'</div>';
							str += 		'<a class="carousel-control-prev" href="#carouselSearchedStores_' + item.instaStore.store_no + '" role="button" data-slide="prev">';
							str +=			'<span class="carousel-control-prev-icon" aria-hidden="true"></span>';
							str +=			'<span class="sr-only">Previous</span>';
							str +=		'</a>';
							str +=		'<a class="carousel-control-next" href="#carouselSearchedStores_' + item.instaStore.store_no + '" role="button" data-slide="next">';
							str +=			'<span class="carousel-control-next-icon" aria-hidden="true"></span>';
							str +=			'<span class="sr-only">Next</span>';
							str +=		'</a>';
							str += '</div>';
						});
						console.log(str);
						$(".istore-container").append(str);
			        },
			        error: function(request, status, error){
			            alert("code : " + request.status + "\n" + "message : " + request.responseText + "\n" + "error : " + error);
			        }
				});
			}
			searchFunc = search;
			init();
		});
		function search(points) {
			searchFunc(points);
		};
		
		const mapContainer = document.getElementById("map");
		mapOption = {
				center: new kakao.maps.LatLng(35.15113, 126.924584),
				level: 1 // 지도의 확대 레벨
		};
		const map = new kakao.maps.Map(mapContainer, mapOption);
		const printMarker = (result) => {
			
			// 마커를 표시할 위치와 title 객체 배열입니다
			let positions = [];
			
			for(let n in result) {
				const posotion = {
					title: result[n].instaStore.store_name,
					latlng: new kakao.maps.LatLng(result[n].instaStore.store_y, result[n].instaStore.store_x)
				}
				positions.push(posotion);
			}
			// 마커 이미지의 이미지 주소입니다
			const imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
			for(let i = 0; i < positions.length; i++) {
				// 마커 이미지의 이미지 크기 입니다
				const imageSize = new kakao.maps.Size(24, 35);
				// 마커 이미지를 생성합니다    
			    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
			 	// 마커를 생성합니다
			    const marker = new kakao.maps.Marker({
			        map: map, // 마커를 표시할 지도
			        position: positions[i].latlng, // 마커를 표시할 위치
			        title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
			        image : markerImage // 마커 이미지 
			    });
			}
		};
		const options = {
		        // Drawing Manager를 생성할 때 사용할 옵션입니다
				map: map, // Drawing Manager로 그리기 요소를 그릴 map 객체입니다
		        drawingMode: [
		          	// drawing manager로 제공할 그리기 요소 모드입니다
		          	kakao.maps.drawing.OverlayType.POLYGON
		        ],
		        // 사용자에게 제공할 그리기 가이드 툴팁입니다
		        // 사용자에게 도형을 그릴때, 드래그할때, 수정할때 가이드 툴팁을 표시하도록 설정합니다
		        guideTooltip: ["draw", "drag", "edit"],
		        polygonOptions: {
		          	draggable: true,
		          	removable: true,
		          	editable: true,
		          	strokeColor: "#39f",
		          	fillColor: "#39f",
		          	fillOpacity: 0.5,
		          	hintStrokeStyle: "dash",
		          	hintStrokeOpacity: 0.5
		        }
		};
		// 위에 작성한 옵션으로 Drawing Manager를 생성합니다
		const manager = new kakao.maps.drawing.DrawingManager(options);
		manager.addListener('drawend', function(data) {
			document.getElementById("selectOverlay").value = 'on';
			document.getElementById("selectOverlay").innerHTML = '범위 선택';
		});
		// 버튼 클릭 시 호출되는 핸들러 입니다
  		const selectOverlay = (type) => {
  	  		let btn = document.getElementById("selectOverlay");
  	  		if(btn.value === 'on') {
  	  			btn.value = 'off';
  	  			btn.innerHTML = '선택 취소';
		    	// 클릭한 그리기 요소 타입을 선택합니다
		    	manager.select(kakao.maps.drawing.OverlayType[type]);
	  	  	} else {
	  	  		btn.value = 'on';
	  	 		btn.innerHTML = '범위 선택';
		  		// 그리기 중이면 그리기를 취소합니다
		   		manager.cancel();
		  	}
  		};
	   // 가져오기 버튼을 클릭하면 호출되는 핸들러 함수입니다
	   const getDataFromDrawingMap = () => {
		   // Drawing Manager에서 그려진 데이터 정보를 가져옵니다
	        const data = manager.getData();
	                
	        let points = [];
	        for(let i in data.polygon[0].points) {
				const point = {
			        	x: data.polygon[0].points[i]["x"],
			        	y: data.polygon[0].points[i]["y"]
			    };
				points.push(point);
			}
			search(points);
	   };
	   const setCenter = (lat, lng) => {
		   const moveLatLng = new kakao.maps.LatLng(lat, lng);
		   map.setCenter(moveLatLng);
	   };
	   const panTo = (lat, lng) => {
		   const moveLatLng = new kakao.maps.LatLng(lat, lng);
		   map.panTo(moveLatLng);
	   };
		
</script>
</body>
</html>