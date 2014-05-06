window.alert("INJECT LOADED");
Toast.makeText(MainActivity.this, "INJECT LOADED", Toast.LENGTH_SHORT).show();

function doSomething() {
    JSBridge.onDidSomething();
    Toast.makeText(MainActivity.this, "INJECT SOMETHING", Toast.LENGTH_SHORT).show();
}

function launchCamera() {
    JSBridge.onLaunchCamera();
}

function injectTakePictureOnClick() {
    Toast.makeText(MainActivity.this, "CAMERA INIT", Toast.LENGTH_SHORT).show();
    var takePicBtn = document.getElementsByClassName('step1')[0]; // This is the <a> element
    takePicBtn.onclick = launchCamera; // should launchCamera have ()
    Toast.makeText(MainActivity.this, "CAMERA LAUNCH", Toast.LENGTH_SHORT).show();
}
