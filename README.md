# amComposeNetwork

## Architecture 

The aim of this program is not to make a great UI, it is just to demonstrate what Compose can do quickly, with a CleanArchi environment.

- Kotlin
- Compose and Navigation for Compose
- Clean Archi / MVVM
- 100% Google : Flow / StateFlow / Coroutines and Hilt
- Room with adapters
- Retrofit2 with Moshi adapters
- Arrow Either
- Bonus : use new TOML gradle dictionnary !

What remains to do :
- I have an error when I download a file and then wants to delete it : "The process cannot access the file because it is being used by another process"
- Sometimes, i dont know why I need to relaunch the .exe, because I want to delete a item with a incorrect ID, but it is however correct.

For exemple here id fdff0e150a50fd88102bf7e16bb7fbad14daebc9
--> GET http://10.0.2.2:8080/items/72652fd4500251a60c6cf06d7057cbaca041f9ed
--> END GET
<-- 200 OK http://10.0.2.2:8080/items/72652fd4500251a60c6cf06d7057cbaca041f9ed (5ms)
Content-Type: application/json; charset=utf-8
Date: Sun, 20 Feb 2022 16:19:45 GMT
Content-Length: 891
[{"id":"5c6e01c551391dd882dd281499d1ddd388efc099","parentId":"72652fd4500251a60c6cf06d7057cbaca041f9ed","name":"211227135008-02-the-batman-trailer-super-tease.jpg","isDir":false,"size":32139,"contentType":"image/jpeg","modificationDate":"2022-02-20T16:53:24.5914575+01:00"},{"id":"fdff0e150a50fd88102bf7e16bb7fbad14daebc9","parentId":"72652fd4500251a60c6cf06d7057cbaca041f9ed","name":"IMG_20220209_163706.jpg","isDir":false,"size":200347,"contentType":"image/jpeg","modificationDate":"2022-02-20T17:16:50.9873713+01:00"},{"id":"8686899b7007ff1d6bead77ddbdff25ee145b364","parentId":"72652fd4500251a60c6cf06d7057cbaca041f9ed","name":"aa","isDir":true,"modificationDate":"2022-02-20T16:35:15.7502203+01:00"},{"id":"ba6b97fcabce4976391e8cde29f071b92cc870f6","parentId":"72652fd4500251a60c6cf06d7057cbaca041f9ed","name":"aaa","isDir":true,"modificationDate":"2022-02-10T08:57:18.8944523+01:00"}]
<-- END HTTP (891-byte body)

--> DELETE http://10.0.2.2:8080/items/fdff0e150a50fd88102bf7e16bb7fbad14daebc9
--> END DELETE
<-- 404 Not Found http://10.0.2.2:8080/items/fdff0e150a50fd88102bf7e16bb7fbad14daebc9 (3ms)
Content-Type: application/json; charset=utf-8
Date: Sun, 20 Feb 2022 16:19:53 GMT
Content-Length: 66
{"error":"Unknown item fdff0e150a50fd88102bf7e16bb7fbad14daebc9"}
<-- END HTTP (66-byte body)



## UI

## Login screen

A simple login screen with login / password

<img src="https://github.com/AntoineMarchaud/amComposeNetwork/blob/master/readme/login.png" width="400" />

## Navigation screen

By default the screen is displayed with master folder information :

<img src="https://github.com/AntoineMarchaud/amComposeNetwork/blob/master/readme/navigable.png" width="400" />

It is possible to add a folder:

<img src="https://github.com/AntoineMarchaud/amComposeNetwork/blob/master/readme/navigable_add_folder.png" width="400" />

It is possible to delete a folder by long click :

<img src="https://github.com/AntoineMarchaud/amComposeNetwork/blob/master/readme/navigable_delete_folder.png" width="400" />

It is possible to display file option by long clicking.
User can delete file by swiping left the displayed item too.
If the user choose "download" it will automatically display full screen image.

<img src="https://github.com/AntoineMarchaud/amComposeNetwork/blob/master/readme/navigable_delete_file.png" width="400" />

The user can add photos by clicking "pick image" at the bottom. 
