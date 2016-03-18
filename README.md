# Auction Sniper

## Openfire installation
Download and start the server:
```
wget http://www.igniterealtime.org/downloadServlet?filename=openfire/openfire_4_0_1.tar.gz -O - | tar -xz
./openfire/bin/openfire start
```

Go to the http://localhost:9090/ and finish the setup:
* Select the language
* Set domain to "localhost"
* Select "Embedded Database"
* Select "Default" profile
* Set email address and the password (default username is "admin")

Add required accounts:
_Users/Groups -> Create New User_

| Username           | Password |
|--------------------|----------|
| sniper             | sniper   |
| auction-item-54321 | auction  |
| auction-item-65432 | auction  |

Configure the server
* _Server -> Server Settings -> Resource Policy_ set to "Never kick"
* _Server -> Server Settings -> Offline Messages_ set to "Drop"
* _Server -> Server Settings -> File Transfer Settings_ set to "Disabled"