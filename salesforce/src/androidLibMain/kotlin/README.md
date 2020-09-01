## Please check [Documentation](../../../../README.md#androidLibMain-sources)

## How to use the Salesforce Login in an Android App

First, you need to add the credentials to connect to your specific endpoint on SalesForce.
These credentials should be taken from your SalesForce account.
These values should be added as strings, the following are mandatory:


    <string name="sf_serverUrl">insert here the url of the SalesForce server</string>
    <string name="sf_ConsumerKey">insert here value of remoteAccessConsumerKey from SalesForce</string>
    <string name="sf_oauthRedirectURI">insert here value of URI for successful authentication with oauth</string>

Secondly, make your activity extend `KalugaSalesForceLoginActivity` and provide a fitting ViewModel (by extending BaseSalesForceViewModel)
