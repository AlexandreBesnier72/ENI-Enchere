<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Inscription</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>

        <link rel="stylesheet" href="css/inscription.css">
    </head>
    <body>
        <h1><a class="text-white" href="${pageContext.request.contextPath}/">ENI - Enchères</a></h1>

        <section class="d-flex justify-content-center">

            <form action="${pageContext.request.contextPath}/inscription" method="post">
                <h1 class="text-center">Mon profil</h1>
                <div class="container">
                    <div class="row">

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="pseudo">Pseudo : <span class="text-danger">*</span></label>
                            </div>
                            <input id="pseudo" class="form-control" name="pseudo" type="text" placeholder="pseudo">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="nom">Nom : <span class="text-danger">*</span></label>
                            </div>
                            <input id="nom" class="form-control" class="form-control" name="nom" type="text" placeholder="nom">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="prenom">Prénom : <span class="text-danger">*</span></label>
                            </div>
                            <input id="prenom" class="form-control" name="prenom" type="text" placeholder="prénom">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="email">Email : <span class="text-danger">*</span></label>
                            </div>
                            <input id="email" class="form-control" name="email" type="email" placeholder="email">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="telephone">Téléphone : <span class="text-danger">*</span></label>
                            </div>
                            <input id="telephone" class="form-control" name="telephone" type="tel" placeholder="téléphone">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="rue">Rue : <span class="text-danger">*</span></label>
                            </div>
                            <input id="rue" class="form-control" name="rue" type="text" placeholder="rue">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="ville">Ville : <span class="text-danger">*</span></label>
                            </div>
                            <input id="ville" class="form-control" name="ville" type="text" placeholder="ville">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="codePostal">Code postal : <span class="text-danger">*</span></label>
                            </div>
                            <input id="codePostal" class="form-control" name="codePostal" type="text" placeholder="code postal">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="motDePasse">Mot de passe : <span class="text-danger">*</span></label>
                            </div>
                            <input id="motDePasse" class="form-control" name="motDePasse" type="password">
                        </section>

                        <section class="col-xs-12 col-sm-12 col-md-6 input-group mb-3">
                            <div class="input-group-prepend">
                                <label class="input-group-text" for="confirmation">Confirmation : <span class="text-danger">*</span></label>
                            </div>
                            <input id="confirmation" class="form-control" name="confirmation" type="password">
                        </section>
							<p class="text-danger text-right ml-3">* Champs obligatoire</p>
                        <section class="col-12 text-center">
                            <button class="btn btn-success mr-3" name="creer" type="submit">Créer</button>
                            <a class="btn btn-danger" href="${pageContext.request.contextPath}/">Annuler</a>
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
