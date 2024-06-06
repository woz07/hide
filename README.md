# hide
An application to cipher texts

> Note: Hide isn't a 100% secure application, this is because the method of ciphering isn't the more best.
> So if you're looking for an app that offers high security then hide would not be it.
> <br>
> <br>
> Hide is not responsible for any issues nor consequences caused by using the app, 
> you have your own responsibility of how you use hide and the consequences that may follow.

> Based on https://www.github.com/woz07/bcipher

## About
Hide is an application that allows you to cipher texts. It allows users to insert what keys they would 
like to cipher their texts with. It also allows users to add and delete keys which are used in the 
encryption. 
<br>
<br>
So, let's say you have a piece of text, `Hello my friend`, which you want to cipher.
What you can do is enter that text into the text box and then set a key, in this example we'll use 
a key of 1, although it's best to use multiple keys and try to make the key as long as possible.
Once we set the key and then press the `Cipher` button then text box at the bottom, which represents 
the ciphered text, becomes `Idmmn!lx!gshdoe`.

> Note: You can have max 255 keys, of which all of them must be greater than 0 but less than or equal 
> to 127

## How to use
### Downloading
In order to download you need to go to `Releases` on the GitHub (https://github.com/woz07/hide/releases)
then you need pick a version, and then you need to install whichever executable you want, either the `.jar` 
or the `.exe`, if you are  on Windows then I recommend the `.exe`, but it's all up to you whether you want 
to install the `.exe`.

> Note: Only install the executable via the `releases`, don't install the executable(s) in the source code

### Using
Once you have installed it, you can then run the app. Once you are running the app, before you can even cipher 
any text, you must add some keys which are vital for the ciphering process. These keys are required and the 
program will show errors if you haven't already set them
<br><br>
The next step is to enter your text in the top box which is the input text box in the window, here you just
enter whatever you want to get ciphered. You do not enter anything in the bottom text box, it will 
automatically just get text added to it once you click on the cipher button.
<br><br>
Once you have added keys and added text in the top text box in the main window then you just need to click on 
the cipher button in the main window, and then you'll get your ciphered text in the bottom text box.
There you go, you have now ciphered your text. I hope you enjoy using Hide.

> Note: There is undo (CTRL + Z) and redo (CTRL + SHIFT + Z) on both the text boxes.

## Help
### File errors
#### 1.1
This error code means that the program was unable to create a folder called `Hide` within
%APPDATA%. To fix this issue, just locate your `%APPDATA%` folder and then create a folder called 
`Hide`, exactly how it's been spelt.
#### 1.2
This error code means that the program was unable to create the file `config.txt` within 
`%APPDATA\\Hide`, if this happens, go through 1.1 first and then if it still hasn't been fixed, you
will need to create a txt file called `config`, exactly how it's been spelt here.
#### 1.3
This error code just means that it was unable to create `config.txt` within the folder `Hide`,
just follow the steps of 1.1 and 1.2.
> Note: The reason why this was even given its own error code was because the backend process of this 
> is different to 1.1 and 1.2 and is only triggered upon a system error

### Ciphering key errors
#### 2.1
This error code is shown only when you try to cipher and the keys are empty. You need to enter 
keys before ciphering
> Note: The keys are not saved once you close the app! So you need to reenter them once opening 
> the app
#### 2.2
This is a system error on behalf of the ciphering library and is thrown when you disobey the rules.
Which are; keeping all keys smaller than or equal to 127 and greater than 0 while also ensuring that 
you have max only 255 keys.

### Link/Browser errors
#### 3.1
This error code just means that the app was unable to open the link it wanted to open. 
There is no fix for this, but it wanted to redirect you to this page anyways.

### Adding keys errors
#### 4.1
This error code just means that you have already reached the maximum amount of keys, which is 255,
so in order to add more keys you will need to remove some keys
#### 4.2
You provided an invalid format to remove keys, you need to do `n key` where n is a natural number
within the range of 1 to 255 but also need to make sure that n + already existing keys isn't equal to 
255, as that breaks bound rules.
<br>
Here is an example that would pass: <br>
"5 20" <br>
> Note: This would create 5 keys of value 20
#### 4.3
This means that the provided key entered is invalid, so it's not within the requirements which are;
being within 1 to 127, and also it needs to actually be a number

### Removing keys errors
#### 5.1
This error code means that you have provided an invalid format. If you wanted to remove an amount 
of keys but not all you can do `n key` where n is how many of that key would like to remove and key
is the key you would like to remove.
> Note: If n is greater than how many keys there actually are then it removes all the keys without 
> showing an error

If you want to delete all the keys then you just do `* key` where key represents the key you want
to clear out
#### 5.2
This error code just means that the key you tried to remove doesn't actually even exist in the 
keys, so it can't remove something that does not exist.
#### 5.3
This error code just means that you provided an invalid input, which could mean you didn't follow 
the rules which are to keep keys within 1 to 127, and to make sure they are actually digits rather 
than letters.

## Deleting
### How to delete properly
In order to delete the app properly, you need to delete the exe/jar, whichever you choose to install, 
and then also ensure that you also delete the `hide` folder within your AppData folder which you can 
get to by typing `Run` into the windows search bar and then typing in `%APPDATA%` then searching for 
the hide folder and deleting it.

## External libraries used
### FlatLaf (https://github.com/JFormDesigner/FlatLaf)
This was used for the GUI and making it look more modern and amazing!

### BCipher (https://github.com/woz07/bcipher)
This was used for the ciphering

### Launch4j (https://sourceforge.net/projects/launch4j/)
A cross-platform java executable wrapper, which let me convert my `.jar` to `.exe`
