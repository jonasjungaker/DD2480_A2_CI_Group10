window.onload = function () { 
    var testContainer = document.getElementsByClassName("test-container");
    var testTable = document.getElementsByClassName("build-list");
    var messageButton = document.getElementsByClassName("view-message-button");
    var shadowBox = document.getElementById("shdow-box");

    for(var i = 0; i < messageButton.length; i++) {
        messageButton[i].onclick = function(){showBox(this.id)};
    }

    shadowBox.onclick = function(){hideBox()};

    for(var i = 0; i < testContainer.length; i++) {
        if(testContainer[i].clientHeight < testTable[i].clientHeight) {
            this.addBlur(testContainer[i]);
        }
        testContainer[i].onscroll = function(){toggleBlur(this)};
    }
}

function showBox(i) {
    var shadowBox = document.getElementById("shdow-box");
    var messageContainer = document.getElementById("test-message-cont");

    shadowBox.setAttribute("style", "visibility: visible;");
    messageContainer.setAttribute("style", "display: block;");
    document.getElementById("box"+i).style.display = "block";
}

function hideBox() {
    document.getElementById("shdow-box").setAttribute("style", "visibility: hidden;");
    document.getElementById("test-message-cont").setAttribute("style", "display: none;");
    var messageContainers = document.getElementsByClassName("test-message");

    for(var i = 0; i < messageContainers.length; i++) {
        messageContainers[i].style.display = "none";
    }
}

function toggleBlur(box) {
    if(box.scrollTop >= box.scrollHeight - box.clientHeight) {
        box.setAttribute('style', '-webkit-mask-image: none');
    } else {
        addBlur(box);
    }
}

function addBlur(box) {
    box.setAttribute('style', '-webkit-mask-image: -webkit-gradient(linear,left 80%,left bottom,from(black),to(rgba(0,0,0,0)))');
}