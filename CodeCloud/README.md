#teama/CodeCloud

CodeCloud Project files. 

Steps to run server
-------------------

1. Download and install docker. 

Docker has excellent documentation on how to install docker on various OS.
See: https://docs.docker.com/engine/installation/ Once you have sucessfully
installed docker and are able to run the hello-world image, proceed to the 
next step.

2. (For Mac OS and Windows only) Create bridged network adapter.

Docker only runs on linux and thus a docker-machine construct is used to create
a virtual machine running linux on top of your native OS. To access the server
across the network we must set up a bridged network to the virtual machine. 

    i) Run "docker-machine stop default" from a terminal.
    ii) Open VirtualBox, click on default and then settings. Open the network
        tab and then on adapter 3. Click "enable network adapter". Change
        attached to: to "bridged adapter". Change name to be the appropriate
        network adapter, whichever the host machine connects to the network
        with.
    iii) Run "docker-machine start default".
    iv) Run "docker-machine regenerate-certs default"
    v) Run "eval $(docker-machine env default)"\

3. Download docker image (be patient, it is ~1GB).

    i) Run "docker pull alexgrandt/codecloud". You may need to login using
       "docker login --username USERNAME --password PASSWORD". This requires
       a docker hub account so make one of this if you do not have one. Also,
       this repo is private, contact abrandt@mun.ca to gain access if "repo
       not found" error.

4. Start the docker image. 

    i) Run "docker run -it -p 4567:4567 --privileged -v /PATH/TO/THIS/REPO/:/home/user -w /home/user/CodeCloud alexgbrandt/codecloud".

    Notes: 
        a) On Mac OS the path to the repo must be under /Users.
           Ex: -v /Users/Alex/Documents/School/4770/teama:/home/user
        b) On Windows the path to the repo must be under C:\Users
           And is specified starting by /c/.
           Ex: -v /c/Users/Alex/Documents/School/4770/teama:/home/user
        c) Windows can be fickle with tty input depending on your terminal.
           If executing the above command results in an error regarding tty
           you must launch the program using a tty-based input. This can
           be solved using a program winpty: https://github.com/rprichard/winpty
           Then with winpty installed, prepent "winpty" to the above command.
           i.e.: "winpty docker run -it..."

5) Download another docker image. 

We are now inside a docker container which is running Ubuntu. We need another
docker image which is used by the server as a sandbox. Docker is already installed
on this image so we simply need to start it and download the image. 

    i) Run "service docker start" to start docker as we are on Ubuntu.
    ii) Ensure docker has started with "docker version" and look for valid
        server information. If it was unsuccessful (which happens sometimes) 
        simple exit the container with "exit" and repeat from step 3 part i.
    iii) Run "docker pull alexgbrandt/sandbox".

6) Run the server. 

    i) Run "ant CodeCloud".

7) Access the server.

  Mac OS and Windows:
    i) From a terminal on the host machine (not in either docker container) 
       run the command "docker-machine ip default". This returns the ip address
       of the virtual machine to be used to access the server. 
       e.g.: "192.168.1.184:4567/login.html" if 192.168.1.184 was returned by 
       "docker-machine ip default".

  Linux: 
    i) Since there is no virtual machine running you can simply use localhost.
       To get the actual ip address for the local network run "ifconfig" and
       look for an ip address begining with "192.168". 
       e.g.: "localhost:4567//".
