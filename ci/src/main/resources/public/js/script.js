window.onload = function () { 
    var testContainer = document.getElementsByClassName("test-container");
    var testTable = document.getElementsByClassName("build-list");

    for (var i = 0; i < testContainer.length; i++) {
        if(testContainer[i].clientHeight < testTable[i].clientHeight) {
            testContainer[i].setAttribute('style', '-webkit-mask-image: -webkit-gradient(linear,left 80%,left bottom,from(black),to(rgba(0,0,0,0)))');
        }
    }
}