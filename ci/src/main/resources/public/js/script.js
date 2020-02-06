/*
Code runes when the window is loaded
*/
window.onload = function () { 
    var testContainer = document.getElementsByClassName("test-container");
    var testTable = document.getElementsByClassName("build-list");
    var messageButton = document.getElementsByClassName("view-message-button");
    var shadowBox = document.getElementById("shdow-box");

    for(var i = 0; i < messageButton.length; i++) {
        //Displays a popup box when a button has been clicked
        messageButton[i].onclick = function(){showBox(this.id)};
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
    var messageContainer = document.getElementById("test-message-cont");

    shadowBox.setAttribute("style", "visibility: visible;");
    messageContainer.setAttribute("style", "display: block;");
    document.getElementById("box"+i).style.display = "block";
}

/*
The function hides the popubox
*/
function hideBox() {
    document.getElementById("shdow-box").setAttribute("style", "visibility: hidden;");
    document.getElementById("test-message-cont").setAttribute("style", "display: none;");
    var messageContainers = document.getElementsByClassName("test-message");

    for(var i = 0; i < messageContainers.length; i++) {
        messageContainers[i].style.display = "none";
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