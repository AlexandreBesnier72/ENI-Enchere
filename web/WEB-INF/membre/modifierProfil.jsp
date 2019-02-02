<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Modifier Profil</title>

        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modifierProfil.css">
    </head>
    <body>
        <header class="bg-info">
            <h1 class="p-3">
                <a class="text-white" href="${pageContext.request.contextPath}/">ENI - Enchères</a>
            </h1>
        </header>

        <section class="d-flex justify-content-center">

            <form  action="${pageContext.request.contextPath}/membre//modifierProfil" method="post">
                <h2 class="text-center m-4">Modifier profil</h2>
                <div class="container ">
                    <div class="row">

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="pseudo">Pseudo :<span class="text-danger">*</span></label>
                            </div>
                             <input class="form-control" name="pseudo" value="${utilisateurActuel.pseudo }" id="pseudo" type="text" placeholder="">
                        </section>

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="nom">Nom :<span class="text-danger">*</span></label>
                            </div>
                            <input class="form-control" name="nom" value="${utilisateurActuel.nom }" id="nom" type="text" placeholder="">
                        </section>
                     
					
                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="prenom">Prénom :<span class="text-danger">*</span></label>
                            </div>
                           <input class="form-control " name="prenom" value="${utilisateurActuel.prenom }" id="prenom" type="text" placeholder="">
                        </section>

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="email">Email :<span class="text-danger">*</span></label>
                            </div>
                            <input class="form-control " name="email" value="${utilisateurActuel.email }" id="email" type="email" placeholder="">
                        </section>
					
                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="telephone">Téléphone :</label>
                            </div>
                            <input class="form-control " name="telephone" value="${utilisateurActuel.telephone }" id="telephone" type="text" placeholder="">
                        </section>

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="rue">Rue :<span class="text-danger">*</span></label>
                            </div>
                            <input class="form-control " name="rue" value="${utilisateurActuel.rue }" id="rue" type="text" placeholder="">
                        </section>

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="codePostal">Code postal :<span class="text-danger">*</span></label>
                            </div>
                            <input class="form-control " name="codePostal" value="${utilisateurActuel.codePostal }" id="codePostal" type="text" placeholder="">
                        </section>
                         <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="ville">Ville :<span class="text-danger">*</span></label>
                            </div>
                            <input class="form-control " name="ville" value="${utilisateurActuel.ville }" id="ville" type="text" placeholder="">
                        </section>

                        <section class="col-12 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="ancienMdp">Mot de passe actuel :</label>
                            </div>
                            <input class="form-control " name="ancienMdp" id="ancienMdp" type="password" placeholder="">
                        </section>

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="nouveauMdp">Nouveau mot de passe :</label>
                            </div>
                            <input class="form-control " name="nouveauMdp" id="nouveauMdp" type="password" placeholder="">
                        </section>

                        <section class="col-xs-12 col-sm-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="confirmationMdp">Confirmation :</label>
                            </div>
                             <input class="form-control " name="confirmationMdp" id="confirmationMdp" type="password" placeholder="">
                        </section>
                        
                        <section class="col-12 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text">Crédit :</label>
                            </div>
                             <div class="form-control"> ${utilisateurActuel.credit }</div>
                        </section>

                        <p class="text-danger text-right ml-4">* Champs obligatoire</p>

                        <section class="col-12 text-center">
                            <input type="hidden" id="noUtilisateur" name="noUtilisateur" value="${utilisateurActuel.noUtilisateur}">
                            <input class="btn btn-success mr-3" name="btn" type="submit" value="Enregistrer">
                        	<input class="btn btn-danger" name="btn" type="submit" value="Supprimer">
                        </section>

                        <section class="col-12 text-center m-3">
                            <div class="text-success">${sessionScope.succes}</div>
                            <div class="text-danger">${sessionScope.erreur}</div>
                        </section>
                    </div>
               </div>
            </form>

        </section>

    </body>
</html>
