function doSomething() {
    JSBridge.onDidSomething();
}

function launchCamera() {
    JSBridge.onLaunchCamera();
}

function injectTakePictureOnClick() {
    var takePicBtn = document.getElementsByClassName('step1')[0]; // This is the <a> element
    takePicBtn.onclick = launchCamera;
}
