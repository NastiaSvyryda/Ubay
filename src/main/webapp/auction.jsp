<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta name="description" content="Ubay">
    <meta name="keywords" content="HTML, CSS, JS, Java, ucode, unitfactory, cbl, cblworld">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ubay</title>
    <link rel="shortcut icon" href="resources/favicon.ico" type="image/x-icon">
    <link rel="icon" href="resources/favicon.ico" type="image/x-icon">
    <link href="https://fonts.googleapis.com/css?family=Lato:300,400,700,900" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="resources/reset.css"/>
    <link rel="stylesheet" href="resources/main.css"/>
    <link rel="stylesheet" href="resources/profile.css"/>
    <link rel="stylesheet" href="resources/auction.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="resources/references.js"></script>
    <script src="resources/auction.js"></script>
</head>

<body>

<div class="help_footer">
    <header class="header">
        <div class="header-top-back"></div>

        <div class="header-top page">
            <div class="header-features">
          <span data-text="We deliver free of charge throughout Ukraine.">
          <span>Free delivery in Ukraine!</span>
          </span>
            </div>
            <div class="header-menu">
                <ul>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Service</a></li>
                    <li><a href="#">Contacts</a></li>
                </ul>
            </div>
            <div class="header-login">
                <a class="abutton" href="${pageContext.request.contextPath}/authorization">Sign in</a>
                <a class="abutton" href="${pageContext.request.contextPath}/profile">Profile</a>
            </div>
        </div>

        <div class="header-middle page">
            <div class="header-contact">
                <!--        <span>(044) 044 04 04</span>-->
            </div>
            <div title="internet shop" class="logo">UBAY</div>
            <div class="header-basket">
                <!--         <a>cart<span class="front-cart">(0)</span></a>-->
            </div>
        </div>
    </header>

    <nav class="breadcrumps">
        <ul class="page crumbs">
            <li><a class="abutton" href="${pageContext.request.contextPath}/main">Home</a></li>
            <li><a id="lotId">Lot #</a></li>
        </ul>
    </nav>

    <h3 id="title" class="page"></h3>

    <main class="page height">
        <div class="personal-section lot-img">
            <img src="resources/favicon.ico" alt="image">
        </div>
        <div class="personal-section lot-description">
            <div class="personal-section__header">
                <div></div>
                <div>
                    <a style="color: #494d5f" href="#" onclick="viewProfile(this)"><span id="aboutProfile">seller</span>
                        <span class="seller-rating seller-rating fa fa-fw fa-star field-icon"></span><span id="rate" class="seller-rating"></span>
                    </a>
                    <a class="button" href="#" onclick="viewFeedbacks(this)">feedbacks</a>
                </div>
            </div>
            <p>Current price:&emsp;<span id="price"></span></p>
            <p>Start time:&emsp;<span id="startTime"></span></p>
            <p>Time left to closure:&emsp;<span id="timer"></span></p>
            <p id="description"></p>

            <div class="personal-section__header">
                <div>
                    <form action="newBit" method="POST" name="form" style="display: none">
                        <label for="newPrice">New price </label>
                        <input id="newPrice" class="button" type="number" name="price" required
                               min=".01" step=".01">
                        <input class="button" type="submit" value="Submit new bit">
                        <a class="button" href="#" onclick="location.reload()">Return</a>
                    </form>
                </div>
                <div id="addBit-buttons" style="display: none">
                    <a class="button" href="#" onclick="auctions.addBit()">Add bit</a>
                    <a class="button" href="#" onclick="window.history.back()">Return</a>
                </div>
            </div>

            <div class="personal-section__header" id="winner" style="display: none">
                <p>Winner: </p>
                <p id="add-feedback-button">
                    <a class="button" href="#" onclick="auctions.addFeedback(this)">Add feedback</a>
                </p>
            </div>
            <p id="feedback"></p>
        </div>
    </main>

</div>

<footer>
    <div class="footer-back"></div>
    <div class="footflexbox page">
        <div class="copyright footbox">
            <p>Copyright &copy; 2020 | Ubay | All Rights Reserved</p>
        </div>
        <div class="terms footbox">
            <p>Terms of Service | Privacy Policy</p>
        </div>
    </div>
</footer>

</body>

</html>

