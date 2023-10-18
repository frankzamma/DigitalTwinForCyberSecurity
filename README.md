<p align="center">
    <img width="200" src="https://github.com/frankzamma/DigitalTwinForCyberSecurity/assets/65612000/2300d151-8c03-4552-9dd3-e6855a020330">
</p>
<h1 align="center">DigitalTwinForCyberSecurity</h1>
This repository contains my bachelor degree project that is a prototype of Intellij plugin that rapresents a digital twin of a security expert that could help developers to perform cybersecurity checks on software projects.

# How to Run project
1. Download and Install [Intellij IDEA](https://www.jetbrains.com/idea/download/), if you don't have it.
2. Clone this repository in Intellij.
3. Wait to Gradle build.
4.  Once the Gradle build is complete, you should have this in the top right:<br>
   <img width="278" alt="image" src="https://github.com/frankzamma/DigitalTwinForCyberSecurity/assets/65612000/0733f270-7cb0-4214-9e58-ece87f8a06cc">
<br>
   To run project, click on play button. This action will open a new Intellij window where the plugin is available. If this is your first run you need to choose or create a project to open in intellij.
5. To use project, you need create an account on <a href="openai.com">openai.com</a>, go to this [page](https://platform.openai.com/account/billing/overview) and charge the account with your credit card. Next, you should create a new API secret token at this [link](https://platform.openai.com/account/api-keys) and insert it in the settings of plugin in Intellij: <br><br>

![Screenshot 2023-10-18 181838](https://github.com/frankzamma/DigitalTwinForCyberSecurity/assets/65612000/a7c3414d-495e-4993-a42c-5b06c1289db5)

# Functionality
There are three major functionality available in the first version of plugin:
1. Static analysis: The digital twin analyzes the current file (or the entire project) and describes to you the vulnerabilities it finds in it.
2. Analysis during writing: The digital twin analyzes your code during writing and send notification if you insert a vulnerability. To use this fuction you active it in the settings.
3. Support to Dynamic Analysis: the digital twin can help you to choose, install and configure a tool to execute dynamic analysis on your project.

# Results
In my thesis, which will be available soon, I research a study around the development of this project. The results of the experiments, discussed in the thesis, can be viewed in the folder [AnalisiRisultati](https://github.com/frankzamma/DigitalTwinForCyberSecurity/).

# Credits
The icon of this project is <a href="https://www.flaticon.com/free-icons/cyber-attack" title="cyber attack icons">Cyber attack icons created by Assia Benkerroum  - Flaticon</a>

    
