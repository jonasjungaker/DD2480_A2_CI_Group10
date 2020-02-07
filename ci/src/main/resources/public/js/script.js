/*
Code runes when the window is loaded
*/
window.onload = function () { 
    var testContainer = document.getElementsByClassName("test-container");
    var testTable = document.getElementsByClassName("build-list");
    var messageButton = document.getElementsByClassName("view-message-button");
    var shadowBox = document.getElementById("shdow-box");

    centerPopup();

    for(var i = 0; i < messageButton.length; i++) {
        //Displays a popup box when a button has been clicked 
        //messageButton[i].onclick = function(){showBox(this.id)};
        messageButton[i].onclick = function(){showBox(this.children[0])};
    }

    //Hides the popup box when the shadow box has been clicked.
    shadowBox.onclick = function(){hideBox()};

    for(var i = 0; i < testContainer.length; i++) {
        if(testContainer[i].clientHeight < testTable[i].clientHeight) {
            this.addBlur(testContainer[i]);
        }
        //Executes the toggleBlur method when scrolling
        testContainer[i].onscroll = function(){toggleBlur(this)};
    }
}

/*
The function bellow displays the popup box
*/
function showBox(i) {
    var shadowBox = document.getElementById("shdow-box");
    console.log(i.getAttribute('style') );
    shadowBox.setAttribute("style", "visibility: visible;");
    i.setAttribute("style", i.getAttribute('style') + " visibility: visible;");
}

/*
The function hides the popubox
*/
function hideBox() {
    document.getElementById("shdow-box").setAttribute("style", "visibility: hidden;");
    var textMainContainter = document.getElementsByClassName("test-message-cont");

    for(var i = 0; i < textMainContainter.length; i++) {
        textMainContainter[i].style.visibility = "hidden";
    }
}

/*
The function bellow removes the blur when the user scrolles 
down to the bottom of box's child
*/
function toggleBlur(box) {
    if(box.scrollTop >= box.scrollHeight - box.clientHeight) {
        box.setAttribute('style', '-webkit-mask-image: none');
    } else {
        addBlur(box);
    }
}

/*
The function bellow adds blur to the bottom of box
*/
function addBlur(box) {
    box.setAttribute('style', '-webkit-mask-image: -webkit-gradient(linear,left 80%,left bottom,from(black),to(rgba(0,0,0,0)))');
}

/**
 * Centers the popup box
 */
function centerPopup() {
    var textMainContainter = document.getElementsByClassName("test-message-cont");
    for(var i = 0; i < textMainContainter.length; i++) {
        var center = (screen.width - textMainContainter[i].clientWidth) / 2;
        textMainContainter[i].style.position = "fixed";
        textMainContainter[i].setAttribute("style", "left: "+center+"px !important;");
    }
}