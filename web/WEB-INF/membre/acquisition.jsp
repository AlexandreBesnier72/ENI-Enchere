<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Aquisition</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
	 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
	 <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aquisition.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
        
</head>
<body>
	<header class="bg-info">
		<h1 class="p-3"><a class="text-white" href="${pageContext.request.contextPath}/">ENI - Enchères</a></h1>
            
    </header>
	<section>
    	<h2 class="text-center">Vous avez remporté la vente</h2>
			 <div class="container ">
		                <div class="row">
		                    <section class="col-xs-12 col-sm-12 col-md-4 mb-3">
		                        <img class="img-fluid" src="${pageContext.request.contextPath}/images/encheres/gris.jpg"
		                             alt="enchere">
		                    </section>
		
		                    <section class="col-xs-12 col-sm-12 col-md-8">
		                    	<ul class="list-group list-group-flush">
								  <li class="list-group-item d-flex flex-wrap  ">
								  	<div class="col-12"  style="font-size:1.4em; ">${requestScope.article.nomArticle} </div>
								  									  	
								  </li>
								  <li class="list-group-item d-flex flex-wrap">
								  	<div class="col-4   text-info font-weight-bold   ">Description :  </div>
								  	<div class="col-8 ">${requestScope.article.description }</div>
								  </li>
								  <li class="list-group-item d-flex flex-wrap">
								  	<div class="col-4   text-info font-weight-bold ">Meilleure offre : </div>
								  	<div class="col-8  ">${requestScope.article.prixVente }</div>
								  </li>
								  <li class="list-group-item d-flex flex-wrap">
								  	<div class="col-4   text-info font-weight-bold  ">Mise à prix : </div>
								  	<div class="col-8  ">${requestScope.article.prixInitial }</div>
								  </li>
								  <li class="list-group-item d-flex flex-wrap">
								  	<div class="col-4   text-info font-weight-bold   ">Retrait : </div>
								  	<div class="col-8  d-flex flex-wrap ">
								  		<div >${requestScope.retrait.rue }</div>
								  		<div >${requestScope.retrait.codePostal } ${requestScope.retrait.ville }</div>
								  	</div>
								  </li>
								  <li class="list-group-item d-flex flex-wrap">
								  	<div class="col-4   text-info font-weight-bold   ">Vendeur : </div>
								  	<div class="col-8  ">${requestScope.utilisateur.pseudo }</div>
								  </li>
								  <li class="list-group-item d-flex flex-wrap">
								  	<div class="col-4   text-info font-weight-bold   ">Tel : </div>
								  	<div class="col-8  ">${requestScope.utilisateur.telephone }</div>
								  </li>		                    
		                    	</ul>
		                    	
		                    	 <a class="btn btn-dark mx-4 my-4 bouton" href="${pageContext.request.contextPath}/">Retour</a>		                    
		                    </section>
						</div>
			</div>
	</section>
</body>
</html>