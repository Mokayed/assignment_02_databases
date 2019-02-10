# assignment_02_databases
assignment_02_databases with java and mongo in docker container

1-Go to your vagrant or Linux, to creater a docker container by typing “docker run --rm -v $(pwd)/data:/data/db --publish=27017:27017 --name dbms -d mongo”

2-after creating the dbms container use this cmd to access the dbms container “docker exec -it dbms bash”.

3-after accessing the container, update the apt with “apt-get update”,then use this cmd to download wget “apt-get install -y wget”,then download unzip using “apt-get install -y unzip”.

4.Download the tweeter training and test data zip file by typing “wget http://cs.stanford.edu/people/alecmgo/trainingandtestdata.zip”.

5-then unzip the trainingandtestdata file by “unzip trainingandtestdata.zip”.

6-Now you ready to convert the data in tu uf8 by typing “iconv -f ISO-8859-1 -t utf-8 training.1600000.processed.noemoticon.csv”.

7-Then get the header ready by doing “sed -i '1s;^;polarity,id,date,query,user,text\n;' converted-utf8.cs” cmd.

8- Then use the mongoimport cmd “mongoimport --host=127.0.0.1 -d tweets -c data --type csv --file converted-utf8.csv --headerlineconnected to: 127.0.0.1”.

9- Now test if the database is working as it should by, connecting to it from the GUI client RoboMongo, see https://robomongo.org/download.

10- You are ready now open then netbeans project which is made in java, then rune the “mainDB” class.

My systems responses to the following questions:
How many Twitter users are in the database?

1598315

Which Twitter users link the most to other Twitter users? (Provide the top ten.)

TBD

Who isare the most mentioned Twitter users? (Provide the top five.)

TBD
Who are the most active Twitter users (top ten)?

TBD

Who are the five most grumpy (most negative tweets) and the most happy (most positive tweets)?

TBD

Note: if you are using Vagrant and you cannot connect to the docker container from your computer in (robo 3t,) put this in the Vagrantfile:

##-*- mode: ruby -*-

##vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "bento/ubuntu-16.04"
  config.vm.network "private_network", ip: "192.168.33.10"
  config.vm.synced_folder "~/", "/host_home", type: "virtualbox"
  config.vm.network "forwarded_port", guest: "27017", host: "27017"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
    vb.cpus = "1"
	config.vm.provision "docker" do |d|
    d.run 'dockerfile/mongodb', args: "-p 27017:27017", name: 'dbms'
  end
  end
  config.vm.provision "shell", privileged: false, inline: <<-SHELL
      sudo apt-get update
  
      sudo echo "LC_ALL=\"en_US.UTF-8\"" >> /etc/environment
      sudo locale-gen UTF-8
  
      sudo apt-get install -y git
      sudo apt-get install -y wget
      sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
      curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
      sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
      sudo apt-get update
      sudo apt-get install -y docker-ce
      curl https://getmic.ro | sudo bash
      sudo mv ./micro /usr/local/bin
  
      echo "==================================================================="
      echo "=                             DONE                                ="
      echo "==================================================================="
      echo "To log onto the VM:"
      echo "$ vagrant ssh"  
    SHELL
end

