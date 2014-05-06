Android webview project - GT
==========
In your button onclick you would need to call the "launchCamera" method that's in assets/injects.js.  
All the magic happens in: 
	src/com/ghostruck/android/MainActivity.java 
	assets/inject.js
	res/layout/activity_main.xml.
I actually went ahead and added some code that injects an onclick listener into your 'take picture' button, and it now launches the camera, and returns to the app upon finalizing the picture.  
You will have to add some code to do what you want with the image, but that should all be there in the stackoverflow link.  
It would probably be better to not inject the onclick though in the app and it come from the webpage
