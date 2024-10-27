# WLS SSP Autotests
This is a shortened version of project description, you can read full version at https://teqblaze.atlassian.net/wiki/spaces/ES/pages/488112130

### Preparation
Before starting, install and set all the software from the Requirements section according to the instructions for each tool and with your current system specifics in mind. As an IDE you can use any tool you like, though **IntelliJ IDEA Community edition** is recommended

### Requirements:
- Java 17+ - https://jdk.java.net/ OR https://www.oracle.com/java/technologies/downloads/. Installing - 
- Maven - https://maven.apache.org/download.cgi. Installing - https://maven.apache.org/install.html
- Node.js - https://nodejs.org/uk/
- npm - https://nodejs.org/en/download/package-manager
- Allure - https://allurereport.org/docs/install/ OR https://www.npmjs.com/package/allure-commandline
- git - https://git-scm.com/downloads
- Chrome browser - latest stable build

### Initial setup

#### Clone project
Set up your GitLab SSH connection in your preferences https://gitlab.teqblaze.io/-/profile/keys and clone the autotests project to your working folder, for example /smarty/autotests/${projectName}

#### Setup local settings
This is required for the project to launch! Navigate to the src/test/resources/ make a copy of file settings-example.properties and rename it to settings.properties. Open the created settings file and change its default settings to match your local environment and other conditions, according to the description given in section https://teqblaze.atlassian.net/wiki/spaces/ES/pages/488112130/Automation+project+description#User-settings-(settings.properties)

#### Use global editorconfig settings
Go to Settings → Editor → Code style and select checkbox ‘Enable EditorConfig support' checkbox.

#### Expand Maven imports
Go to Settings → Build, Execution, Deployment → Build Tools → Maven → Importing. Here select checkboxes to Automatically download Sources, Documentation, Annotations

#### Commit settings
Go to Settings → Version control → Commit and select several checkboxes: Reformat code, Optimize imports. Other settings are left as default.

#### SSH Configuration
In case you are using payed IDEA Ultimate or DataGrip, you can add the SSH config to connect to the remote stage MySQL. Go to Settings → Tools → SSH Configurations. Click + button to add new config with the following settings:
- Host - IP address of the stage, same as stageLanIp in settings.properties
- Username - qaautotest
- Authentication type - Key pair
- Private key file - full path to the ${projectName}/src/test/resources/qaStageSsh
- Passphrase - empty

### Project structure:
- All files, produced on test run, are stored in the folder /RESULTS
- All tests are located in `/src/test/java`
- Core mechanics (Waits, Selectors etc.) are located in `/src/main/java/common`

# Tips on environment and tool setup

## Gmail account

To test the emails flow, we can use Mailhog (for local docker instance) and external mail service, for example Gmail (
for Stage and Production). To set the simple API access to the inbox, follow the steps:

- create a test account in gmail (for example, autotest@teqblaze.com)
- open gmail All settings, click on 'Forwarding and POP/IMAP' tab
- enable IMAP and save settings. This is considered a dangerous settings change, so the system will ask you to confirm
  this action by trusted device, sms, email or other method
- go to https://myaccount.google.com/apppasswords, register the new app name for the autotest project and copy the
  password, that Google provided for the app
- add the app password to the Data.StaticData.gmailAppPassword